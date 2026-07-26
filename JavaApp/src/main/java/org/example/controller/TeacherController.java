package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.enums.CourseType;
import org.example.enums.QuestionType;
import org.example.model.AppUser;
import org.example.service.CourseService;
import org.example.service.TestService;
import org.example.validator.TestValidator;
import org.example.view.course.CourseCreateView;
import org.example.view.course.CourseShortView;
import org.example.view.course.CourseUpdateView;
import org.example.view.course.CourseView;
import org.example.view.course.CourseWithMaterialsView;
import org.example.view.test.create.TestCreateView;
import org.example.view.test.main.TestView;
import org.example.view.test.show.TestShortView;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TeacherController {

    private final CourseService courseService;

    private final TestService testService;

    private final TestValidator validator;

    @GetMapping("/teacher/courses")
    public String getMyCoursePage(@RequestParam(required = false) String description,
        @RequestParam(required = false) CourseType courseType,
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        @RequestParam(required = false, defaultValue = "5") int pageSize,
        Authentication authentication,
        Model model) {
        AppUser user = (AppUser) authentication.getPrincipal();
        Page<CourseView> courses = courseService.findAll(user.getId(), description, courseType, pageNumber, pageSize);
        model.addAttribute("courses", courses);
        model.addAttribute("courseType", CourseType.values());
        return "teacher/my-courses";
    }

    @PostMapping("/teacher/courses/delete")
    public String deleteCourse(@RequestParam Long id,
        Authentication authentication,
        RedirectAttributes redirectAttributes) {
        AppUser user = (AppUser) authentication.getPrincipal();
        courseService.deleteById(id, user.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Курс удален");
        return "redirect:/teacher/courses";
    }

    @GetMapping("/teacher/courses/create")
    public String getCourseCreatePage(Model model) {
        model.addAttribute("courseTypes", CourseType.values());
        return "teacher/create-course";
    }

    @GetMapping("/teacher/courses/view/{id}")
    public String getCourseViewPage(@PathVariable Long id, Model model) {
        CourseWithMaterialsView course = courseService.findByIdWithMaterials(id);
        model.addAttribute("course", course);
        return "teacher/view-course";
    }

    @PostMapping("/teacher/courses/create")
    public String createCourse(@Valid @ModelAttribute CourseCreateView courseCreateView,
        BindingResult bindingResult,
        Authentication authentication,
        RedirectAttributes redirectAttributes,
        Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("courseTypes", CourseType.values());
            model.addAttribute("courseCreateView", courseCreateView);
            model.addAttribute("errors", errors);
            return "teacher/create-course";
        }
        AppUser user = (AppUser) authentication.getPrincipal();
        courseService.save(courseCreateView, user);
        redirectAttributes.addFlashAttribute("successMessage", "Курс создан");
        return "redirect:/teacher/courses";
    }

    @GetMapping("/teacher/courses/edit/{id}")
    public String getCourseEditPage(@PathVariable Long id, Model model) {
        CourseUpdateView course = courseService.findByIdToEdit(id);
        model.addAttribute("courseTypes", CourseType.values());
        model.addAttribute("course", course);
        return "teacher/edit-course";
    }

    @PostMapping("/teacher/courses/edit")
    public String editCourse(@Valid @ModelAttribute CourseUpdateView courseUpdateView,
        BindingResult bindingResult,
        Authentication authentication,
        RedirectAttributes redirectAttributes,
        Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("courseTypes", CourseType.values());
            model.addAttribute("course", courseUpdateView);
            model.addAttribute("errors", errors);
            return "teacher/edit-course";
        }

        AppUser user = (AppUser) authentication.getPrincipal();
        courseService.update(courseUpdateView, user.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Курс изменен");
        return "redirect:/teacher/courses";
    }

    @GetMapping("/teacher/tests")
    public String getMyTestPage(@RequestParam(required = false) String title,
        @RequestParam(required = false) CourseType courseType,
        @RequestParam(required = false, defaultValue = "5") int pageSize,
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        Authentication authentication,
        Model model) {
        AppUser user = (AppUser) authentication.getPrincipal();
        Page<TestShortView> tests = testService.findAll(courseType, title, user.getId(), pageSize, pageNumber);
        model.addAttribute("tests", tests);
        model.addAttribute("courseType", CourseType.values());
        return "teacher/my-tests";
    }

    @GetMapping("/teacher/tests/view/{id}")
    public String getMyTestPage(@PathVariable Long id,
        Model model) {

        TestView test = testService.findById(id);
        model.addAttribute("test", test);

        return "teacher/view-test";
    }

    @PostMapping("/teacher/tests/delete")
    public String deleteTest(@RequestParam Long id,
        @RequestParam(required = false, defaultValue = "/teacher/tests") String redirectUrl,
        Authentication authentication,
        RedirectAttributes redirectAttributes) {
        AppUser user = (AppUser) authentication.getPrincipal();
        testService.deleteById(id, user.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Тест удален");
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/teacher/tests/create")
    public String getCreateTestPage(@RequestParam(required = false) Long courseId,
        Authentication authentication,
        Model model) {
        model.addAttribute("questionTypes", QuestionType.values());

        List<CourseShortView> courses;
        if (courseId != null) {
            courses = List.of(courseService.findByIdForTest(courseId));
        } else {
            AppUser appUser = (AppUser) authentication.getPrincipal();
            courses = courseService.findCourseWithoutTestForTeacher(appUser.getId());
        }

        model.addAttribute("courses", courses);
        model.addAttribute("selectedCourseId", courseId);

        if (!model.containsAttribute("testCreateView")) {
            model.addAttribute("testCreateView", new TestCreateView());
        }

        return "teacher/create-test";
    }

    @PostMapping("/teacher/tests/create")
    public String createTest(@ModelAttribute TestCreateView testCreateView,
        Authentication authentication,
        RedirectAttributes redirectAttributes,
        Model model) {

        Map<String, List<String>> validationResults = validator.validate(testCreateView);
        if (!validationResults.isEmpty()) {
            model.addAttribute("errors", validationResults);
            model.addAttribute("testCreateView", testCreateView);
            model.addAttribute("questionTypes", QuestionType.values());
            model.addAttribute("selectedCourseId", testCreateView.getCourseId());

            AppUser user = (AppUser) authentication.getPrincipal();
            List<CourseShortView> courses;
            if (testCreateView.getCourseId() != null) {
                courses = List.of(courseService.findByIdForTest(testCreateView.getCourseId()));
            } else {
                courses = courseService.findCourseWithoutTestForTeacher(user.getId());
            }
            model.addAttribute("courses", courses);

            return "teacher/create-test";
        }

        AppUser user = (AppUser) authentication.getPrincipal();
        testService.createTest(testCreateView, user.getId());

        redirectAttributes.addFlashAttribute("successMessage", "Тест успешно создан");
        return "redirect:/teacher/tests";
    }

    @GetMapping("/teacher/tests/edit/{id}")
    public String getEditTestPage(@PathVariable Long id,
        Model model) {
        TestView testView = testService.findById(id);
        model.addAttribute("questionTypes", QuestionType.values());
        model.addAttribute("test", testView);
        model.addAttribute("courseDescription",
            courseService.getCourseDescription(testView.getCourseId()));
        return "teacher/edit-test";
    }

    @PostMapping("/teacher/tests/edit")
    public String editTest(@ModelAttribute TestView testView,
        Authentication authentication,
        RedirectAttributes redirectAttributes,
        Model model) {

        Map<String, List<String>> validationResults = validator.validate(testView);
        if (!validationResults.isEmpty()) {

            model.addAttribute("errors", validationResults);
            model.addAttribute("test", testView);
            model.addAttribute("questionTypes", QuestionType.values());
            model.addAttribute("courseDescription",
                courseService.getCourseDescription(testView.getCourseId()));

            return "teacher/edit-test";
        }

        AppUser user = (AppUser) authentication.getPrincipal();
        testService.updateTest(testView, user.getId());

        redirectAttributes.addFlashAttribute("successMessage", "Тест успешно обновлен");
        return "redirect:/teacher/tests";
    }
}
