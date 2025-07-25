package com.Mind_Forge.MafiaMadness.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Mind_Forge.MafiaMadness.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String verificationCode);

}
 