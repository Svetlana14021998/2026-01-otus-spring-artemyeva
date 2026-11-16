package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.user.UserDto;
import org.example.dto.user.UserShortDto;
import org.example.exception.ActiveUserException;
import org.example.exception.CantSaveFileException;
import org.example.exception.EntityNotFoundException;
import org.example.exception.InactiveUserException;
import org.example.exception.ValidationException;
import org.example.exception.WrongPasswordException;
import org.example.model.AppUser;
import org.example.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    private final MessageSource messageSource;

    @GetMapping("/login")
    public String getLoginPage() {
        return "profile/login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", UserShortDto.builder().build());
        }
        return "profile/register";
    }

    @PostMapping("/register")
    public String registration(@Valid @ModelAttribute("user") UserShortDto user,
        BindingResult result,
        RedirectAttributes redirectAttributes,
        Model model) {
        if (result.hasErrors()) {
            if (!model.containsAttribute("user")) {
                model.addAttribute("user", user);
            }
            return "profile/register";
        }
        try {
            userService.createNewUser(user);
            redirectAttributes.addFlashAttribute("success", "Аккаунт успешно создан");
            return "redirect:/login?success";
        } catch (WrongPasswordException e) {
            log.warn("Некорректный пароль: {}", e.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("passwordError", "Некорректное подтверждение пароля");
        } catch (ValidationException e) {
            Map<String, String> validationResults = e.getValidationResults();
            if (validationResults.containsKey("username")) {
                model.addAttribute("usernameError", "Данный логин уже используется другим пользователем");
            }
            if (validationResults.containsKey("email")) {
                model.addAttribute("emailError", "Данная почта уже используется другим пользователем");
            }
            model.addAttribute("user", user);
        }
        return "profile/register";
    }

    @GetMapping("/reset-password")
    public String getResetPasswordPage() {
        return "profile/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
        RedirectAttributes redirectAttributes,
        Model model) {
        try {
            userService.resetPassword(email);
            redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("success.reset.password", null, getLocale()));
            return "redirect:/login?success";
        } catch (EntityNotFoundException e) {
            log.warn("Пользователь с почтой {} не найден", email);
            model.addAttribute("enteredEmail", email);
            model.addAttribute("emailError",
                messageSource.getMessage("check.your.email", null, getLocale()));
            return "profile/reset-password";
        } catch (InactiveUserException e) {
            log.warn("Учетная запись пользователя с почтой {} деактивирована", email);
            model.addAttribute("enteredEmail", email);
            model.addAttribute("emailError",
                messageSource.getMessage("your.account.inactive", null, getLocale()));
            return "profile/reset-password";
        }
    }

    @GetMapping("/recover-account")
    public String getRecoverAccountPage() {
        return "profile/recover-account";
    }

    @PostMapping("/recover-account")
    public String recoverAccount(@RequestParam String email,
        RedirectAttributes redirectAttributes,
        Model model) {
        try {
            userService.recoverAccount(email);
            redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("account.recover", null, getLocale()));
            return "redirect:/login?success";
        } catch (EntityNotFoundException e) {
            log.warn("Пользователь с указанной почтой {} не найден", email);
            model.addAttribute("enteredEmail", email);
            model.addAttribute("emailError",
                messageSource.getMessage("not.success.account.recover", null, getLocale()));
            return "profile/recover-account";
        } catch (ActiveUserException e) {
            log.info("Аккаунт {} активен. Восстановление не требуется", email);
            model.addAttribute("enteredEmail", email);
            model.addAttribute("success",
                messageSource.getMessage("account.already.active", null, getLocale()));
            return "profile/recover-account";
        }
    }

    @GetMapping("/profile")
    public String getProfilePage(Authentication authentication, Model model) {
        AppUser appUser = (AppUser) authentication.getPrincipal();
        var user = userService.findById(appUser.getId());
        model.addAttribute("user", user);
        return "profile/profile";
    }

    @PostMapping("/profile/delete")
    public String deleteProfile(Authentication authentication,
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes attributes) {
        AppUser appUser = (AppUser) authentication.getPrincipal();
        userService.inactive(appUser.getId());

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);

        attributes.addFlashAttribute("success",
            messageSource.getMessage("user.account.deleted.success", null, getLocale()));
        return "redirect:/login";
    }

    @GetMapping("/profile/change-password")
    public String getChangePasswordPage() {
        return "profile/change-password";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String currentPassword,
        @RequestParam String newPassword,
        @RequestParam String confirmPassword,
        Authentication authentication,
        RedirectAttributes redirectAttributes,
        Model model) {
        try {
            AppUser user = (AppUser) authentication.getPrincipal();
            userService.changePassword(user, currentPassword, newPassword, confirmPassword);
            redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("password.changed.success", null, getLocale()));
            return "redirect:/profile";
        } catch (ValidationException e) {
            Map<String, String> validationResults = e.getValidationResults();
            model.addAttribute("errors", validationResults);
        }
        return "profile/change-password";
    }

    @GetMapping("/profile/edit")
    public String getEditPagePage(Authentication authentication, Model model) {
        AppUser user = (AppUser) authentication.getPrincipal();
        UserDto userDto = userService.findById(user.getId());
        model.addAttribute("user", userDto);
        return "profile/edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@Valid @ModelAttribute("user") UserDto userDto,
        BindingResult result,
        @RequestParam(required = false) MultipartFile photoFile,
        Authentication authentication,
        RedirectAttributes redirectAttributes,
        Model model) {

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "profile/edit-profile";
        }

        AppUser user = (AppUser) authentication.getPrincipal();

        try {
            userService.updateUserProfile(user.getId(), userDto, photoFile);
            redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("profile.updated.success", null, getLocale()));
            return "redirect:/profile";
        } catch (CantSaveFileException e) {
            log.error("Не удалось обновить профиль: ", e);
            redirectAttributes.addFlashAttribute("error",
                messageSource.getMessage("photo.saved.error", null, getLocale()));
            redirectAttributes.addFlashAttribute("user", userDto);
            return "redirect:/profile";
        }
    }
}