package com.shdv09.authservice.controller;

import com.shdv09.authservice.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Token Controller", description = "Контроллер для работы с токенами доступа")
@SecurityRequirement(name = "basic")
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/token")
    @Operation(summary = "Метод для получения JWT-токена для авторизации в сервисах")
    public String generateToken(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }
}
