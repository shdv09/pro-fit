package com.shdv09.reportservice.feign;

import com.shdv09.reportservice.dto.response.clientservice.ClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "client-service", url = "localhost:8001")
public interface ClientServiceProxy {

    @GetMapping("/api/clients/{id}")
    ClientDto findClient(@PathVariable(name = "id") long id);

    @GetMapping("/api/clients/card/{number}")
    ClientDto findClientByCardNumber(@PathVariable(name = "number") String cardNumber);

    @GetMapping("/api/clients")
    List<ClientDto> findClients(@RequestParam(name = "firstName", required = false) String firstName,
                                @RequestParam(name = "lastName", required = false) String lastName,
                                @RequestParam(name = "birthDate", required = false) LocalDate birthDate);
}
