package com.shdv09.visitservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Объект DTO посещения клуба клиентом")
public class VisitDto {
    @Schema(description = "Идентификатор")
    private Long id;

    @Schema(description = "Начало посещения")
    private LocalDateTime start;

    @Schema(description = "Конец посещения")
    private LocalDateTime end;

    @Schema(description = "Идентификатор клиента")
    private Long clientId;
}
