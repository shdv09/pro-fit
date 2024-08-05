package com.shdv09.visitservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Объект DTO сообщения об ошибке")
public class ErrorDto {
    @Schema(description = "Описание ошибки")
    private String error;
}
