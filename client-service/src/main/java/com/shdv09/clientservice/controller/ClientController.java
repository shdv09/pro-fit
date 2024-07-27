package com.shdv09.clientservice.controller;

import com.shdv09.clientservice.dto.request.AddClientDto;
import com.shdv09.clientservice.dto.request.BindCardRequest;
import com.shdv09.clientservice.dto.request.UpdateClientDto;
import com.shdv09.clientservice.dto.response.ClientDto;
import com.shdv09.clientservice.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/clients")
    public List<ClientDto> findClients(@RequestParam(name = "firstName", required = false) String firstName,
                                       @RequestParam(name = "lastName", required = false) String lastName,
                                       @RequestParam(name = "birthDate", required = false) LocalDate birthDate) {
        return clientService.findClients(firstName, lastName, birthDate);

    }

    @GetMapping("/clients/{id}")
    public ClientDto findClient(@PathVariable(name = "id") long id) {
        return clientService.findClient(id);
    }

    @GetMapping("/clients/cards/{number}")
    public ClientDto findClientByCard(@PathVariable(name = "number") String number) {
        return clientService.findClientByCardNumber(number);
    }

    @PostMapping("/clients")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ClientDto addClient(@RequestBody @Valid AddClientDto clientDto) {
        return clientService.addClient(clientDto);
    }

    @PutMapping("/clients")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ClientDto updateClient(@RequestBody @Valid UpdateClientDto clientDto) {
        return clientService.updateClient(clientDto);
    }

    @PutMapping("clients/cards")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ClientDto bindCardToClient(@RequestBody @Valid BindCardRequest request) {
        return clientService.bindCard(request);
    }
}
