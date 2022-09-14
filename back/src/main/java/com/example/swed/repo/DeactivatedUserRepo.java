package com.example.swed.repo;

import com.example.swed.models.DeactivatedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeactivatedUserRepo extends JpaRepository<DeactivatedUser, Long> {
    List<DeactivatedUser> findAll();
}
