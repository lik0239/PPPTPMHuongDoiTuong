package com.example.courseReg.repo;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.courseReg.model.DangKy;

public interface DangKyRepository extends JpaRepository<DangKy, Long> {

  // Đếm theo trạng thái
  long countByLopHocPhanIdAndTrangThai(Long lopHocPhanId, String trangThai);

  // Tìm bản ghi active của SV cho lớp
  Optional<DangKy> findByLopHocPhanIdAndSinhVienIdAndTrangThai(
      Long lopHocPhanId, Long sinhVienId, String trangThai);

  // Lấy tất cả classId SV đã đăng ký (để đổi nút trong view)
  @Query("select dk.lopHocPhanId from DangKy dk " +
         "where dk.sinhVienId = :svId and dk.trangThai = 'da_dang_ky'")
  Set<Long> findActiveClassIdsBySinhVien(@Param("svId") Long svId);

  boolean existsBySinhVienIdAndLopHocPhanId(Long sinhVienId, Long lopHocPhanId);

  Optional<DangKy> findBySinhVienIdAndLopHocPhanId(Long sinhVienId, Long lopHocPhanId);
}
