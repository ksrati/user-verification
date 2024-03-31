package com.user.verification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.web.reactive.function.client.WebClient;

import com.user.verification.CustomException.CustomValidationException;
import com.user.verification.model.FilteredAndTotalUsers;
import com.user.verification.model.User;
import com.user.verification.service.UserRepositoryService;
import com.user.verification.service.UserVerifier;
import com.user.verification.service.UserVerifierImpl;
import com.user.verification.service.UserService;

public class UserServiceTest {

    @Mock
    private UserVerifier userVerifier;

    @Mock
    private UserRepositoryService userRepositoryService;
    
    @Mock
    private WebClient genderizeWebClient;

    @Mock
    private WebClient nationalizeWebClient;


    @Mock
    private WebClient randomUserWebClient;

    @InjectMocks
    private UserService userService;
    
    @InjectMocks
    private UserVerifierImpl userVerifierImpl;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
   private List<User> sampleUsers(){
	   List<User> userList = new ArrayList<>();
       // Populate userList with some test users
       User user1 = new User();
       user1.setId(1L); 
       user1.setName("John Doe");
       user1.setGender("Male");
       user1.setDob("1990-01-01"); 
       user1.setAge(32); 
       user1.setNat("US");
       user1.setVerificationStatus("VERIFIED");
       user1.setDateCreated(LocalDateTime.now());
       user1.setDateModified(LocalDateTime.now());
       userList.add  (user1);
       User user2 = new User();
       user2.setId(2L);
       user2.setName("Maria Swift");
       user2.setGender("female");
       user2.setDob("1999-02-01"); 
       user2.setAge(24); 
       user2.setNat("US");
       user2.setVerificationStatus("TO_BE_VERIFIED");
       user2.setDateCreated(LocalDateTime.now());
       user2.setDateModified(LocalDateTime.now());
       userList.add  (user2);
       User user3 = new User();
       user3.setId(3L); 
       user3.setName("Harry Styles");
       user3.setGender("Male");
       user3.setDob("1992-11-21"); 
       user3.setAge(31); 
       user3.setNat("US");
       user3.setVerificationStatus("VERIFIED");
       user3.setDateCreated(LocalDateTime.now());
       user3.setDateModified(LocalDateTime.now());
       userList.add  (user3);
       
       return userList;
    	
    }
  

    @Test
    public void testGetUsersWithSorting_ValidParameters() {
       
    	 List<User> userList = sampleUsers();
        // Mocking userRepositoryService to return the list of users
        when(userRepositoryService.getUsersWithOddNameCharacterCount()).thenReturn(userList);

        // Test parameters
        String sortType = "Name";
        String sortOrder = "odd";
        int limit = 5;
        int offset = 0;

       
        // Perform the test
        FilteredAndTotalUsers result = userService.getUsersWithSorting(sortType, sortOrder, limit, offset);

        Assertions.assertEquals(userList.subList(offset, Math.min(offset + limit, userList.size())), result.getFilteredUsers());
        Assertions.assertEquals(userList.size(), result.getTotalDataCount());
        verify(userRepositoryService).getUsersWithOddNameCharacterCount();
    }

    @Test
    public void testGetUsersWithSorting_InvalidSortType() {
        // Test parameters
        String sortType = "InvalidSort";
        String sortOrder = "odd";
        int limit = 10;
        int offset = 0;

        // Perform the test and assert exception
        Assertions.assertThrows(
            CustomValidationException.class,
            () -> userService.getUsersWithSorting(sortType, sortOrder, limit, offset)
        );
    }

    @Test
    public void testGetUsersWithSorting_EmptyPage() {
        // Mocking userRepositoryService behavior to return an empty list
        when(userRepositoryService.getUsersWithOddNameCharacterCount()).thenReturn(Collections.emptyList());

        // Test parameters
        String sortType = "Name";
        String sortOrder = "odd";
        int limit = 5;
        int offset = 0;

        // Perform the test and assert exception
        Assertions.assertThrows(
            CustomValidationException.class,
            () -> userService.getUsersWithSorting(sortType, sortOrder, limit, offset)
        );

        // Verify that getUsersWithOddNameCharacterCount() is invoked
        verify(userRepositoryService).getUsersWithOddNameCharacterCount();
    }
    

    @Test
    public void testGetUsersWithSorting_HighOffsetParameter() {
    	
    	
        // Stub getUsersCount() to return a non-zero value to avoid an exception due to an empty database
        when(userRepositoryService.getUsersCount()).thenReturn(10L);
   	    List<User> userList = sampleUsers();

        
        // Mock the sorted list to have a certain size
        when(userRepositoryService.getUsersWithOddNameCharacterCount()).thenReturn(userList);

        // Test parameters with an invalid offset (higher than sorted list size)
        String sortType = "Name";
        String sortOrder = "odd";
        int limit = 5;
        int offset = 15; // Set an offset higher than the sorted list size

        // Execute & Assert
        CustomValidationException exception = assertThrows(
            CustomValidationException.class,
            () -> userService.getUsersWithSorting(sortType, sortOrder, limit, offset)
        );

        assertEquals("Error occurred: offset value is too high", exception.getMessage());
    }


    @Test
    public void testGetUsersWithSorting_ValidParametersWithOffset() {
        // Mocking userRepositoryService behavior
        List<User> userList = sampleUsers();
       

        when(userRepositoryService.getUsersWithOddNameCharacterCount()).thenReturn(userList);

        // Test parameters
        String sortType = "Name";
        String sortOrder = "odd";
        int limit = 5;
        int offset = 1;

        // Perform the test
        FilteredAndTotalUsers result = userService.getUsersWithSorting(sortType, sortOrder, limit, offset);

        // Assertions
        int expectedTotalCount = userList.size();
        int expectedFilteredCount = Math.max(expectedTotalCount - offset, 0);
        Assertions.assertEquals(userList.subList(offset, Math.min(offset + limit, userList.size())), result.getFilteredUsers());
        Assertions.assertEquals(expectedFilteredCount, result.getFilteredUsers().size());
        Assertions.assertEquals(expectedTotalCount, result.getTotalDataCount());
        verify(userRepositoryService).getUsersWithOddNameCharacterCount();
    }
    
   

    
}
