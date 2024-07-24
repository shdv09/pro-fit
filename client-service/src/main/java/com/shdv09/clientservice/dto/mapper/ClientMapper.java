package com.shdv09.clientservice.dto.mapper;

import com.shdv09.clientservice.dto.request.AddClientDto;
import com.shdv09.clientservice.dto.request.UpdateClientDto;
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

    public Client fromDto(AddClientDto dto) {
        Client result = new Client();
        result.setFirstName(dto.getFirstName());
        result.setLastName(dto.getLastName());
        result.setGender(dto.getGender());
        result.setBirthDate(dto.getBirthDate());
        return result;
    }

    public Client fromDto(UpdateClientDto dto) {
        Client result = new Client();
        result.setId(dto.getId());
        result.setFirstName(dto.getFirstName());
        result.setLastName(dto.getLastName());
        result.setGender(dto.getGender());
        result.setBirthDate(dto.getBirthDate());
        return result;
    }
}
