package com.shdv09.visitservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shdv09.visitservice.dto.request.ProcessVisitDto;
import com.shdv09.visitservice.dto.response.VisitDto;
import com.shdv09.visitservice.dto.response.clientservice.ClientDto;
import com.shdv09.visitservice.dto.response.clientservice.ClubCardDto;
import com.shdv09.visitservice.exception.AccessDeniedException;
import com.shdv09.visitservice.feign.ClientServiceProxy;
import com.shdv09.visitservice.model.Visit;
import com.shdv09.visitservice.repository.VisitRepository;
import com.shdv09.visitservice.service.DateFactory;
import com.shdv09.visitservice.validation.CardValidator;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
    private static final Long CLIENT_ID = 2L;
    private static final String CARD_NUMBER = "55555";
    private static final String CLIENT_DTO_JSON_PATH = "/controller/visit/clientDto.json";
    private static final String NEW_VISIT_JSON_PATH = "/controller/visit/newVisit.json";
    private static final String SAVED_VISIT_JSON_PATH = "/controller/visit/savedVisit.json";
    private static final String UPDATED_VISIT_JSON_PATH = "/controller/visit/updatedVisit.json";
    private static final String NEW_VISIT_DTO_REFERENCE_JSON_PATH = "/controller/visit/newVisitDtoReference.json";
    private static final String EXIST_VISIT_DTO_REFERENCE_JSON_PATH = "/controller/visit/existVisitDtoReference.json";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitRepository visitRepository;

    @MockBean
    private ClientServiceProxy clientServiceProxy;

    @MockBean
    private DateFactory dateFactory;

    @MockBean
    private CardValidator cardValidator;

    @BeforeEach
    void init() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(visitRepository, clientServiceProxy, dateFactory, cardValidator);
    }

    @Test
    @WithMockUser(username = "user")
    void processNewVisitTest() throws Exception {
        doNothing().when(cardValidator).validateCard(any(ClubCardDto.class));
        when(dateFactory.generateDate()).thenReturn(LocalDateTime.of(2024, 7, 22, 12, 0 ,0));
        ClientDto client = mapper.readValue(getFileContent(CLIENT_DTO_JSON_PATH), ClientDto.class);
        when(clientServiceProxy.findClientByCardNumber(anyString())).thenReturn(client);
        when(visitRepository.findFirstByClientIdOrderByStartTimeDesc(anyLong())).thenReturn(null);
        Visit savedVisit = mapper.readValue(getFileContent(SAVED_VISIT_JSON_PATH), Visit.class);
        when(visitRepository.save(any(Visit.class))).thenReturn(savedVisit);
        ProcessVisitDto request = new ProcessVisitDto(CARD_NUMBER);

        MvcResult result = mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        VisitDto response = mapper.readValue(result.getResponse().getContentAsString(), VisitDto.class);
        VisitDto reference = mapper.readValue(getFileContent(NEW_VISIT_DTO_REFERENCE_JSON_PATH), VisitDto.class);

        ArgumentCaptor<Visit> captor = ArgumentCaptor.forClass(Visit.class);
        Visit newVisit = mapper.readValue(getFileContent(NEW_VISIT_JSON_PATH), Visit.class);
        assertAll(
                () -> assertEquals(reference, response),
                () -> verify(clientServiceProxy).findClientByCardNumber(CARD_NUMBER),
                () -> verify(cardValidator).validateCard(client.getCard()),
                () -> verify(visitRepository).findFirstByClientIdOrderByStartTimeDesc(CLIENT_ID),
                () -> verify(dateFactory).generateDate(),
                () -> verify(visitRepository).save(captor.capture()),
                () -> assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(newVisit)
        );
    }

    @Test
    @WithMockUser(username = "user")
    void processExistedVisitTest() throws Exception {
        doNothing().when(cardValidator).validateCard(any(ClubCardDto.class));
        when(dateFactory.generateDate()).thenReturn(LocalDateTime.of(2024, 7, 23, 8, 0 ,0));
        ClientDto client = mapper.readValue(getFileContent(CLIENT_DTO_JSON_PATH), ClientDto.class);
        when(clientServiceProxy.findClientByCardNumber(anyString())).thenReturn(client);
        Visit savedVisit = mapper.readValue(getFileContent(SAVED_VISIT_JSON_PATH), Visit.class);
        when(visitRepository.findFirstByClientIdOrderByStartTimeDesc(anyLong())).thenReturn(savedVisit);
        Visit updatedVisit = mapper.readValue(getFileContent(UPDATED_VISIT_JSON_PATH), Visit.class);
        when(visitRepository.save(any(Visit.class))).thenReturn(updatedVisit);
        ProcessVisitDto request = new ProcessVisitDto(CARD_NUMBER);

        MvcResult result = mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        VisitDto response = mapper.readValue(result.getResponse().getContentAsString(), VisitDto.class);
        VisitDto reference = mapper.readValue(getFileContent(EXIST_VISIT_DTO_REFERENCE_JSON_PATH), VisitDto.class);

        ArgumentCaptor<Visit> captor = ArgumentCaptor.forClass(Visit.class);
        assertAll(
                () -> assertEquals(reference, response),
                () -> verify(clientServiceProxy).findClientByCardNumber(CARD_NUMBER),
                () -> verify(cardValidator).validateCard(client.getCard()),
                () -> verify(visitRepository).findFirstByClientIdOrderByStartTimeDesc(CLIENT_ID),
                () -> verify(dateFactory).generateDate(),
                () -> verify(visitRepository).save(captor.capture()),
                () -> assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(updatedVisit)
        );
    }

    @Test
    @WithMockUser(username = "user")
    void processVisitCardNotFoundTest() throws Exception {
        doNothing().when(cardValidator).validateCard(any(ClubCardDto.class));
        when(dateFactory.generateDate()).thenReturn(LocalDateTime.of(2024, 7, 22, 12, 0 ,0));
        FeignException ex = mock(FeignException.class);
        when(ex.status()).thenReturn(404);
        when(ex.getMessage()).thenReturn("Card not found");
        when(clientServiceProxy.findClientByCardNumber(anyString())).thenThrow(ex);
        ProcessVisitDto request = new ProcessVisitDto(CARD_NUMBER);

        mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andDo(print());

        assertAll(
                () -> verify(clientServiceProxy).findClientByCardNumber(CARD_NUMBER)
        );
    }

    @Test
    @WithMockUser(username = "user")
    void processVisitCardValidationErrorTest() throws Exception {
        doThrow(new AccessDeniedException("Access to club denied for card 55555, invalid hours"))
                .when(cardValidator).validateCard(any(ClubCardDto.class));
        ClientDto client = mapper.readValue(getFileContent(CLIENT_DTO_JSON_PATH), ClientDto.class);
        when(clientServiceProxy.findClientByCardNumber(anyString())).thenReturn(client);
        ProcessVisitDto request = new ProcessVisitDto(CARD_NUMBER);

        mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().json("{\"error\":\"Access to club denied for card 55555, invalid hours\"}"))
                .andDo(print());

        assertAll(
                () -> verify(clientServiceProxy).findClientByCardNumber(CARD_NUMBER),
                () -> verify(cardValidator).validateCard(client.getCard())
        );
    }

    @Test
    @WithMockUser(username = "user")
    void processVisitBadRequestErrorTest() throws Exception {
        ProcessVisitDto request = new ProcessVisitDto();

        mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Validation failed for: cardNumber\"}"))
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
