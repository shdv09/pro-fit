package com.shdv09.clientservice.dto.response;

import com.shdv09.clientservice.model.CardType;
import com.shdv09.clientservice.model.Client;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ClubCardDto {
    private Long id;
    private String number;
    private CardType cardType;
    private Boolean isActive;
}
