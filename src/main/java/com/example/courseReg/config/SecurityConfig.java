package com.example.courseReg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.courseReg.auth.AccountRepository;

@Configuration
public class SecurityConfig {

  private final AccountRepository accountRepo;

  public SecurityConfig(AccountRepository accountRepo) {
    this.accountRepo = accountRepo;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> accountRepo.findByUsername(username)
        .map(acc -> User.withUsername(acc.getUsername())
                        .password(acc.getPassword())
                        .roles(acc.getRole())
                        .build())
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  http
    .csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(auth -> auth
      .requestMatchers(
        "/", "/home",
        "/courses", "/courses/**",
        "/login",
        "/register", "/register/**",
        "/error",
        "/styles.css", "/favicon.ico",
        "/css/**", "/js/**", "/images/**", "/webjars/**"
      ).permitAll()
      .requestMatchers("/admin/**").hasRole("ADMIN")
      .anyRequest().authenticated()
    )
    .formLogin(login -> login
      .loginPage("/login")
      .defaultSuccessUrl("/", true)
      .permitAll()
    )
    .logout(logout -> logout
      .logoutUrl("/logout")
      .logoutSuccessUrl("/")
    );
    return http.build();
  }
}
