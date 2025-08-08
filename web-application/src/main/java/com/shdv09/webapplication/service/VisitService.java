package com.shdv09.webapplication.service;

import com.shdv09.webapplication.dto.request.ProcessVisitDto;
import com.shdv09.webapplication.dto.response.VisitDto;

public interface VisitService {
    VisitDto processVisit(ProcessVisitDto request);
}
