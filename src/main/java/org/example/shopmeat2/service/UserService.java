package org.example.shopmeat2.service;

import org.example.shopmeat2.dal.UserRepository;
import org.example.shopmeat2.modals.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Lấy tất cả user
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // Lấy user theo ID
    public Users getUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }


    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
