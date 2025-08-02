package com.irctc.irctc_train_booking.service;



import com.irctc.irctc_train_booking.entity.User;
import com.irctc.irctc_train_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(User user) {
        user.setRole("USER"); // default role
        return userRepository.save(user);
    }

    public Optional<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

