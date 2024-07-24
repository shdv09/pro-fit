package com.shdv09.webapplication.service;

import com.shdv09.webapplication.dto.request.ProcessVisitDto;
import com.shdv09.webapplication.dto.response.VisitDto;
import com.shdv09.webapplication.feign.VisitServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {
    private final VisitServiceProxy visitServiceProxy;

    @Override
    public VisitDto processVisit(ProcessVisitDto request) {
        return visitServiceProxy.processVisit(request);
    }
}
