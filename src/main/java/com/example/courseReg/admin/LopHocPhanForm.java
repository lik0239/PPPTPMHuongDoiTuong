// src/main/java/com/example/courseReg/admin/LopHocPhanForm.java
package com.example.courseReg.admin;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LopHocPhanForm {
  private Long id;

  @NotNull
  private Long hocPhanId;

  @NotNull
  private Long hocKyId;

  @NotBlank
  private String nhomLop;

  // có thể null (chưa gán GV)
  private Long giangVienId;

  @NotNull
  @Min(1)
  private Integer siSoToiDa;

  private String ghiChu;

  // chọn nhiều khung giờ -> nhận ID
  private List<Long> khungGioIds = new ArrayList<>();

  public LopHocPhanForm() {}

  // --- getters/setters ---
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

  public List<Long> getKhungGioIds() { return khungGioIds; }
  public void setKhungGioIds(List<Long> khungGioIds) { this.khungGioIds = khungGioIds; }
}
