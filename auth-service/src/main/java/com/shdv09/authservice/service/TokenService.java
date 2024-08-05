package com.shdv09.authservice.service;

import org.springframework.security.core.Authentication;

public interface TokenService {
    String generateToken(Authentication authentication);
}
