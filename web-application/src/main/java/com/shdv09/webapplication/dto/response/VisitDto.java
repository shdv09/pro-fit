package com.shdv09.webapplication.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class VisitDto {
    private Long id;

    private Date start;

    private Date end;

    private ClientDto client;
}
