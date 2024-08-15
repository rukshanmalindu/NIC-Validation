package com.example.nic.repo;

import com.example.nic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<User, Integer> {
    boolean existsByNicNumber(String nicNumber);

    // summery data
    long countByGender(String gender);

    @Query("SELECT COUNT(n) FROM User n WHERE n.gender = ?1")
    long countGender(String gender);
}



