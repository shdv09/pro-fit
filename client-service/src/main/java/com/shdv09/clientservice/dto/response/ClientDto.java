package com.shdv09.clientservice.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String gender;

    private LocalDate birthDate;

    private ClubCardDto card;
}
