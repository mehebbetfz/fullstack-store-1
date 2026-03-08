package com.my.store.controller;

import com.my.store.model.User;
import com.my.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        model.addAttribute("user", user);
        return "profile";
    }


    @PostMapping("/update")
    public String update(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phone,
            @RequestParam String address,
            RedirectAttributes redirectAttributes
    ) {
        userService.updateProfile(userDetails.getUsername(), firstName, lastName, phone, address);
        redirectAttributes.addFlashAttribute("success", "Профиль обновлен");
        return "redirect:/profile";
    }

    @PostMapping("/password")
    public String changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Пароли не совпадают");
            return "redirect:/profile";
        }

        try {
            userService.changePassword(userDetails.getUsername(), oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("success", "Пароль успешно изменен");
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/profile";
    }

}
