package com.example.swed.repo;

import com.example.swed.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query("update User u set u.firstName = ?1, u.lastName = ?2, u.birthdate = ?3, u.address = ?4 where u.email = ?5")
    void updateUserByEmail(String firstname, String lastname, Date birthdate, String address, String email);

    @Modifying
    @Transactional
    @Query("update User u set u.firstName = ?1, u.lastName = ?2, u.birthdate = ?3, u.address = ?4, u.password = ?5 where u.email = ?6")
    void updateUserByEmail(String firstname, String lastname, Date birthdate, String address, String password, String email);


    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<User> findAll();


}