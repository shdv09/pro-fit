package com.shdv09.clientservice.dto.response;

import com.shdv09.clientservice.model.CardType;
import lombok.Data;

@Data
public class ClubCardDto {
    private Long id;

    private String number;

    private CardType cardType;

    private Boolean isActive;
}
