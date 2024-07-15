package com.shdv09.visitservice.controller;

import com.shdv09.visitservice.dto.request.CreateVisitDto;
import com.shdv09.visitservice.dto.response.VisitDto;
import com.shdv09.visitservice.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class VisitController {

    private final VisitService visitService;

    @PostMapping("/api/visit")
    public VisitDto createUpdateVisit(CreateVisitDto dto) {
        return new VisitDto();
    }
}
