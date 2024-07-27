package com.shdv09.authservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenControllerTest {
    private static final String TOKEN = "token";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtEncoder encoder;

    @BeforeEach
    void init() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(encoder);
    }

    @Test
    @WithMockUser(username = "user")
    void generateTokenPositiveTest() throws Exception {
        Jwt jwt = Mockito.mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn(TOKEN);
        when(encoder.encode(any())).thenReturn(jwt);

        MvcResult result = mockMvc.perform(get("/api/token"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(encoder).encode(captor.capture());
        Map<String, Object> claims = captor.getValue().getClaims().getClaims();
        assertAll(
                () -> assertEquals(TOKEN, response),
                () -> assertEquals("self", claims.get("iss")),
                () -> assertEquals("user", claims.get("sub")),
                () -> assertEquals("ROLE_USER", claims.get("scope"))
        );
    }

    @Test
    @WithMockUser(username = "user")
    void generateTokenErrorTest() throws Exception {
        when(encoder.encode(any())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/token"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"error\":null}"))
                .andDo(print());

        assertAll(
                () -> verify(encoder).encode(any())
        );
    }

    @Test
    void generateTokenUnauthorizedTest() throws Exception {
        mockMvc.perform(get("/api/token"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
