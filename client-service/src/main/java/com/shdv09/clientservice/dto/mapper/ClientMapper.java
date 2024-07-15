package com.shdv09.clientservice.dto.mapper;

import com.shdv09.clientservice.dto.response.ClientDto;
import com.shdv09.clientservice.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public ClientDto toDto(Client entity) {
        ClientDto result = new ClientDto();
        result.setId(entity.getId());
        result.setFirstName(entity.getFirstName());
        result.setLastName(entity.getLastName());
        result.setGender(entity.getGender());
        result.setBirthDate(entity.getBirthDate());
        return result;
    }
}
