package com.shdv09.visitservice.controller;

import com.shdv09.visitservice.dto.request.ProcessVisitDto;
import com.shdv09.visitservice.dto.response.VisitDto;
import com.shdv09.visitservice.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class VisitController {

    private final VisitService visitService;

    @PostMapping("/visit")
    public VisitDto processVisit(@RequestBody @Valid ProcessVisitDto dto) {
        return visitService.processVisit(dto);
    }
}
