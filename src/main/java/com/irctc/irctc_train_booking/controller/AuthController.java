package com.irctc.irctc_train_booking.controller;



import com.irctc.irctc_train_booking.entity.Booking;
import com.irctc.irctc_train_booking.entity.Train;
import com.irctc.irctc_train_booking.entity.User;
import com.irctc.irctc_train_booking.service.BookingService;
import com.irctc.irctc_train_booking.service.TrainService;
import com.irctc.irctc_train_booking.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final BookingService bookingService;
    private final TrainService trainService;

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }


    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        return userService.login(email, password).map(user -> {
            session.setAttribute("user", user);
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin/dashboard";
            } else {
                return "user/dashboard";
            }
        }).orElseGet(() -> {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        });
    }
    @GetMapping("/user/dashboard")
    public String userDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login"; // or show error
        }

        model.addAttribute("user", user);
        return "user/dashboard";
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

