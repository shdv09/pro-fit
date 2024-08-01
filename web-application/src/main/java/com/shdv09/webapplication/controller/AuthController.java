package com.shdv09.webapplication.controller;

import com.shdv09.webapplication.feign.AuthServiceProxy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Authorization Controller", description = "Контроллер авторизации пользователя")
@SecurityRequirement(name = "basic")
public class AuthController {
    private final AuthServiceProxy authServiceProxy;

    @PostMapping("/login")
    @Operation(summary = "Метод авторизации пользователя", responses = {
            @ApiResponse(description = "JWT токен для авторизации")
    })
    public String generateToken(
            @Parameter(description = "Basic auth токен")
            @RequestHeader(name = "Authorization", required = false) String basicAuthToken) {
        return authServiceProxy.generateJwtToken(basicAuthToken);
    }
}
