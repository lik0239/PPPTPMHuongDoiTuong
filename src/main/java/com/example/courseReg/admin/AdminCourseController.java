// src/main/java/com/example/courseReg/admin/AdminCourseController.java
package com.example.courseReg.admin;

import java.util.LinkedHashSet;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.courseReg.model.LopHocPhan;
import com.example.courseReg.repo.CourseClassRepository;
import com.example.courseReg.repo.GiangVienRepository;
import com.example.courseReg.repo.HocKyRepository;
import com.example.courseReg.repo.HocPhanRepository;
import com.example.courseReg.repo.KhungGioRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/courses")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminCourseController {

    private final CourseClassRepository courseRepo;   // CRUD cho LopHocPhan
    private final HocPhanRepository hocPhanRepo;      // dropdown
    private final HocKyRepository hocKyRepo;          // dropdown
    private final GiangVienRepository giangVienRepo;  // dropdown
    private final KhungGioRepository khungGioRepo;    // dropdown multi-select

    public AdminCourseController(
            CourseClassRepository courseRepo,
            HocPhanRepository hocPhanRepo,
            HocKyRepository hocKyRepo,
            GiangVienRepository giangVienRepo,
            KhungGioRepository khungGioRepo) {
        this.courseRepo = courseRepo;
        this.hocPhanRepo = hocPhanRepo;
        this.hocKyRepo = hocKyRepo;
        this.giangVienRepo = giangVienRepo;
        this.khungGioRepo = khungGioRepo;
    }

    /* ===== tiện ích nạp dropdown ===== */
    private void loadDropdowns(Model model) {
        model.addAttribute("hocPhans", hocPhanRepo.findAll());
        model.addAttribute("hocKys", hocKyRepo.findAll());
        model.addAttribute("giangViens", giangVienRepo.findAll());
        model.addAttribute("khungGios", khungGioRepo.findAll());
    }

    /* ===== CREATE ===== */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("title", "Thêm học phần");
        model.addAttribute("form", new LopHocPhanForm()); // <-- dùng DTO
        loadDropdowns(model);
        return "admin/course_create";
    }

    @PostMapping("/create")
    public String doCreate(@Valid @ModelAttribute("form") LopHocPhanForm form,
                           BindingResult br,
                           RedirectAttributes ra,
                           Model model) {
        if (br.hasErrors()) {
            loadDropdowns(model);
            return "admin/course_create";
        }

        LopHocPhan e = new LopHocPhan();
        copyToEntity(form, e);
        e.setId(null); // chắc chắn là thêm mới
        courseRepo.save(e);

        ra.addFlashAttribute("ok", "Đã thêm học phần!");
        return "redirect:/courses";
    }

    /* ===== EDIT ===== */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        LopHocPhan e = courseRepo.findByIdWithKhungGios(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        LopHocPhanForm form = new LopHocPhanForm();
        copyFromEntity(e, form);

        model.addAttribute("title", "Sửa học phần");
        model.addAttribute("form", form);
        loadDropdowns(model);
        return "admin/course_edit";
    }

    @PostMapping("/{id}/edit")
    public String doEdit(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("form") LopHocPhanForm form,
                         BindingResult br,
                         RedirectAttributes ra,
                         Model model) {
        if (br.hasErrors()) {
            loadDropdowns(model);
            return "admin/course_edit";
        }
        LopHocPhan e = courseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        copyToEntity(form, e);
        e.setId(id);
        courseRepo.save(e);

        ra.addFlashAttribute("ok", "Đã cập nhật học phần!");
        return "redirect:/courses";
    }

    /* ===== DELETE ===== */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        if (!courseRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        courseRepo.deleteById(id);
        ra.addFlashAttribute("ok", "Đã xóa học phần!");
        return "redirect:/courses";
    }

    /* ====== mapping giữa DTO và entity ====== */

    private void copyFromEntity(LopHocPhan e, LopHocPhanForm f) {
        f.setId(e.getId());
        f.setHocPhanId(e.getHocPhanId());
        f.setHocKyId(e.getHocKyId());
        f.setNhomLop(e.getNhomLop());
        f.setGiangVienId(e.getGiangVienId());
        f.setSiSoToiDa(e.getSiSoToiDa());
        f.setGhiChu(e.getGhiChu());
        f.setKhungGioIds(e.getKhungGios().stream().map(k -> k.getId()).toList());
    }

    private void copyToEntity(LopHocPhanForm f, LopHocPhan e) {
        e.setHocPhanId(f.getHocPhanId());
        e.setHocKyId(f.getHocKyId());
        e.setNhomLop(f.getNhomLop());
        e.setGiangVienId(f.getGiangVienId());
        e.setSiSoToiDa(f.getSiSoToiDa());
        e.setGhiChu(f.getGhiChu());

        var khungGioSet = new LinkedHashSet<>(khungGioRepo.findAllById(f.getKhungGioIds()));
        e.setKhungGios(khungGioSet);
    }
}
