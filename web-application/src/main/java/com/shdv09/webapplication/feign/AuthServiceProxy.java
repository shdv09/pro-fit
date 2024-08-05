package com.shdv09.webapplication.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url = "localhost:8004")
public interface AuthServiceProxy {
    @GetMapping("/api/token")
    String generateJwtToken(@RequestHeader(name = "Authorization", required = false) String basicAuthToken);
}
