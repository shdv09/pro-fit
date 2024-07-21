package com.shdv09.clientservice.service;

import com.shdv09.clientservice.dto.response.ClientDto;

public interface ClientService {
    ClientDto findClient(Long id);

    ClientDto findClientByCardNumber(String cardNumber);
}
