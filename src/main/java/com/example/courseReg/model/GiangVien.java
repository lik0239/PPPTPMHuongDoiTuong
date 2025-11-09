package com.example.courseReg.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity @Table(name="giang_vien")
public class GiangVien {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(name="ho_ten", nullable=false) private String hoTen;
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getHoTen(){return hoTen;} public void setHoTen(String v){this.hoTen=v;}
}