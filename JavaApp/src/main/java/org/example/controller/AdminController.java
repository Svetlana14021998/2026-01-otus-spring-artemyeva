package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.TeacherDto;
import org.example.dto.course.CourseDto;
import org.example.dto.user.UserDto;
import org.example.enums.CourseType;
import org.example.enums.UserRole;
import org.example.exception.ValidationException;
import org.example.service.StatisticService;
import org.example.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    private final StatisticService statisticService;

    private final MessageSource messageSource;

    @GetMapping("/admin/employees")
    public String getEmployeesPage(Model model) {
        List<UserDto> teachers = userService.findByRole(UserRole.TEACHER);
        model.addAttribute("teachers", teachers);
        return "admin/employees";
    }

    @PostMapping("/admin/employees/create")
    public String createEmployee(@RequestParam String username,
        @RequestParam String email,
        Model model,
        RedirectAttributes redirectAttributes) {
        try {
            userService.createManager(username, email);
            redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("teacher.created", null, LocaleContextHolder.getLocale()));
            return "redirect:/admin/employees";
        } catch (ValidationException e) {
            Map<String, String> validationResults = e.getValidationResults();

            model.addAttribute("usernameValue", username);
            model.addAttribute("emailValue", email);

            if (validationResults.containsKey("username")) {
                model.addAttribute("usernameError", validationResults.get("username"));
            }
            if (validationResults.containsKey("email")) {
                model.addAttribute("emailError", validationResults.get("email"));
            }
            model.addAttribute("showCreateModal", true);
            model.addAttribute("teachers", userService.findByRole(UserRole.TEACHER));

            return "admin/employees";
        }
    }

    @PostMapping("/admin/employees/delete")
    public String deleteEmployee(@RequestParam Long id,
        RedirectAttributes redirectAttributes) {
        userService.deleteTeacher(id);
        redirectAttributes.addFlashAttribute("success", messageSource.getMessage(
            "teacher.delete", new Object[]{id}, LocaleContextHolder.getLocale()));
        return "redirect:/admin/employees";
    }

    @GetMapping("/admin/statistics")
    public String getStatisticPage(Model model) {
        Double passPercentage = statisticService.getAveragePassPercentage();
        Map<String, Double> averagePercentageByTest = statisticService.getAveragePercentageByTest();
        List<CourseDto> coursesWithoutTests = statisticService.findByTestIdIsNull();
        Map<CourseType, Double> averagePercentageByCourseType = statisticService.getAveragePercentageByCourseType();
        Map<TeacherDto, Long> teachersCourseCount = statisticService.getTeachersCourseCount();
        Map<TeacherDto, Double> averagePercentageByTeacher = statisticService.getAveragePercentageByTeacher();
        List<CourseType> courseTypeWithoutCourses = statisticService.courseTypeWithoutCourses();
        model.addAttribute("averagePassPercentage", passPercentage);
        model.addAttribute("coursesWithoutTests", coursesWithoutTests);
        model.addAttribute("averagePercentageByTest", averagePercentageByTest);
        model.addAttribute("averagePercentageByCourseType", averagePercentageByCourseType);
        model.addAttribute("teachersCourseCount", teachersCourseCount);
        model.addAttribute("averagePercentageByTeacher", averagePercentageByTeacher);
        model.addAttribute("courseTypeWithoutCourses", courseTypeWithoutCourses);
        return "admin/statistic";
    }
}