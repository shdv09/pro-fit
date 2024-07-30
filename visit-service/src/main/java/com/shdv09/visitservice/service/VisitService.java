package com.shdv09.visitservice.service;

import com.shdv09.visitservice.dto.request.ProcessVisitDto;
import com.shdv09.visitservice.dto.response.VisitDto;

public interface VisitService {
    VisitDto processVisit(ProcessVisitDto dto);
}
