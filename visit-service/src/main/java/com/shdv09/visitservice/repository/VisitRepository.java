package com.shdv09.visitservice.repository;

import com.shdv09.visitservice.model.Visit;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends CrudRepository<Visit, Long> {
    Visit findFirstByClientIdOrderByStartTimeDesc(Long clientId);

    List<Visit> findByEndTimeBetweenAndClientId(LocalDateTime startDate, LocalDateTime endDate, Long clientId);
}
