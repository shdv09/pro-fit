package com.shdv09.webapplication.service;

import com.shdv09.webapplication.dto.request.AddClientDto;
import com.shdv09.webapplication.dto.request.BindCardRequest;
import com.shdv09.webapplication.dto.request.UpdateClientDto;
import com.shdv09.webapplication.dto.response.ClientDto;
import com.shdv09.webapplication.feign.ClientServiceProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private static final String LOG_CODE = "CLIENT";

    private final ClientServiceProxy clientServiceProxy;

    @Override
    public List<ClientDto> findClients(String firstName, String lastName, LocalDate birthDate) {
        log.info("{}. Finding client by criteria. First name: {}, last name: {}, date of birth: {}",
                LOG_CODE, firstName, lastName, birthDate);
        return clientServiceProxy.findClients(firstName, lastName, birthDate);
    }

    @Override
    public ClientDto findClientByCardNumber(String cardNumber) {
        log.info("{}. Finding client by club card. Card number: {}", LOG_CODE, cardNumber);
        return clientServiceProxy.findClientByCardNumber(cardNumber);
    }

    @Override
    public ClientDto findClient(Long id) {
        log.info("{}. Finding client by id. Id: {}", LOG_CODE, id);
        return clientServiceProxy.findClient(id);
    }

    @Override
    public ClientDto addClient(AddClientDto request) {
        log.info("{}. Adding client. Request: {}", LOG_CODE, request);
        return clientServiceProxy.addClient(request);
    }

    @Override
    public ClientDto updateClient(UpdateClientDto request) {
        log.info("{}. Updating client. Request: {}", LOG_CODE, request);
        return clientServiceProxy.updateClient(request);
    }

    @Override
    public ClientDto bindCard(BindCardRequest request) {
        log.info("{}. Binding club card with client. Request: {}", LOG_CODE, request);
        return clientServiceProxy.bindCardToClient(request);
    }
}
