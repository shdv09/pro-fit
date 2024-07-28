package com.shdv09.visitservice.controller;

import com.shdv09.visitservice.dto.request.ProcessVisitDto;
import com.shdv09.visitservice.dto.response.VisitDto;
import com.shdv09.visitservice.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "Visit Controller", description = "Контроллер для управления посещениями")
@SecurityRequirement(name = "JWT")
public class VisitController {

    private final VisitService visitService;

    @PostMapping("/visits")
    @Operation(summary = "Метод обработки посещения клиентом клуба")
    public VisitDto processVisit(@RequestBody @Valid ProcessVisitDto dto) {
        return visitService.processVisit(dto);
    }
}
