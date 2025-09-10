package org.example.shopmeat2.config;

import org.example.shopmeat2.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Tắt CSRF cho API products (tuỳ nhu cầu)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/products/**"))

            // Phân quyền
            .authorizeHttpRequests(auth -> auth
                // Cho phép truy cập tự do các trang công khai & static
                .requestMatchers(
                    "/login",
                    "/HomePages/**",
                    "/css/**", "/js/**", "/images/**", "/webjars/**"
                ).permitAll()
                // Khu vực admin
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Giỏ hàng cho CUSTOMER
                .requestMatchers("/cart/**").hasRole("CUSTOMER")
                // Các request còn lại yêu cầu đăng nhập
                .anyRequest().authenticated()
            )

            // Form login
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/process-login")
                .defaultSuccessUrl("/HomePages/", true)
                .failureUrl("/login?error")
                .permitAll()
            )

            // Logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }

    // --- CHỌN 1 TRONG 2 TUỲ CHỌN ENCODER DƯỚI ĐÂY ---

    // 1) Khuyến nghị: mã hoá BCrypt (dùng thật)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    // 2) Tạm thời KHÔNG mã hoá (chỉ dùng để test nhanh, không dùng production)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    */
}
