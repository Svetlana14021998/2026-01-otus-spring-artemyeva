package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserQuestionDto;
import org.example.enums.EmailTemplate;
import org.example.enums.UserRole;
import org.example.exception.SendMessagingException;
import org.example.service.ContactService;
import org.example.service.CourseService;
import org.example.service.TestService;
import org.example.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

    private final MessageSource messageSource;

    @GetMapping("/about")
    public String getAboutPage(Model model) {
        addCommonAttributes(model);
        if (!model.containsAttribute("question")) {
            model.addAttribute("question", new UserQuestionDto());
        }
        return "about";
    }

    @PostMapping("about/send-question")
    public String sendQuestion(@Valid @ModelAttribute UserQuestionDto question,
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
            redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("msg.send.success", null, LocaleContextHolder.getLocale()));
        } catch (SendMessagingException e) {
            redirectAttributes.addFlashAttribute("error",
                messageSource.getMessage("msg.send.error", null, LocaleContextHolder.getLocale()));
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