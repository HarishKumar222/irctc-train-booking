package com.irctc.irctc_train_booking.service;



import com.irctc.irctc_train_booking.entity.Booking;
import com.irctc.irctc_train_booking.entity.Train;
import com.irctc.irctc_train_booking.entity.User;
import com.irctc.irctc_train_booking.repository.BookingRepository;
import com.irctc.irctc_train_booking.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TrainRepository trainRepository;

    public Booking bookTrain(User user, Train train, int seats) {
        if (train.getTotalSeats() < seats) {
            throw new RuntimeException("Not enough seats available");
        }

        train.setTotalSeats(train.getTotalSeats() - seats);
        trainRepository.save(train);

        Booking booking = Booking.builder()
                .user(user)
                .train(train)
                .seatsBooked(seats)
                .bookingDate(LocalDate.now())
                .build();

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public void cancelBooking(Long id) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            Train train = booking.getTrain();

            // Restore seats
            train.setTotalSeats(train.getTotalSeats() + booking.getSeatsBooked());
            trainRepository.save(train);

            // Delete booking
            bookingRepository.deleteById(id);
        }
    }


}

