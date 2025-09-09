package org.example.shopmeat2.security;

import org.example.shopmeat2.dal.UserRepository;
import org.example.shopmeat2.modals.Users;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(">> Đang đăng nhập với username: " + username);

        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println(">> Không tìm thấy user trong DB!");
                    return new UsernameNotFoundException("Không tìm thấy người dùng");
                });

        System.out.println(">> Tìm thấy user: " + user.getUsername() + " - " + user.getPasswordHash() + " - Role: " + user.getRole());


        String role = user.getRole().toUpperCase();

        return User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .roles(role)
                .build();
    }
}
