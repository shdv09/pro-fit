package com.shdv09.clientservice.controller;

import com.shdv09.clientservice.dto.request.AddClientDto;
import com.shdv09.clientservice.dto.request.BindCardRequest;
import com.shdv09.clientservice.dto.request.UpdateClientDto;
import com.shdv09.clientservice.dto.response.ClientDto;
import com.shdv09.clientservice.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Client Controller", description = "Контроллер для работы с клиентами клуба")
@SecurityRequirement(name = "JWT")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/clients")
    @Operation(summary = "Метод поиска клиента по имени, фамилии, дате рождения")
    public List<ClientDto> findClients(
            @Parameter(description = "Имя") @RequestParam(name = "firstName", required = false) String firstName,
            @Parameter(description = "Фамилия") @RequestParam(name = "lastName", required = false) String lastName,
            @Parameter(description = "Дата рождения")
            @RequestParam(name = "birthDate", required = false) LocalDate birthDate) {
        return clientService.findClients(firstName, lastName, birthDate);

    }

    @GetMapping("/clients/{id}")
    @Operation(summary = "Метод поиск клиента по идентификатору")
    public ClientDto findClient(@Parameter(description = "Идентификатор клиента") @PathVariable(name = "id") long id) {
        return clientService.findClient(id);
    }

    @GetMapping("/clients/cards/{number}")
    @Operation(summary = "Метод поиск клиента по номеру клубной карты")
    public ClientDto findClientByCard(
            @Parameter(description = "Номер клубной карты")
            @PathVariable(name = "number") String number) {
        return clientService.findClientByCardNumber(number);
    }

    @PostMapping("/clients")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Метод добавления клиента")
    public ClientDto addClient(@RequestBody @Valid AddClientDto clientDto) {
        return clientService.addClient(clientDto);
    }

    @PutMapping("/clients")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Метод изменения клиента")
    public ClientDto updateClient(@RequestBody @Valid UpdateClientDto clientDto) {
        return clientService.updateClient(clientDto);
    }

    @PutMapping("clients/cards")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @Operation(summary = "Метод привязки клубной карты к клиету")
    public ClientDto bindCardToClient(@RequestBody @Valid BindCardRequest request) {
        return clientService.bindCard(request);
    }
}
