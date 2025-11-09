package com.example.courseReg.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.courseReg.model.SinhVien;

public interface SinhVienRepository extends JpaRepository<SinhVien, Long> {

    @Query("select sv.id from SinhVien sv where sv.maSoSinhVien = :mssv")
    Optional<Long> findIdByMaSoSinhVien(@Param("mssv") String maSoSinhVien);

    Optional<SinhVien> findByMaSoSinhVien(String maSoSinhVien);

    boolean existsByMaSoSinhVien(String maSoSinhVien);

    // Lấy sv.id từ username, join theo khóa ngoại sinh_vien_id (native SQL)
    @Query(value = """
        select sv.id
        from sinh_vien sv
        join tai_khoan tk on tk.sinh_vien_id = sv.id
        where tk.username = :username
        """, nativeQuery = true)
    Optional<Long> findIdByUsername(@Param("username") String username);
}
