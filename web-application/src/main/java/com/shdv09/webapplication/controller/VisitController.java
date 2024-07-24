package com.shdv09.webapplication.controller;

import com.shdv09.webapplication.dto.request.ProcessVisitDto;
import com.shdv09.webapplication.dto.response.VisitDto;
import com.shdv09.webapplication.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @PostMapping("/visits")
    public VisitDto processVisit(@RequestBody @Valid ProcessVisitDto request) {
        return visitService.processVisit(request);
    }
}
