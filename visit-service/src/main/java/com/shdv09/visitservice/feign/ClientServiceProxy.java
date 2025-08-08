package com.shdv09.visitservice.feign;

import com.shdv09.visitservice.dto.response.clientservice.ClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-service", url = "client-service:8001")
public interface ClientServiceProxy {

    @GetMapping("/api/clients/cards/{number}")
    ClientDto findClientByCardNumber(@PathVariable(name = "number") String cardNumber);
}
