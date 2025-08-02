package com.irctc.irctc_train_booking.service;



import com.irctc.irctc_train_booking.entity.Train;
import com.irctc.irctc_train_booking.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepository;

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    public Train saveTrain(Train train) {
        return trainRepository.save(train);
    }

    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }

    public Optional<Train> getTrainById(Long id) {
        return trainRepository.findById(id);
    }
}
