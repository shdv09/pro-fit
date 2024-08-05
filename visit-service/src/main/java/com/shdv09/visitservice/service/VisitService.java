package com.shdv09.visitservice.service;

import com.shdv09.visitservice.dto.request.ProcessVisitDto;
import com.shdv09.visitservice.dto.response.VisitDto;

import java.time.LocalDate;
import java.util.List;

public interface VisitService {
    VisitDto processVisit(ProcessVisitDto dto);

    List<VisitDto> getVisitsForPeriod(Long clientId, LocalDate startDate, LocalDate endDate);
}
