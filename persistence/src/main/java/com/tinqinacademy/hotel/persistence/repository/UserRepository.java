package com.tinqinacademy.hotel.persistence.repository;

import com.tinqinacademy.hotel.persistence.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("""
        SELECT u FROM User u
        WHERE u.firstName = :firstName AND u.lastName = :lastName AND u.phoneNumber = :phoneNumber
    """)
    Optional<User> findUserByNameAndPhone(String firstName,String lastName, String phoneNumber);
}
