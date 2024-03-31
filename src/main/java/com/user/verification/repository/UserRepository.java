package com.user.verification.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.user.verification.model.User;


public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE MOD(LENGTH(REPLACE(u.name, ' ', '')), 2) != 0 ORDER BY LENGTH(REPLACE(u.name, ' ', '')) ASC")
    List<User> findUsersWithOddNameCharacterCount();

    @Query("SELECT u FROM User u WHERE MOD(LENGTH(REPLACE(u.name, ' ', '')), 2) = 0 ORDER BY LENGTH(REPLACE(u.name, ' ', '')) ASC")
    List<User> findUsersWithEvenNameCharacterCount();

    @Query("SELECT u FROM User u WHERE MOD(u.age, 2) != 0 ORDER BY u.age ASC")
    List<User> findUsersWithOddAge();

    @Query("SELECT u FROM User u WHERE MOD(u.age, 2) = 0 ORDER BY u.age ASC")
    List<User> findUsersWithEvenAge();
}

