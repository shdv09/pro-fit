package com.shdv09.clientservice.service;

import com.shdv09.clientservice.dto.request.AddClientDto;
import com.shdv09.clientservice.dto.request.BindCardRequest;
import com.shdv09.clientservice.dto.request.UpdateClientDto;
import com.shdv09.clientservice.dto.response.ClientDto;

import java.time.LocalDate;
import java.util.List;

public interface ClientService {
    ClientDto findClient(Long id);

    ClientDto findClientByCardNumber(String cardNumber);

    List<ClientDto> findClients(String firstName, String lastName, LocalDate birthDate);

    ClientDto addClient(AddClientDto clientDto);

    ClientDto updateClient(UpdateClientDto clientDto);

    ClientDto bindCard(BindCardRequest request);
}
