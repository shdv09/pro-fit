package com.shdv09.webapplication.service;

import com.shdv09.webapplication.dto.request.ProcessVisitDto;
import com.shdv09.webapplication.dto.response.VisitDto;
import com.shdv09.webapplication.feign.VisitServiceProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitServiceImpl implements VisitService {
    private static final String LOG_CODE = "VISIT";

    private final VisitServiceProxy visitServiceProxy;

    @Override
    public VisitDto processVisit(ProcessVisitDto request) {
        log.info("{}. Processing visit. Request: {}", LOG_CODE, request);
        return visitServiceProxy.processVisit(request);
    }
}
