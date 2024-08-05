package com.shdv09.reportservice.dto.response.visitservice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "Объект DTO посещения клуба клиентом")
public class VisitDto {
    @Schema(description = "Идентификатор")
    private Long id;

    @Schema(description = "Начало посещения")
    private Date start;

    @Schema(description = "Конец посещения")
    private Date end;

    @Schema(description = "Идентификатор клиента")
    private Long clientId;
}
