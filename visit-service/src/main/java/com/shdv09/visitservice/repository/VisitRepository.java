package com.shdv09.visitservice.repository;

import com.shdv09.visitservice.model.Visit;
import org.springframework.data.repository.CrudRepository;

public interface VisitRepository extends CrudRepository<Visit, Long> {
    Visit findFirstByClientIdOrderByStartTimeDesc(Long clientId);
}
