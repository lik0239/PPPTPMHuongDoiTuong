package com.example.courseReg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sinh_vien")
public class SinhVien {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ma_so_sinh_vien", nullable = false, unique = true, length = 20)
  private String maSoSinhVien;

  @Column(name = "ho_ten", nullable = false)
  private String hoTen;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "khoa_id")
  private Khoa khoa;

  @Column(name = "khoa_nhap_hoc")
  private Integer khoaNhapHoc;

  @Column(name = "gioi_han_tin_chi")
  private Integer gioiHanTinChi = 20;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getMaSoSinhVien() { return maSoSinhVien; }
  public void setMaSoSinhVien(String maSoSinhVien) { this.maSoSinhVien = maSoSinhVien; }

  public String getHoTen() { return hoTen; }
  public void setHoTen(String hoTen) { this.hoTen = hoTen; }

  public Khoa getKhoa() { return khoa; }
  public void setKhoa(Khoa khoa) { this.khoa = khoa; }

  public Integer getKhoaNhapHoc() { return khoaNhapHoc; }
  public void setKhoaNhapHoc(Integer khoaNhapHoc) { this.khoaNhapHoc = khoaNhapHoc; }

  public Integer getGioiHanTinChi() { return gioiHanTinChi; }
  public void setGioiHanTinChi(Integer gioiHanTinChi) { this.gioiHanTinChi = gioiHanTinChi; }
}
