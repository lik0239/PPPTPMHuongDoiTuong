package com.example.courseReg.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.courseReg.repo.SinhVienRepository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Service
public class AuthService {
  private final AccountRepository repo;
  private final PasswordEncoder encoder;
  private final SinhVienRepository svRepo;

  public AuthService(AccountRepository repo, PasswordEncoder encoder, SinhVienRepository svRepo) {
    this.repo = repo; this.encoder = encoder; this.svRepo = svRepo;
  }

  public static class RegisterForm {
    @NotBlank @Size(min=3, max=20)
    private String mssv;                // bắt buộc

    @NotBlank @Size(min=3, max=100)
    private String username;

    @NotBlank @Size(min=6, max=100)
    private String password;

    // không cho người dùng chỉnh, luôn là STUDENT
    private final String role = "STUDENT";

    private Long sinhVienId;            // sẽ set sau khi tra MSSV

    public RegisterForm() {}

    public String getMssv() { return mssv; }
    public void setMssv(String mssv) { this.mssv = mssv; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }

    public Long getSinhVienId() { return sinhVienId; }
    public void setSinhVienId(Long sinhVienId) { this.sinhVienId = sinhVienId; }
  }

  public void register(RegisterForm f){
    String u = f.getUsername().trim();

    // kiểm tra username trùng
    if (repo.existsByUsername(u)) {
      throw new IllegalArgumentException("Username đã tồn tại");
    }

    // tra MSSV -> sinh viên
    var sv = svRepo.findByMaSoSinhVien(f.getMssv().trim())
        .orElseThrow(() -> new IllegalArgumentException("MSSV không tồn tại trong hệ thống"));

    // (tuỳ chọn) chặn một MSSV tạo nhiều tài khoản
    if (repo.existsBySinhVienId(sv.getId())) {
      throw new IllegalArgumentException("MSSV này đã được liên kết với tài khoản khác");
    }

    Account acc = new Account();
    acc.setUsername(u);
    acc.setPassword(encoder.encode(f.getPassword()));
    acc.setRole("STUDENT");            // cố định
    acc.setSinhVienId(sv.getId());     // gán id theo MSSV
    repo.save(acc);
  }
}
