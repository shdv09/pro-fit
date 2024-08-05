package com.shdv09.clientservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Объект DTO клиента")
public class ClientDto {
    @Schema(description = "Идентификатор")
    private Long id;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Пол")
    private String gender;

    @Schema(description = "Дата рождения")
    private LocalDate birthDate;

    @Schema(description = "Клубная карта")
    private ClubCardDto card;
}
