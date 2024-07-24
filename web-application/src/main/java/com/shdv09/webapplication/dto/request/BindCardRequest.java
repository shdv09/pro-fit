package com.shdv09.webapplication.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BindCardRequest {
    @NotNull
    private Long clientId;

    @NotNull
    private Long cardId;
}
