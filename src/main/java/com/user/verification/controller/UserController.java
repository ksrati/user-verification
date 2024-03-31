package com.user.verification.controller;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.user.verification.CustomException.CustomError;
import com.user.verification.CustomException.CustomValidationException;
import com.user.verification.model.FilteredAndTotalUsers;
import com.user.verification.model.User;
import com.user.verification.pagination.CustomPage;
import com.user.verification.service.UserRepositoryService;
import com.user.verification.service.UserService;
import com.user.verification.validators.Validator;
import com.user.verification.validators.ValidatorFactory;

@RestController
public class UserController {

	 private final UserService userService;
	    private final ValidatorFactory validatorFactory;
	    private final UserRepositoryService userRepositoryService;

    @Autowired
    public UserController(UserService userService, ValidatorFactory validatorFactory,UserRepositoryService userRepositoryService) {
        this.userService = userService;
        this.validatorFactory = validatorFactory;
        this.userRepositoryService = userRepositoryService;
    }

    @PostMapping("/saveUser")
    public ResponseEntity<List<User>> verifyUser(@RequestParam(defaultValue = "1") int size) {
    	 Validator sizeValidator = validatorFactory.getValidator("size");
    	 boolean isSizeValid = sizeValidator.validate(String.valueOf(size));
    	 if(!isSizeValid) {
    		 throw new CustomValidationException("Invalid size parameter");
                		 
    	 }
     return ResponseEntity.ok(userService.verifyUserDetails(size));
        
    }
    

    
    
    @GetMapping("/listUsers")
    public ResponseEntity<?> getUsers(
            @RequestParam String sortType,
            @RequestParam String sortOrder,
            @RequestParam int limit,
            @RequestParam int offset
    ) {
    	try {
    	//check if db is empty
    	if (userRepositoryService.getUsersCount() == 0L) {
            throw new CustomValidationException("No User Data Exists in the database ");
        }
    	
        // if not empty then Validate sortType and sortOrder using appropriate validators from validatorFactory
        Validator sortTypeValidator = validatorFactory.getValidator("sortType");
        Validator sortOrderValidator = validatorFactory.getValidator("sortOrder");
        Validator limitValidator = validatorFactory.getValidator("limit");
        Validator offsetValidator = validatorFactory.getValidator("offset");

        boolean isSortTypeValid = sortTypeValidator.validate(sortType);
        boolean isSortOrderValid = sortOrderValidator.validate(sortOrder);
        boolean isLimitValid = limitValidator.validate(String.valueOf(limit));
        boolean isOffsetValid = offsetValidator.validate(String.valueOf(offset));

        //if any parameter is invalid
        if (!isSortTypeValid || !isSortOrderValid || !isLimitValid || !isOffsetValid) {
        	if(!isSortTypeValid) {
        		throw new CustomValidationException("Invalid Sort Type input parameter");
        	}else if(!isSortOrderValid) {
        		throw new CustomValidationException("Invalid Sort Order input parameter");
        	}else if(!isLimitValid) {
        		throw new CustomValidationException("Invalid Limit input parameter");
        	}else {
        		throw new CustomValidationException("Invalid Offset input parameter");
        	}     	
             
        }
        
        // else Proceed with getting users based on validated parameters
        FilteredAndTotalUsers userResult = userService.getUsersWithSorting(sortType, sortOrder,limit,offset);
        
        List<User> filteredUsers= userResult.getFilteredUsers();
        int totalDataCount = userResult .getTotalDataCount();
        
        
      
        // Calculate hasNextPage and hasPreviousPage for pageInfo
        boolean hasNextPage = (offset+limit)< totalDataCount;
        boolean hasPreviousPage = offset > 0;

        // Create a custom page object
        CustomPage<User> customPage = new CustomPage<>();
        
        customPage.setContent(filteredUsers);

        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("hasNextPage", hasNextPage);
        pageInfo.put("hasPreviousPage", hasPreviousPage);
        pageInfo.put("totalData",totalDataCount );

        customPage.setpageInfo(pageInfo);
        
        // Return the response entity with the list of users
        return ResponseEntity.ok(customPage);
        
       
    }catch (CustomValidationException e) {
        // Catch and handle custom validation exceptions
    	throw new CustomValidationException(e.getMessage());
    } catch (Exception e) {
        // Catch any other unexpected exceptions and handle them
        CustomError error = new CustomError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }}

}

