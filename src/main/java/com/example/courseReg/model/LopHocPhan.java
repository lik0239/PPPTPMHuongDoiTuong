package com.example.courseReg.model;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "lop_hoc_phan")
public class LopHocPhan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "hoc_phan_id", nullable = false)
  private Long hocPhanId;

  @Column(name = "hoc_ky_id", nullable = false)
  private Long hocKyId;

  @Column(name = "nhom_lop", length = 10, nullable = false)
  private String nhomLop;

  @Column(name = "giang_vien_id")
  private Long giangVienId;

  @Column(name = "si_so_toi_da", nullable = false)
  private Integer siSoToiDa;

  @Column(name = "ghi_chu")
  private String ghiChu;

  // ---- Many-to-many với KHUNG_GIO ----
  @ManyToMany
  @JoinTable(
      name = "lop_hoc_phan_khung_gio",
      joinColumns = @JoinColumn(name = "lop_hoc_phan_id"),
      inverseJoinColumns = @JoinColumn(name = "khung_gio_id")
  )
  private Set<KhungGio> khungGios = new LinkedHashSet<>();

  // ===== getters/setters =====
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Long getHocPhanId() { return hocPhanId; }
  public void setHocPhanId(Long hocPhanId) { this.hocPhanId = hocPhanId; }

  public Long getHocKyId() { return hocKyId; }
  public void setHocKyId(Long hocKyId) { this.hocKyId = hocKyId; }

  public String getNhomLop() { return nhomLop; }
  public void setNhomLop(String nhomLop) { this.nhomLop = nhomLop; }

  public Long getGiangVienId() { return giangVienId; }
  public void setGiangVienId(Long giangVienId) { this.giangVienId = giangVienId; }

  public Integer getSiSoToiDa() { return siSoToiDa; }
  public void setSiSoToiDa(Integer siSoToiDa) { this.siSoToiDa = siSoToiDa; }

  public String getGhiChu() { return ghiChu; }
  public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

  public Set<KhungGio> getKhungGios() { return khungGios; }
  public void setKhungGios(Set<KhungGio> khungGios) { this.khungGios = khungGios; }
}
