package com.irctc.irctc_train_booking.controller;

import com.irctc.irctc_train_booking.entity.Train;
import com.irctc.irctc_train_booking.entity.User;
import com.irctc.irctc_train_booking.service.TrainService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/train")
public class TrainController {

    private final TrainService trainService;

    // ADMIN: View dashboard
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }
        model.addAttribute("trains", trainService.getAllTrains());
        return "admin_dashboard";
    }

    // ADMIN: Add new train
    @GetMapping("/admin/add")
    public String addTrainForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }
        model.addAttribute("train", new Train());
        return "train_form";
    }

    @PostMapping("/admin/save")
    public String saveTrain(@ModelAttribute Train train, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }
        trainService.saveTrain(train);
        return "redirect:/train/admin/dashboard";
    }

    // ADMIN: Delete train
    @GetMapping("/admin/delete/{id}")
    public String deleteTrain(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }
        trainService.deleteTrain(id);
        return "redirect:/train/admin/dashboard";
    }

    // USER: Search trains
    @GetMapping("/user/search")
    public String searchTrains(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("trains", trainService.getAllTrains());
        return "train_list";
    }
}

