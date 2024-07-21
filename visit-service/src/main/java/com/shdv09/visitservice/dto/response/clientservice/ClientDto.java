package com.shdv09.visitservice.dto.response.clientservice;

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
}
