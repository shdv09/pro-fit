package com.shdv09.visitservice.validation;

import com.shdv09.visitservice.dto.response.clientservice.CardType;
import com.shdv09.visitservice.dto.response.clientservice.ClubCardDto;
import com.shdv09.visitservice.exception.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class CardValidator {
    public void validateCard(ClubCardDto card) {
        CardType cardType = card.getCardType();
        LocalTime now = LocalTime.now();
        LocalTime start = cardType.getStart();
        LocalTime end = cardType.getEnd();
        if (now.isBefore(start) || now.isAfter(end)) {
            throw new AccessDeniedException("Access to club denied for card %s, invalid hours"
                    .formatted(card.getNumber()));
        }
    }
}
