package com.example.courseReg.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lop_hoc_phan")
public class LopHocPhan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Không cần thêm field khác nếu bạn chỉ dùng native query + projection
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
