package com.shdv09.visitservice.dto.mapper;

import com.shdv09.visitservice.dto.response.VisitDto;
import com.shdv09.visitservice.dto.response.clientservice.ClientDto;
import com.shdv09.visitservice.model.Visit;
import org.springframework.stereotype.Component;

@Component
public class VisitMapper {
    public VisitDto toDto(Visit entity, ClientDto clientDto) {
        VisitDto result = new VisitDto();
        result.setId(entity.getId());
        result.setStart(entity.getStartTime());
        result.setEnd(entity.getEndTime());
        result.setClient(clientDto);
        return result;
    }
}
