package com.shdv09.clientservice.dto.mapper;

import com.shdv09.clientservice.dto.response.ClubCardDto;
import com.shdv09.clientservice.model.ClubCard;
import org.springframework.stereotype.Component;

@Component
public class ClubCardMapper {
    public ClubCardDto toDto(ClubCard entity) {
        ClubCardDto result = new ClubCardDto();
        result.setId(entity.getId());
        result.setNumber(entity.getNumber());
        result.setCardType(entity.getCardType());
        result.setIsActive(entity.getIsActive());
        return result;
    }
}
