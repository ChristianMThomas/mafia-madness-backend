package com.Mind_Forge.MafiaMadness.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Mind_Forge.MafiaMadness.model.User;
import com.Mind_Forge.MafiaMadness.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }
}
