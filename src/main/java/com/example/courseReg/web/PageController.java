package com.example.courseReg.web;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.courseReg.repo.CourseClassRepository;
import com.example.courseReg.repo.DangKyRepository;
import com.example.courseReg.repo.SinhVienRepository;
import com.example.courseReg.service.DangKyService;

@Controller
public class PageController {

    private static final Logger log = LoggerFactory.getLogger(PageController.class);

    private final CourseClassRepository courseClassRepository;
    private final DangKyRepository dangKyRepository;
    private final SinhVienRepository sinhVienRepository;
    private final DangKyService dangKyService;

    public PageController(CourseClassRepository courseClassRepository,
                          DangKyRepository dangKyRepository,
                          SinhVienRepository sinhVienRepository,
                          DangKyService dangKyService) {
        this.courseClassRepository = courseClassRepository;
        this.dangKyRepository = dangKyRepository;
        this.sinhVienRepository = sinhVienRepository;
        this.dangKyService = dangKyService;
    }

    // --- tiện ích: suy ra id sinh viên từ tài khoản đăng nhập
    private Long getSinhVienId(Principal principal) {
        if (principal == null) throw new IllegalStateException("Bạn chưa đăng nhập.");

        String uname = principal.getName(); // ví dụ: "han"
        // 1) thử coi username chính là mã SV (SV001...)
        return sinhVienRepository.findIdByMaSoSinhVien(uname)
            // 2) nếu không phải, tra theo liên kết tai_khoan.sinh_vien_id
            .or(() -> sinhVienRepository.findIdByUsername(uname))
            .orElseThrow(() ->
                new IllegalStateException("Không tìm thấy sinh viên cho tài khoản: " + uname));
    }

    @GetMapping("/")
    public String home() { return "home"; }

    @GetMapping("/courses")
    public String courses(@RequestParam(value="q", defaultValue="") String q,
                          @RequestParam(value="open", defaultValue="false") boolean open,
                          Principal principal,
                          Model model) {

        var items = courseClassRepository.searchByKeyword(q.trim(), open);
        model.addAttribute("q", q);
        model.addAttribute("open", open);
        model.addAttribute("items", items);
        model.addAttribute("count", items.size());

        Set<Long> registeredIds = Collections.emptySet();
        if (principal != null) {
            try {
                Long svId = getSinhVienId(principal);
                registeredIds = dangKyRepository.findActiveClassIdsBySinhVien(svId);
                if (registeredIds == null) registeredIds = Collections.emptySet();
            } catch (Exception ignore) {
                // cho phép render trang ngay cả khi chưa map được sinh viên
            }
        }
        model.addAttribute("registeredIds", registeredIds);

        log.debug("Load /courses: user={}, q='{}', open={}, items={}, registeredIds={}",
                principal != null ? principal.getName() : null, q, open, items.size(), registeredIds.size());

        return "courses";
    }

    @PostMapping("/courses/{classId}/register")
    public String register(@PathVariable("classId") Long classId,
                           Principal principal,
                           RedirectAttributes ra) {
        try {
            if (principal == null) {
                ra.addFlashAttribute("err", "Bạn cần đăng nhập để đăng ký.");
                return "redirect:/login";
            }

            log.debug("Register request: user={}, classId={}", principal.getName(), classId);

            Long svId = getSinhVienId(principal);
            log.debug("Resolved svId={} for user={}", svId, principal.getName());

            dangKyService.register(classId, svId);
            ra.addFlashAttribute("ok", "Đăng ký thành công!");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("err", e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi đăng ký lớp {}: {}", classId, e.getMessage(), e);
            ra.addFlashAttribute("err", "Đăng ký thất bại.");
        }
        return "redirect:/courses";
    }

    @PostMapping("/courses/{classId}/unregister")
    public String unregister(@PathVariable("classId") Long classId,
                             Principal principal,
                             RedirectAttributes ra) {
        try {
            if (principal == null) {
                ra.addFlashAttribute("err", "Bạn cần đăng nhập để hủy đăng ký.");
                return "redirect:/login";
            }

            log.debug("Unregister request: user={}, classId={}", principal.getName(), classId);

            Long svId = getSinhVienId(principal);
            log.debug("Resolved svId={} for user={}", svId, principal.getName());

            dangKyService.unregister(classId, svId);
            ra.addFlashAttribute("ok", "Đã hủy đăng ký.");
        } catch (Exception e) {
            log.error("Lỗi hủy đăng ký lớp {}: {}", classId, e.getMessage(), e);
            ra.addFlashAttribute("err", "Hủy đăng ký thất bại.");
        }
        return "redirect:/courses";
    }
}
