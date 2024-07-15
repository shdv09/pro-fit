package com.shdv09.visitservice.service;

import com.shdv09.visitservice.dto.request.CreateVisitDto;
import com.shdv09.visitservice.dto.response.VisitDto;

public interface VisitService {
    public VisitDto createUpdateVisit(CreateVisitDto dto);
}
