package com.isppG8.infantem.infantem.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.nameUser = ?1")
    Optional<User> findByNameUser(String nameUser);
    
}
