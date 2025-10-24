package com.example.courseReg.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.courseReg.repo.CourseClassRepository;
import com.example.courseReg.repo.CourseClassView;

@Controller
public class PageController {

    private final CourseClassRepository courseClassRepository;

    public PageController(CourseClassRepository courseClassRepository) {
        this.courseClassRepository = courseClassRepository;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/courses")
    public String courses(
        @RequestParam(value = "q", defaultValue = "") String q,
        @RequestParam(value = "open", defaultValue = "false") boolean open,
        Model model) {

    List<CourseClassView> items =
        courseClassRepository.searchByKeyword(q.trim(), open);
    model.addAttribute("q", q);
    model.addAttribute("open", open);
    model.addAttribute("items", items);
    model.addAttribute("count", items.size());
    return "courses";
    }
}
