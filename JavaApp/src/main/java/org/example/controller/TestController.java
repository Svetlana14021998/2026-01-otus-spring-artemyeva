package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.enums.CourseType;
import org.example.model.AppUser;
import org.example.service.TestResultService;
import org.example.service.TestService;
import org.example.service.UserService;
import org.example.view.test.TestResultView;
import org.example.view.test.TestSubmitView;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;

    private final TestService testService;

    private final TestResultService testResultService;

    @GetMapping("/tests")
    public String getTestsPage(@RequestParam(required = false) CourseType courseType,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) Long teacherId,
        @RequestParam(required = false, defaultValue = "5") int pageSize,
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        Model model) {

        model.addAttribute("courseType", CourseType.values());
        model.addAttribute("authors", userService.findTeachers());
        model.addAttribute("tests", testService.findAll(courseType, title, teacherId, pageSize, pageNumber));
        return "tests/testsPage";
    }

    @GetMapping("/tests/{id}")
    public String getTestsPage(@PathVariable Long id,
        Model model) {

        model.addAttribute("test", testService.findById(id));
        return "tests/testPage";
    }

    @PostMapping("/tests/submit")
    public String submitTest(@RequestBody TestSubmitView testSubmitView,
        Authentication authentication,
        Model model) {
        AppUser user = (AppUser) authentication.getPrincipal();
        TestResultView result = testService.processTestResult(testSubmitView, user.getId());
        model.addAttribute("result", result);
        return "tests/resultPage";
    }

    @GetMapping("/tests/results")
    public String getTestResultsPage(@RequestParam(required = false) Boolean isOnlyLatestResults,
        @RequestParam(required = false) Boolean isPassed,
        @RequestParam(required = false) Long testId,
        @RequestParam(required = false) CourseType courseType,
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        @RequestParam(required = false, defaultValue = "5") int pageSize,
        Authentication authentication,
        Model model) {
        AppUser user = (AppUser) authentication.getPrincipal();
        model.addAttribute("courseType", CourseType.values());
        model.addAttribute("tests", testService.findAll());
        model.addAttribute("results", testResultService.findByUserId(user.getId(), isOnlyLatestResults, isPassed, testId, courseType, pageNumber, pageSize));
        return "tests/testResults";
    }
}
