package com.shdv09.clientservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Запрос привязку клубной карты к клиенту")
public class BindCardRequest {
    @NotNull
    @Schema(description = "Идентификатор клиента", example = "1")
    private Long clientId;

    @NotNull
    @Schema(description = "Идентификатор клубной карты", example = "2")
    private Long cardId;
}
