package com.example.courseReg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "khoa")
public class Khoa {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ma_khoa", nullable = false, unique = true, length = 20)
  private String maKhoa;

  @Column(name = "ten_khoa", nullable = false)
  private String tenKhoa;

  // getters/setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getMaKhoa() { return maKhoa; }
  public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }
  public String getTenKhoa() { return tenKhoa; }
  public void setTenKhoa(String tenKhoa) { this.tenKhoa = tenKhoa; }
}
