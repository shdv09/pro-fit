package com.shdv09.appointmentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "time_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
    @EmbeddedId
    private TimeSlotPK pKey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    @MapsId("trainerId")
    private Trainer trainer;
    @Column(nullable = false)
    private Long clientId;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class TimeSlotPK implements Serializable {
        private Long trainerId;
        @Column(nullable = false)
        private LocalDate workoutDate;
        @Column(nullable = false)
        private Integer workoutHour;
    }
}
