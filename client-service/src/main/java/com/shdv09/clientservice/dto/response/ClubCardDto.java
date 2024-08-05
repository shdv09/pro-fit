package com.shdv09.clientservice.dto.response;

import com.shdv09.clientservice.model.CardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Объект DTO клубной карты клиента")
public class ClubCardDto {
    @Schema(description = "Идентификатор")
    private Long id;

    @Schema(description = "Номер")
    private String number;

    @Schema(description = "Тип")
    private CardType cardType;

    @Schema(description = "Признак активности")
    private Boolean isActive;
}
