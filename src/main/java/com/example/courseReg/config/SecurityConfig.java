package com.example.courseReg.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

  // CHỈ GIỮ MỘT BEAN NÀY
  @Bean
  public UserDetailsService userDetailsService(AccountRepository repo) {
    return username -> {
      var acc = repo.findByUsername(username.trim().toLowerCase())
          .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + username));

      var authorities = List.of(new SimpleGrantedAuthority(acc.getRole())); // "ADMIN"/"STUDENT"

      return new org.springframework.security.core.userdetails.User(
          acc.getUsername(), acc.getPassword(), authorities
      );
    };
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/home", "/login", "/register", "/error",
                         "/styles.css", "/favicon.ico", "/css/**", "/js/**", "/images/**", "/webjars/**",
                         "/h2-console/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/courses").permitAll()
        .requestMatchers(HttpMethod.POST, "/courses/*/register", "/courses/*/unregister")
          .hasAuthority("STUDENT")
        .requestMatchers("/admin/**").hasAuthority("ADMIN")
        .anyRequest().authenticated()
      )
      .formLogin(login -> login
        .loginPage("/login")
        .loginProcessingUrl("/login")
        .defaultSuccessUrl("/", true)
        .permitAll()
      )
      .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/")
        .permitAll()
      );
    return http.build();
  }
}
