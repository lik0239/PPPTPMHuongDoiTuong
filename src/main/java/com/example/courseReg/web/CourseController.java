package com.example.courseReg.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.courseReg.repo.CourseClassRepository;
import com.example.courseReg.repo.CourseClassView;

@Controller
public class CourseController {

    private final CourseClassRepository repo;

    public CourseController(CourseClassRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/api/courses")
    @ResponseBody // nếu bạn trả JSON
    public List<CourseClassView> list(@RequestParam(required=false, defaultValue="") String q,
                                  @RequestParam(required=false, defaultValue="false") boolean open) {
        return repo.searchByKeyword(q, open);
    }
}
