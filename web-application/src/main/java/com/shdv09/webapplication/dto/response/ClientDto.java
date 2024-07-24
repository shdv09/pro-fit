package com.shdv09.webapplication.dto.response;

import com.shdv09.webapplication.model.CardType;
import lombok.Data;

import java.util.Date;

@Data
public class ClientDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String gender;

    private Date birthDate;

    private ClubCardDto card;

    @Data
    public static class ClubCardDto {
        private Long id;

        private String number;

        private CardType cardType;

        private Boolean isActive;
    }
}
