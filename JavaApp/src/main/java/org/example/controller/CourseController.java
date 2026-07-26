package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.enums.CourseType;
import org.example.service.CourseService;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    private final UserService userService;

    @GetMapping("/courses")
    public String getCoursesPage(@RequestParam(required = false) Long authorId,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) CourseType courseType,
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        @RequestParam(required = false, defaultValue = "5") int pageSize,
        Model model) {
        model.addAttribute("courses", courseService.findAll(authorId, description, courseType, pageNumber, pageSize));
        model.addAttribute("courseType", CourseType.values());
        model.addAttribute("teachers", userService.findTeachers());
        return "courses/coursesPage";
    }

    @GetMapping("/courses/{id}")
    public String getCoursePage(@PathVariable Long id,
        Model model) {
        model.addAttribute("course", courseService.findById(id));

        return "courses/coursePage";
    }

    @GetMapping("/courses/{id}/learn")
    public String getLearnCoursePage(@PathVariable Long id,
        Model model) {
        model.addAttribute("course", courseService.findByIdWithMaterials(id));

        return "courses/learnPage";
    }
}
