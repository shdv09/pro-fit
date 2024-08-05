package com.shdv09.webapplication.service;

import com.shdv09.webapplication.dto.request.AddClientDto;
import com.shdv09.webapplication.dto.request.BindCardRequest;
import com.shdv09.webapplication.dto.request.UpdateClientDto;
import com.shdv09.webapplication.dto.response.ClientDto;

import java.time.LocalDate;
import java.util.List;

public interface ClientService {
    List<ClientDto> findClients(String firstName, String lastName, LocalDate birthDate);

    ClientDto findClient(Long id);

    ClientDto findClientByCardNumber(String cardNumber);

    ClientDto addClient(AddClientDto clientDto);

    ClientDto updateClient(UpdateClientDto clientDto);

    ClientDto bindCard(BindCardRequest request);
}
