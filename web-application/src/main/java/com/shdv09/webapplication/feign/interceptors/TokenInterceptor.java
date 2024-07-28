package com.shdv09.webapplication.feign.interceptors;

import com.shdv09.webapplication.service.auth.AuthService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenInterceptor implements RequestInterceptor {
    private final AuthService authService;

    @Override
    public void apply(RequestTemplate template) {
        String jwtToken = authService.getToken();
        if (jwtToken != null) {
            template.header("Authorization", "Bearer " + authService.getToken());
        }
    }
}
