package com.booking.userservice.repository;


import com.booking.userservice.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String userName);

    User findByRefreshToken(String refreshToken);
    @Modifying
    @Transactional
    @Query("Update User u SET u.deleted = true WHERE u.id = :id")
    int softDeleteById(UUID id);
}
