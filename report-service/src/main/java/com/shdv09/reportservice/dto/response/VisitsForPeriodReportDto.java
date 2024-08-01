package com.shdv09.reportservice.dto.response;

import com.shdv09.reportservice.dto.response.visitservice.VisitDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitsForPeriodReportDto {
    private List<VisitDto> visits;
}
