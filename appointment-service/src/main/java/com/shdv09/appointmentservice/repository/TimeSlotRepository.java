package com.shdv09.appointmentservice.repository;

import com.shdv09.appointmentservice.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, TimeSlot.TimeSlotPK> {
    List<TimeSlot> findBypKeyWorkoutDateAndTrainerId(LocalDate workoutDate, Long trainerId);
}
