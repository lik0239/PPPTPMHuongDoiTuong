package com.example.courseReg.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "dang_ky",
  uniqueConstraints = @UniqueConstraint(columnNames = {"sinh_vien_id","lop_hoc_phan_id"}))
public class DangKy {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="sinh_vien_id", nullable=false)
  private Long sinhVienId;

  @Column(name="lop_hoc_phan_id", nullable=false)
  private Long lopHocPhanId;

  @Column(name="trang_thai", nullable=false, length=12)
  private String trangThai = "da_dang_ky";

  @Column(name="thoi_diem_dang_ky")
  private OffsetDateTime thoiDiemDangKy = OffsetDateTime.now();

  // getters/setters
  public Long getId() { return id; }
  public Long getSinhVienId() { return sinhVienId; }
  public void setSinhVienId(Long id) { this.sinhVienId = id; }
  public Long getLopHocPhanId() { return lopHocPhanId; }
  public void setLopHocPhanId(Long id) { this.lopHocPhanId = id; }
  public String getTrangThai() { return trangThai; }
  public void setTrangThai(String t) { this.trangThai = t; }
  public OffsetDateTime getThoiDiemDangKy() { return thoiDiemDangKy; }
  public void setThoiDiemDangKy(OffsetDateTime t) { this.thoiDiemDangKy = t; }
}
