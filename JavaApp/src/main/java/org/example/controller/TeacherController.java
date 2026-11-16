package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.course.CourseCreateDto;
import org.example.dto.course.CourseDto;
import org.example.dto.course.CourseShortDto;
import org.example.dto.course.CourseUpdateDto;
import org.example.dto.course.CourseWithMaterialsDto;
import org.example.dto.test.create.TestCreateDto;
import org.example.dto.test.main.TestDto;
import org.example.dto.test.show.TestShortDto;
import org.example.enums.CourseType;
import org.example.enums.QuestionType;
import org.example.model.AppUser;
import org.example.service.CourseService;
import org.example.service.TestService;
import org.example.validator.TestValidator;
import org.springframework.context.MessageSource;
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

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Controller
@RequiredArgsConstructor
public class TeacherController {

    private final CourseService courseService;

    private final TestService testService;

    private final TestValidator validator;

    private final MessageSource messageSource;

    @GetMapping("/teacher/courses")
    public String getMyCoursePage(@RequestParam(required = false) String description,
        @RequestParam(required = false) CourseType courseType,
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        @RequestParam(required = false, defaultValue = "5") int pageSize,
        Authentication authentication,
        Model model) {
        AppUser user = (AppUser) authentication.getPrincipal();
        Page<CourseDto> courses = courseService.findAll(user.getId(), description, courseType, pageNumber, pageSize);
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
        redirectAttributes.addFlashAttribute("successMessage",
            messageSource.getMessage("course.deleted.success", null, getLocale()));
        return "redirect:/teacher/courses";
    }

    @GetMapping("/teacher/courses/create")
    public String getCourseCreatePage(Model model) {
        model.addAttribute("courseTypes", CourseType.values());
        return "teacher/create-course";
    }

    @GetMapping("/teacher/courses/view/{id}")
    public String getCourseViewPage(@PathVariable Long id, Model model) {
        CourseWithMaterialsDto course = courseService.findByIdWithMaterials(id);
        model.addAttribute("course", course);
        return "teacher/view-course";
    }

    @PostMapping("/teacher/courses/create")
    public String createCourse(@Valid @ModelAttribute CourseCreateDto courseCreateDto,
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
            model.addAttribute("courseCreateView", courseCreateDto);
            model.addAttribute("errors", errors);
            return "teacher/create-course";
        }
        AppUser user = (AppUser) authentication.getPrincipal();
        courseService.save(courseCreateDto, user);
        redirectAttributes.addFlashAttribute("successMessage",
            messageSource.getMessage("course.created.success", null, getLocale()));
        return "redirect:/teacher/courses";
    }

    @GetMapping("/teacher/courses/edit/{id}")
    public String getCourseEditPage(@PathVariable Long id, Model model) {
        CourseUpdateDto course = courseService.findByIdToEdit(id);
        model.addAttribute("courseTypes", CourseType.values());
        model.addAttribute("course", course);
        return "teacher/edit-course";
    }

    @PostMapping("/teacher/courses/edit")
    public String editCourse(@Valid @ModelAttribute CourseUpdateDto courseUpdateDto,
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
            model.addAttribute("course", courseUpdateDto);
            model.addAttribute("errors", errors);
            return "teacher/edit-course";
        }

        AppUser user = (AppUser) authentication.getPrincipal();
        courseService.update(courseUpdateDto, user.getId());
        redirectAttributes.addFlashAttribute("successMessage",
            messageSource.getMessage("course.updated.success", null, getLocale()));
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
        Page<TestShortDto> tests = testService.findAll(courseType, title, user.getId(), pageSize, pageNumber);
        model.addAttribute("tests", tests);
        model.addAttribute("courseType", CourseType.values());
        return "teacher/my-tests";
    }

    @GetMapping("/teacher/tests/view/{id}")
    public String getMyTestPage(@PathVariable Long id,
        Model model) {

        TestDto test = testService.findById(id);
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
        redirectAttributes.addFlashAttribute("successMessage",
            messageSource.getMessage("test.deleted.success", null, getLocale()));
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/teacher/tests/create")
    public String getCreateTestPage(@RequestParam(required = false) Long courseId,
        Authentication authentication,
        Model model) {
        model.addAttribute("questionTypes", QuestionType.values());

        List<CourseShortDto> courses;
        if (courseId != null) {
            courses = List.of(courseService.findByIdForTest(courseId));
        } else {
            AppUser appUser = (AppUser) authentication.getPrincipal();
            courses = courseService.findCourseWithoutTestForTeacher(appUser.getId());
        }

        model.addAttribute("courses", courses);
        model.addAttribute("selectedCourseId", courseId);

        if (!model.containsAttribute("testCreateView")) {
            model.addAttribute("testCreateView", new TestCreateDto());
        }

        return "teacher/create-test";
    }

    @PostMapping("/teacher/tests/create")
    public String createTest(@ModelAttribute TestCreateDto testCreateDto,
        Authentication authentication,
        RedirectAttributes redirectAttributes,
        Model model) {

        Map<String, List<String>> validationResults = validator.validate(testCreateDto);
        if (!validationResults.isEmpty()) {
            model.addAttribute("errors", validationResults);
            model.addAttribute("testCreateView", testCreateDto);
            model.addAttribute("questionTypes", QuestionType.values());
            model.addAttribute("selectedCourseId", testCreateDto.getCourseId());

            AppUser user = (AppUser) authentication.getPrincipal();
            List<CourseShortDto> courses;
            if (testCreateDto.getCourseId() != null) {
                courses = List.of(courseService.findByIdForTest(testCreateDto.getCourseId()));
            } else {
                courses = courseService.findCourseWithoutTestForTeacher(user.getId());
            }
            model.addAttribute("courses", courses);

            return "teacher/create-test";
        }

        AppUser user = (AppUser) authentication.getPrincipal();
        testService.createTest(testCreateDto, user.getId());

        redirectAttributes.addFlashAttribute("successMessage",
            messageSource.getMessage("test.created.success", null, getLocale()));
        return "redirect:/teacher/tests";
    }

    @GetMapping("/teacher/tests/edit/{id}")
    public String getEditTestPage(@PathVariable Long id,
        Model model) {
        TestDto testDto = testService.findById(id);
        model.addAttribute("questionTypes", QuestionType.values());
        model.addAttribute("test", testDto);
        model.addAttribute("courseDescription",
            courseService.getCourseDescription(testDto.getCourseId()));
        return "teacher/edit-test";
    }

    @PostMapping("/teacher/tests/edit")
    public String editTest(@ModelAttribute TestDto testDto,
        Authentication authentication,
        RedirectAttributes redirectAttributes,
        Model model) {

        Map<String, List<String>> validationResults = validator.validate(testDto);
        if (!validationResults.isEmpty()) {

            model.addAttribute("errors", validationResults);
            model.addAttribute("test", testDto);
            model.addAttribute("questionTypes", QuestionType.values());
            model.addAttribute("courseDescription",
                courseService.getCourseDescription(testDto.getCourseId()));

            return "teacher/edit-test";
        }

        AppUser user = (AppUser) authentication.getPrincipal();
        testService.updateTest(testDto, user.getId());

        redirectAttributes.addFlashAttribute("successMessage",
            messageSource.getMessage("test.updated.success", null, getLocale()));
        return "redirect:/teacher/tests";
    }
}
