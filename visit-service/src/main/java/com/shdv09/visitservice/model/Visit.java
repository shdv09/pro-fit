package com.shdv09.visitservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Visit {
    @Id
    private Long id;
    private Date startTime;
    private Date endTime;
    private Long clientId;
}
