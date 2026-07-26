package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
import org.example.exception.EntityLinksException;
import org.example.exception.EntityNotFoundException;
import org.example.exception.PermissionException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class AdviceController {

    private final AppConfig appConfig;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("email", appConfig.getEmail());
        model.addAttribute("phone", appConfig.getPhone());
        model.addAttribute("telegram", appConfig.getTelegram());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException e,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes) {
        log.error("Сущность не найдена: {}", e.getMessage());

        redirectAttributes.addFlashAttribute("globalError", "Запрашиваемая запись не найдена");
        return redirect(request);
    }

    @ExceptionHandler(PermissionException.class)
    public String handlePermissionException(PermissionException e,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes) {
        log.error("Ошибка прав доступа: {}", e.getMessage());

        redirectAttributes.addFlashAttribute("globalError", e.getMsgForUser());
        return redirect(request);
    }

    @ExceptionHandler(EntityLinksException.class)
    public String handleEntityLinksException(EntityLinksException e,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes) {
        log.error("Ошибка связи между объектами: {}", e.getMessage());

        redirectAttributes.addFlashAttribute("globalError", e.getMsgForUser());
        return redirect(request);
    }

    private String redirect(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            try {
                URL url = new URL(referer);
                String path = url.getPath();
                String contextPath = appConfig.getContextPath();
                if (path.startsWith(contextPath)) {
                    path = path.substring(contextPath.length());
                }
                if (path.isEmpty()) {
                    path = "/";
                }
                if (url.getQuery() != null) {
                    path += "?" + url.getQuery();
                }
                return "redirect:" + path;
            } catch (MalformedURLException ex) {
                log.warn("Invalid referer URL: {}", referer);
                return "redirect:/main";
            }
        }
        return "redirect:/main";
    }
}
