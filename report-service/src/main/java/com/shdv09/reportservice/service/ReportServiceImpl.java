package com.shdv09.reportservice.service;

import com.shdv09.reportservice.dto.response.VisitsForPeriodReportDto;
import com.shdv09.reportservice.dto.response.visitservice.VisitDto;
import com.shdv09.reportservice.feign.VisitServiceProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    private static final String LOG_CODE = "REPORT";

    private final VisitServiceProxy visitServiceProxy;

    @Override
    public VisitsForPeriodReportDto getVisitsForPeriodReport(Long clientId, LocalDate start, LocalDate end) {
        log.info("{}. Getting visits for period report. ClientId: {}, start: {}, end: {}",
                LOG_CODE, clientId, start, end);
        List<VisitDto> visitsForPeriod = visitServiceProxy.getVisitsForPeriod(clientId, start, end);
        return new VisitsForPeriodReportDto(visitsForPeriod);
    }
}
