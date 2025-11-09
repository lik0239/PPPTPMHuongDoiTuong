package com.example.courseReg.model;

import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "khung_gio")
public class KhungGio {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 1..7 (Thứ 2..Chủ nhật nếu bạn quy ước như vậy)
  @Column(nullable = false)
  private Integer thu;

  @Column(name = "gio_bat_dau", nullable = false)
  private LocalTime gioBatDau;

  @Column(name = "gio_ket_thuc", nullable = false)
  private LocalTime gioKetThuc;

  // ----- tiện cho hiển thị trên dropdown -----
  @Transient
  public String getLabel() {
    return "Th" + thu + " " + gioBatDau + "-" + gioKetThuc;
  }

  // ----- getters/setters -----
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Integer getThu() { return thu; }
  public void setThu(Integer thu) { this.thu = thu; }

  public LocalTime getGioBatDau() { return gioBatDau; }
  public void setGioBatDau(LocalTime gioBatDau) { this.gioBatDau = gioBatDau; }

  public LocalTime getGioKetThuc() { return gioKetThuc; }
  public void setGioKetThuc(LocalTime gioKetThuc) { this.gioKetThuc = gioKetThuc; }

  // equals/hashCode theo id để dùng tốt trong Set<>
  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof KhungGio)) return false;
    KhungGio that = (KhungGio) o;
    return Objects.equals(id, that.id);
  }
  @Override public int hashCode() { return Objects.hash(id); }
}
