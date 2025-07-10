package org.example.shopmeat2.test;

import org.example.shopmeat2.dal.UserRepository;
import org.example.shopmeat2.modals.Users;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

@SpringBootApplication(scanBasePackages = "org.example.shopmeat2")
public class LoginTestApp {

    public static void main(String[] args) {
        // Khởi động Spring để inject Repository
        ApplicationContext context = SpringApplication.run(LoginTestApp.class, args);

        UserRepository userRepo = context.getBean(UserRepository.class);

        String inputUsername = "thanhlong";
        String inputPassword = "12345";

        Optional<Users> optionalUser = userRepo.findByUsername(inputUsername);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            System.out.println("✅ Tìm thấy user: " + user.getUsername());
            System.out.println(">> Password trong DB: " + user.getPasswordHash());

            if (user.getPasswordHash().equals(inputPassword)) {
                System.out.println("✅ Mật khẩu đúng. Role: " + user.getRole());
            } else {
                System.out.println("❌ Mật khẩu sai.");
            }
        } else {
            System.out.println("❌ Không tìm thấy người dùng: " + inputUsername);
        }

        // Kết thúc ứng dụng sau test
        SpringApplication.exit(context);
    }
}
