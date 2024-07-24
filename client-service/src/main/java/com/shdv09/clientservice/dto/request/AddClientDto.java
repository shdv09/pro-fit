package com.shdv09.clientservice.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AddClientDto {
    private String firstName;

    private String lastName;

    private String gender;

    private LocalDate birthDate;
}
