package com.example.courseReg.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity @Table(name="hoc_phan")
public class HocPhan {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(name="ma_hoc_phan", nullable=false) private String maHocPhan;
  @Column(name="ten_hoc_phan", nullable=false) private String tenHocPhan;
  @Column(name="so_tin_chi", nullable=false) private Integer soTinChi;
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getMaHocPhan(){return maHocPhan;} public void setMaHocPhan(String v){this.maHocPhan=v;}
  public String getTenHocPhan(){return tenHocPhan;} public void setTenHocPhan(String v){this.tenHocPhan=v;}
  public Integer getSoTinChi(){return soTinChi;} public void setSoTinChi(Integer v){this.soTinChi=v;}
}