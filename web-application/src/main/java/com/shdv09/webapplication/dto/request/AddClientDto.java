package com.shdv09.webapplication.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class AddClientDto {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String gender;

    @NotNull
    private Date birthDate;
}
