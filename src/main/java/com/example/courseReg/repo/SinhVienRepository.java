package com.example.courseReg.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.courseReg.model.SinhVien; 

public interface SinhVienRepository extends JpaRepository<SinhVien, Long> {
  Optional<SinhVien> findByMaSoSinhVien(String maSoSinhVien);
  boolean existsByMaSoSinhVien(String maSoSinhVien);
}
