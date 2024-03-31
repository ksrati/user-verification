package com.user.verification;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.user.verification.CustomException.CustomValidationException;
import com.user.verification.controller.UserController;
import com.user.verification.model.User;
import com.user.verification.service.UserRepositoryService;
import com.user.verification.service.UserService;
import com.user.verification.validators.LengthValidator;
import com.user.verification.validators.Validator;
import com.user.verification.validators.ValidatorFactory;

public class UserControllerTests {


    @Mock
    private UserService userService;
    
    @Mock
    private UserRepositoryService userRepositoryService;
    
    @Mock
    private Validator validator;


    @Mock
    private ValidatorFactory validatorFactory;

    @InjectMocks
    private UserController userController;
    
    
	
	 @Autowired
	    private TestRestTemplate restTemplate;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        restTemplate = new TestRestTemplate();
    }
    
    

    @Test
    public void testGetUsers_EmptyDatabase() {
        // Stub getUsersCount() to return 0
        when(userRepositoryService.getUsersCount()).thenReturn(0L);

        // Execute & Assert
        CustomValidationException exception = assertThrows(
            CustomValidationException.class,
            () -> userController.getUsers("name", "odd", 5, 0)
        );

        assertEquals("No User Data Exists in the database ", exception.getMessage());
    }

    


    @Test
    void testWrongEndpointReturnsPageNotFound() {
        RestTemplate restTemplate = new RestTemplate();

        String wrongEndpoint = "http://localhost:8054" + "/wrongEndpoint";

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restTemplate.getForEntity(wrongEndpoint, String.class);
        });

        HttpStatus statusCode = (HttpStatus) exception.getStatusCode();
        assert statusCode == HttpStatus.NOT_FOUND;
    }
    
    @Test
    public void testMissingParametersThrowsError() {
        String baseUrl = "http://localhost:8054" + "/listUsers";

        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(baseUrl, Object.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody()); 

//         System.out.println(responseEntity.getBody());
    }
    
    @Test
    public void testVerifyUser_InvalidSizeParameter() {
        // Mock the behavior of the validator for "size"
        when(validatorFactory.getValidator("size")).thenReturn(new LengthValidator());

        // Test parameters
        int size = 10; 

       
        Assertions.assertThrows(
            CustomValidationException.class,
            () -> userController.verifyUser(size)
        );
    }

    @Test
    public void testVerifyUser_ValidSizeParameter() {
        // Mock the behavior of the validator for "size"
        when(validatorFactory.getValidator("size")).thenReturn(new LengthValidator());

        // Test parameters
        int size = 3; 

        List<User> userList = new ArrayList<>(); // Add necessary user data for the test
        when(userService.verifyUserDetails(size)).thenReturn(userList);

        // Perform the test
        ResponseEntity<List<User>> result = userController.verifyUser(size);

        // Assertions
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(userList, result.getBody());
    }
    
    

}
