package com.shdv09.reportservice.service;

import com.shdv09.reportservice.dto.response.VisitsForPeriodReportDto;

import java.time.LocalDate;

public interface ReportService {
    VisitsForPeriodReportDto getVisitsForPeriodReport(Long clientId, LocalDate start, LocalDate end);
}
