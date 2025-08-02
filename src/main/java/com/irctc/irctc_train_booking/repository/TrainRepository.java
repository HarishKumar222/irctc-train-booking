package com.irctc.irctc_train_booking.repository;



import com.irctc.irctc_train_booking.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository extends JpaRepository<Train, Long> {

}
