package com.shdv09.webapplication.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "Запрос на создание клиента")
public class AddClientDto {
    @NotEmpty
    @Schema(description = "Имя", example = "Фёдор")
    private String firstName;

    @NotEmpty
    @Schema(description = "Фамилия", example = "Сумкин")
    private String lastName;

    @NotEmpty
    @Size(max = 1)
    @Schema(description = "Пол", example = "М", maxLength = 1)
    private String gender;

    @NotNull
    @Schema(description = "Дата рождения", type = "string", format = "date", example = "2001-11-27")
    private Date birthDate;
}
