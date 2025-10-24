package com.example.courseReg.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
  Optional<Account> findByUsername(String username);
  boolean existsByUsername(String username);
  boolean existsBySinhVienId(Long sinhVienId);
}
