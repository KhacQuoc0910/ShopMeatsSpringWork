package org.example.shopmeat2.controller;

import org.example.shopmeat2.dal.UserRepository;
import org.example.shopmeat2.modals.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class TestLoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/test-login")
    @ResponseBody
    public String testLogin(@RequestParam String username, @RequestParam String password) {
        Optional<Users> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return "❌ Không tìm thấy người dùng: " + username;
        }

        Users user = optionalUser.get();

        // Nếu bạn dùng NoOp, so sánh thẳng:
        if (user.getPasswordHash().equals(password)) {
            return "✅ Đăng nhập thành công! Role: " + user.getRole();
        } else {
            return "❌ Mật khẩu không khớp!";
        }
    }
}
