package com.shdv09.webapplication.feign;

import com.shdv09.webapplication.dto.request.AddClientDto;
import com.shdv09.webapplication.dto.request.BindCardRequest;
import com.shdv09.webapplication.dto.request.UpdateClientDto;
import com.shdv09.webapplication.dto.response.ClientDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "client-service", url = "client-service:8001")
public interface ClientServiceProxy {

    @GetMapping("/api/clients/{id}")
     ClientDto findClient(@PathVariable(name = "id") long id);

    @GetMapping("/api/clients/card/{number}")
    ClientDto findClientByCardNumber(@PathVariable(name = "number") String cardNumber);

    @GetMapping("/api/clients")
    List<ClientDto> findClients(@RequestParam(name = "firstName", required = false) String firstName,
                                       @RequestParam(name = "lastName", required = false) String lastName,
                                       @RequestParam(name = "birthDate", required = false) LocalDate birthDate);

    @PostMapping("/api/clients")
    ClientDto addClient(@RequestBody @Valid AddClientDto clientDto);

    @PutMapping("/api/clients")
    ClientDto updateClient(@RequestBody @Valid UpdateClientDto clientDto);

    @PutMapping("/api/clients/cards")
    ClientDto bindCardToClient(@RequestBody BindCardRequest request);
}
