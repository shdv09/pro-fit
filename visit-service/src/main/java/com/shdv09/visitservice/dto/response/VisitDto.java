package com.shdv09.visitservice.dto.response;

import com.shdv09.visitservice.dto.response.clientservice.ClientDto;
import lombok.Data;

import java.util.Date;

@Data
public class VisitDto {
    private Long id;

    private Date start;

    private Date end;

    private ClientDto client;
}
