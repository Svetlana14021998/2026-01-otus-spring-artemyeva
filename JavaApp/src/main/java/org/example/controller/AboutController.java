package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.enums.EmailTemplate;
import org.example.enums.UserRole;
import org.example.exception.SendMessagingException;
import org.example.service.ContactService;
import org.example.service.CourseService;
import org.example.service.TestService;
import org.example.service.UserService;
import org.example.view.UserQuestionView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class AboutController {

    private final ContactService contactService;

    private final UserService userService;

    private final CourseService courseService;

    private final TestService testService;

    @GetMapping("/about")
    public String getAboutPage(Model model) {
        addCommonAttributes(model);
        if (!model.containsAttribute("question")) {
            model.addAttribute("question", new UserQuestionView());
        }
        return "about";
    }

    @PostMapping("about/send-question")
    public String sendQuestion(@Valid @ModelAttribute UserQuestionView question,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("question", question);
            addCommonAttributes(model);

            return "about";
        }

        try {
            contactService.sendQuestionToAdmin(question, EmailTemplate.QUESTION);
            redirectAttributes.addFlashAttribute("success", "Сообщение успешно отправлено!");
        } catch (SendMessagingException e) {
            redirectAttributes.addFlashAttribute("error",
                "Ошибка при отправке сообщения. Если в течении суток с вами не свяжутся, пожатуйста, позвоните в службу поддержки");
        }
        return "redirect:/about";
    }

    private void addCommonAttributes(Model model) {
        model.addAttribute("tests", testService.testsCount());
        model.addAttribute("courses", courseService.courseCount());
        model.addAttribute("users", userService.countByRole(UserRole.USER));
        model.addAttribute("teachers", userService.countByRole(UserRole.TEACHER));
    }
}
