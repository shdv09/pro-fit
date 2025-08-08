package com.shdv09.visitservice.dto.response.clientservice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

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
    private Date birthDate;

    @Schema(description = "Клубная карта")
    private ClubCardDto card;
}
