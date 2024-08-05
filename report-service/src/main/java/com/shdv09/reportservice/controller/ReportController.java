package com.shdv09.reportservice.controller;

import com.shdv09.reportservice.dto.response.VisitsForPeriodReportDto;
import com.shdv09.reportservice.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Report Controller", description = "Контроллер для получения отчётов")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/reports/visits-for-period")
    @Operation(summary = "Метод получения отчёта по посещениям за период")
    public VisitsForPeriodReportDto getVisitsForPeriodReport(
            @Parameter(description = "Идентификатор клиента") @RequestParam("client") Long clientId,
            @Parameter(description = "Начало периода") @RequestParam("start") LocalDate start,
            @Parameter(description = "Конец периода") @RequestParam("end") LocalDate end) {
        return reportService.getVisitsForPeriodReport(clientId, start, end);
    }
}
