package com.shdv09.webapplication.service;

import com.shdv09.webapplication.dto.request.AddClientDto;
import com.shdv09.webapplication.dto.request.BindCardRequest;
import com.shdv09.webapplication.dto.request.UpdateClientDto;
import com.shdv09.webapplication.dto.response.ClientDto;
import com.shdv09.webapplication.feign.ClientServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientServiceProxy clientServiceProxy;

    @Override
    public List<ClientDto> findClients(String firstName, String lastName, LocalDate birthDate) {
        return clientServiceProxy.findClients(firstName, lastName, birthDate);
    }

    @Override
    public ClientDto findClient(Long id) {
        return clientServiceProxy.findClient(id);
    }

    @Override
    public ClientDto addClient(AddClientDto clientDto) {
        return clientServiceProxy.addClient(clientDto);
    }

    @Override
    public ClientDto updateClient(UpdateClientDto clientDto) {
        return clientServiceProxy.updateClient(clientDto);
    }

    @Override
    public ClientDto bindCard(BindCardRequest request) {
        return clientServiceProxy.bindCardToClient(request);
    }
}
