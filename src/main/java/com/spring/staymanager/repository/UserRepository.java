package com.spring.staymanager.repository;

import com.spring.staymanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select case when count(u) > 0 then true else false end from User u where u.email = ?1")
    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("select case when count(u) > 0 then true else false end from User u where u.phoneNumber = ?1")
    Boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber")
    Optional<User> findByPhoneNumber(String phoneNumber);

}
