package com.irctc.irctc_train_booking.controller;

import com.irctc.irctc_train_booking.entity.Booking;
import com.irctc.irctc_train_booking.entity.Train;
import com.irctc.irctc_train_booking.entity.User;
import com.irctc.irctc_train_booking.service.BookingService;
import com.irctc.irctc_train_booking.service.TrainService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;
    private final TrainService trainService;

    @GetMapping("/book/{trainId}")
    public String bookTrainForm(@PathVariable Long trainId, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }
        Optional<Train> train = trainService.getTrainById(trainId);
        train.ifPresent(t -> model.addAttribute("train", t));
        return "book_form";
    }

    @PostMapping("/book")
    public String bookTrain(@RequestParam Long trainId,
                            @RequestParam Integer seats,
                            HttpSession session,
                            Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/";

        Optional<Train> trainOpt = trainService.getTrainById(trainId);
        if (trainOpt.isPresent()) {
            try {
                bookingService.bookTrain(user, trainOpt.get(), seats);
                return "redirect:/booking/user/history";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                model.addAttribute("train", trainOpt.get());
                return "book_form";
            }
        } else {
            model.addAttribute("error", "Train not found");
            return "book_form";
        }
    }

    @GetMapping("/user/history")
    public String viewUserBookings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/";
        model.addAttribute("bookings", bookingService.getBookingsByUser(user));
        return "booking_history";
    }

    @GetMapping("/user/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Please login to cancel bookings.");
            return "redirect:/";
        }

        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            // Ensure only the user who booked can cancel it
            if (booking.getUser().getId().equals(user.getId())) {
                bookingService.cancelBooking(id);
                redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Unauthorized to cancel this booking.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Booking not found.");
        }

        return "redirect:/booking/user/history";
    }

    @GetMapping("/admin/all")
    public String viewAllBookings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin_bookings";
    }
}


