package com.shdv09.reportservice.feign;

import com.shdv09.reportservice.dto.response.visitservice.VisitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "visit-service", url = "visit-service:8002")
public interface VisitServiceProxy {

    @GetMapping("/api/visits")
    List<VisitDto> getVisitsForPeriod(
            @RequestParam("client") Long clientId,
            @RequestParam("start") LocalDate startDate,
            @RequestParam("end") LocalDate endDate);
}
