package com.user.verification.service;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.user.verification.CustomException.CustomValidationException;
import com.user.verification.model.User;
import com.user.verification.repository.UserRepository;

@Service
public class UserRepositoryService {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        userRepository.save(user);
        return user;
    }
    public List<User> getUsersWithOddNameCharacterCount() {
        try {
            return userRepository.findUsersWithOddNameCharacterCount();
        } catch (Exception e) {
            throw new CustomValidationException("Error retrieving users with odd name character count");
        }
    }

    public List<User> getUsersWithEvenNameCharacterCount() {
        try {
            return userRepository.findUsersWithEvenNameCharacterCount();
        } catch (Exception e) {
            throw new CustomValidationException("Error retrieving users with even name character count");
        }
    }

    public List<User> getUsersWithOddAge() {
        try {
            return userRepository.findUsersWithOddAge();
        } catch (Exception e) {
            throw new CustomValidationException("Error retrieving users with odd age");
        }
    }

    public List<User> getUsersWithEvenAge() {
        try {
            return userRepository.findUsersWithEvenAge();
        } catch (Exception e) {
            throw new CustomValidationException("Error retrieving users with even age");
        }
    }

    public long getUsersCount() {
       
            return userRepository.count();   
    }



   
}
