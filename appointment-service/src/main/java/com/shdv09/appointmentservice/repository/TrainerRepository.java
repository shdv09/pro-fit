package com.shdv09.appointmentservice.repository;

import com.shdv09.appointmentservice.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
