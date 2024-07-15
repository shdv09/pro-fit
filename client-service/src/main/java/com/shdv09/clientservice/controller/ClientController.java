package com.shdv09.clientservice.controller;

import com.shdv09.clientservice.dto.response.ClientDto;
import com.shdv09.clientservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/client/{id}")
    public ClientDto findClient(@PathVariable(name = "id") long id) {
        return clientService.findClient(id);
    }

    @GetMapping("/client/card/{number}")
    public ClientDto findClientByCard(@PathVariable(name = "number") String number) {
        return clientService.findClientByCardNumber(number);
    }
}
