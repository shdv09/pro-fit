package com.shdv09.visitservice.dto.response.clientservice;

import lombok.Data;

@Data
public class ClubCardDto {
    private Long id;
    private String number;
    private CardType cardType;
    private Boolean isActive;
}
