package com.shdv09.webapplication.dto.response;

import com.shdv09.webapplication.model.Specialization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Объект DTO тренера")
public class TrainerDto {
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

    @Schema(description = "Специализация")
    private Specialization specialization;
}
