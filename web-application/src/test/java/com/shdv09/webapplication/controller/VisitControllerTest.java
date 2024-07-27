package com.shdv09.webapplication.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shdv09.webapplication.dto.request.ProcessVisitDto;
import com.shdv09.webapplication.dto.response.VisitDto;
import com.shdv09.webapplication.feign.VisitServiceProxy;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VisitControllerTest {
    private static final String CARD_NUMBER = "12321";
    private static final String VISIT_DTO_JSON_PATH = "/controller/visit/visitDto.json";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitServiceProxy visitServiceProxy;

    @BeforeEach
    void init() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(visitServiceProxy);
    }

    @Test
    @WithMockUser(username = "user")
    void processVisitPositiveTest() throws Exception {
        VisitDto visit = mapper.readValue(getFileContent(VISIT_DTO_JSON_PATH), VisitDto.class);
        when(visitServiceProxy.processVisit(any(ProcessVisitDto.class))).thenReturn(visit);
        ProcessVisitDto request = new ProcessVisitDto(CARD_NUMBER);

        MvcResult result = mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        VisitDto response = mapper.readValue(result.getResponse().getContentAsString(), VisitDto.class);
        assertAll(
                () -> assertEquals(visit, response),
                () -> verify(visitServiceProxy).processVisit(request)
        );
    }

    @Test
    @WithMockUser(username = "user")
    void processVisitErrorTest() throws Exception {
        FeignException ex = mock(FeignException.class);
        when(ex.status()).thenReturn(404);
        when(ex.getMessage()).thenReturn("Client not found");
        when(visitServiceProxy.processVisit(any(ProcessVisitDto.class))).thenThrow(ex);
        ProcessVisitDto request = new ProcessVisitDto(CARD_NUMBER);

        mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Client not found\"}"))
                .andDo(print());

        assertAll(
                () -> verify(visitServiceProxy).processVisit(request)
        );
    }

    @Test
    @WithMockUser(username = "user")
    void processVisitValidationErrorTest() throws Exception {
        ProcessVisitDto request = new ProcessVisitDto();

        mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Validation failed for: ProcessVisitDto(cardNumber=null)\"}"))
                .andDo(print());
    }

    @Test
    void processVisitUnauthorizedTest() throws Exception {
        ProcessVisitDto request = new ProcessVisitDto(CARD_NUMBER);

        mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    private String getFileContent(String path) throws Exception {
        URI fileUri = getClass().getResource(path).toURI();
        List<String> strings = Files.readAllLines(Paths.get(fileUri));
        return String.join("", strings);
    }
}
