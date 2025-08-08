package com.shdv09.webapplication.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обработку посещения клиентом клуба")
public class ProcessVisitDto {
    @NotEmpty
    @Schema(description = "Номер карты", example = "12321")
    private String cardNumber;
}
