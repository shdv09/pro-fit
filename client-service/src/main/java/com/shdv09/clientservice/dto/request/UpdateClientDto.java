package com.shdv09.clientservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Запрос на изменение клиента")
public class UpdateClientDto {
    @NotNull
    @Schema(description = "Идентификатор клиента", example = "1")
    private Long id;

    @NotEmpty
    @Schema(description = "Имя", example = "Фёдор", minLength = 1)
    private String firstName;

    @NotEmpty
    @Schema(description = "Фамилия", example = "Сумкин", minLength = 1)
    private String lastName;

    @NotEmpty
    @Size(max = 1)
    @Schema(description = "Пол", example = "М", minLength = 1, maxLength = 1)
    private String gender;

    @NotNull
    @Schema(description = "Дата рождения", type = "string", format = "date", example = "2001-11-27")
    private LocalDate birthDate;
}
