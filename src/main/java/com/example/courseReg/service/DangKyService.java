package com.example.courseReg.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.courseReg.model.DangKy;
import com.example.courseReg.model.LopHocPhan;
import com.example.courseReg.repo.CourseClassRepository;
import com.example.courseReg.repo.DangKyRepository;

@Service
public class DangKyService {

  private final CourseClassRepository courseClassRepo;
  private final DangKyRepository dangKyRepo;

  // KHÔNG dùng Lombok: tự viết constructor
  public DangKyService(CourseClassRepository courseClassRepo,
                       DangKyRepository dangKyRepo) {
    this.courseClassRepo = courseClassRepo;
    this.dangKyRepo = dangKyRepo;
  }

  @Transactional
  public void register(Long classId, Long sinhVienId) {
    // 1) Khóa bản ghi lớp để tránh race condition
    LopHocPhan lop = courseClassRepo.lockById(classId);
    if (lop == null) throw new IllegalArgumentException("Không tìm thấy lớp");

    // 2) Lấy sĩ số tối đa
    Integer capacity = courseClassRepo.findCapacityById(classId);

    // 3) Đếm số đã đăng ký hợp lệ
    long enrolled = dangKyRepo.countByLopHocPhanIdAndTrangThai(classId, "da_dang_ky");
    if (capacity != null && enrolled >= capacity) {
      throw new IllegalStateException("Lớp đã đủ chỗ");
    }

    // 4) Nếu SV đã đăng ký rồi thì bỏ qua
    var existed = dangKyRepo.findByLopHocPhanIdAndSinhVienIdAndTrangThai(
        classId, sinhVienId, "da_dang_ky");
    if (existed.isPresent()) return;

    // 5) Tạo bản ghi đăng ký
    DangKy dk = new DangKy();
    dk.setLopHocPhanId(classId);
    dk.setSinhVienId(sinhVienId);
    dk.setTrangThai("da_dang_ky");
    dangKyRepo.save(dk);
  }

  @Transactional
  public void unregister(Long classId, Long sinhVienId) {
    dangKyRepo.findByLopHocPhanIdAndSinhVienIdAndTrangThai(
        classId, sinhVienId, "da_dang_ky").ifPresent(dangKyRepo::delete);
  }
}
