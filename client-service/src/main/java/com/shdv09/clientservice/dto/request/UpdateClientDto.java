package com.shdv09.clientservice.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateClientDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String gender;

    private LocalDate birthDate;
}
