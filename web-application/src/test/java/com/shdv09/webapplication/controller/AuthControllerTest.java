package com.shdv09.webapplication.controller;

import com.shdv09.webapplication.feign.AuthServiceProxy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    private static final String JWT_TOKEN = "token";
    private static final String BASIC_TOKEN = "Basic dXNlcjpwYXNzd29yZA==";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthServiceProxy authServiceProxy;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(authServiceProxy);
    }

    @Test
    void loginPositiveTest() throws Exception {
        when(authServiceProxy.generateJwtToken(any(String.class))).thenReturn(JWT_TOKEN);

        MvcResult result = mockMvc.perform(get("/api/login")
                        .header("Authorization", BASIC_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertAll(
                () -> assertEquals(JWT_TOKEN, response),
                () -> verify(authServiceProxy).generateJwtToken(BASIC_TOKEN)
        );
    }

    @Test
    void loginErrorTest() throws Exception {
        when(authServiceProxy.generateJwtToken(any(String.class))).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/login")
                        .header("Authorization", BASIC_TOKEN))
                .andExpect(status().isInternalServerError())
                .andDo(print());
        assertAll(
                () -> verify(authServiceProxy).generateJwtToken(BASIC_TOKEN)
        );
    }

}
