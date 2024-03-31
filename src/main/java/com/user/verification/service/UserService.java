package com.user.verification.service;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.user.verification.CustomException.CustomValidationException;
import com.user.verification.model.FilteredAndTotalUsers;
import com.user.verification.model.Results;
import com.user.verification.model.User;
import com.user.verification.model.UserResult;


@Service
public class UserService {

    private final UserVerifier userVerifier;
    private final UserRepositoryService userRepositoryService;
    private final WebClient randomUserWebClient;

    // Inject WebClient instances with specific configurations
    public UserService(
            UserVerifier userVerifier,
            UserRepositoryService userRepositoryService,  
           
            @Qualifier("randomUserWebClient") WebClient randomUserWebClient
    ) {
        this.userVerifier = userVerifier;      
        this.randomUserWebClient = randomUserWebClient;
        this.userRepositoryService =userRepositoryService;
    }

    public List<User> verifyUserDetails(int size) {
    	
        return IntStream.range(0, size)
                .mapToObj(i -> randomUserWebClient.get()
                        .uri("/")
                        .retrieve()
                        .bodyToMono(UserResult.class)
                        .block())
                .filter(userResult -> userResult != null &&
                        userResult.getResults() != null &&
                        !userResult.getResults().isEmpty())
                .map(userResult -> {
                    Results userFromApi = userResult.getResults().get(0);
                    User verifiedUser = userVerifier.verifyUser(userFromApi);
                    return (verifiedUser != null) ? verifiedUser : null;
                })
                .filter(verifiedUser -> verifiedUser != null)
                .collect(Collectors.toList());
    	
    }
    
    public FilteredAndTotalUsers getUsersWithSorting(String sortType, String sortOrder ,int limit,int offset) {
      
    	try {

        // Retrieve users based on sortType and sortOrder
    	 List<User> sortedUsers;
    	    if (sortType.equalsIgnoreCase("Name")) {
    	        if (sortOrder.equalsIgnoreCase("odd")) {
    	            sortedUsers = userRepositoryService.getUsersWithOddNameCharacterCount();
    	        } else if (sortOrder.equalsIgnoreCase("even")) {
    	            sortedUsers = userRepositoryService.getUsersWithEvenNameCharacterCount();
    	        } else {
    	            throw new CustomValidationException("Invalid Sort Order input .Choose:Odd/Even");
    	        }
    	    } else if (sortType.equalsIgnoreCase("Age")) {
    	        if (sortOrder.equalsIgnoreCase("odd")) {
    	            sortedUsers = userRepositoryService.getUsersWithOddAge();
    	        } else if (sortOrder.equalsIgnoreCase("even")) {
    	            sortedUsers = userRepositoryService.getUsersWithEvenAge();
    	        } else {
    	            throw new CustomValidationException("Invalid Sort Order input .Choose:Odd/Even");
    	        }
    	    } else {
    	        throw new CustomValidationException("Invalid Sort Type input parameter. Choose:Age/Name");
    	    }
    	    
    	    
    	    
    	    //if empty! sorted list throws error
    	    if (sortedUsers.size() == 0) {
    	        throw new CustomValidationException("No Sorted User Data Found ");
    	    }
    	    
    	    //else apply limit and offset
    	    int totalDataCount = sortedUsers.size();

    	    if(offset>=totalDataCount) {
    	    	 throw new CustomValidationException("offset value is too high");
    	    }
            int startIndex = Math.min(offset , totalDataCount);
            int endIndex = Math.min(startIndex + limit, totalDataCount);
            List<User> filteredUsers = sortedUsers.subList(startIndex, endIndex);
            
            
    	   
    	    //else return filteredUsers and totalDataCount
    	    return new FilteredAndTotalUsers(filteredUsers, totalDataCount);

    	    
    	}catch (Exception e) {
            // Handle exceptions or rethrow with a custom exception type
            throw new CustomValidationException("Error occurred: " + e.getMessage());
        }}
}
