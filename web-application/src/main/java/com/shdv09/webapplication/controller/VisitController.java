package com.shdv09.webapplication.controller;

import com.shdv09.webapplication.dto.request.ProcessVisitDto;
import com.shdv09.webapplication.dto.response.VisitDto;
import com.shdv09.webapplication.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Visit Controller", description = "Контроллер для управления посещениями")
@SecurityRequirement(name = "JWT")
public class VisitController {
    private final VisitService visitService;

    @PostMapping("/visits")
    @Operation(summary = "Метод обработки посещения клиентом клуба")
    public VisitDto processVisit(@RequestBody @Valid ProcessVisitDto request) {
        return visitService.processVisit(request);
    }
}
