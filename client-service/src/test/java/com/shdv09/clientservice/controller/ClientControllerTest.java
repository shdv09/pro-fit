package com.shdv09.clientservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shdv09.clientservice.dto.response.ClientDto;
import com.shdv09.clientservice.model.Client;
import com.shdv09.clientservice.model.ClubCard;
import com.shdv09.clientservice.repository.ClientRepository;
import com.shdv09.clientservice.repository.ClubCardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {
    private static final Long CLIENT_ID = 2L;
    private static final String CLUB_CARD_NUMBER = "55555";
    private static final String CLIENT_JSON_PATH = "/controller/client/client.json";
    private static final String CLUB_CARD_JSON_PATH = "/controller/client/clubCard.json";
    private static final String CLIENT_DTO_REFERENCE_JSON_PATH = "/controller/client/clientDtoReference.json";
    private static final String CLIENT_DTO_NULL_CARD_REFERENCE_JSON_PATH = "/controller/client/clientDtoNullCardReference.json";


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private ClubCardRepository clubCardRepository;

    @BeforeEach
    void init() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(clientRepository, clubCardRepository);
    }

    @Test
    void findClientPositiveTest() throws Exception {
        Client client = mapper.readValue(getFileContent(CLIENT_JSON_PATH), Client.class);
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        ClubCard clubCard = mapper.readValue(getFileContent(CLUB_CARD_JSON_PATH), ClubCard.class);
        when(clubCardRepository.findClubCardByClientId(anyLong())).thenReturn(Optional.of(clubCard));

        MvcResult result = mockMvc.perform(get("/api/clients/2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ClientDto response = mapper.readValue(result.getResponse().getContentAsString(), ClientDto.class);
        ClientDto reference = mapper.readValue(getFileContent(CLIENT_DTO_REFERENCE_JSON_PATH), ClientDto.class);

        assertAll(
                () -> assertEquals(reference, response),
                () -> verify(clientRepository).findById(CLIENT_ID),
                () -> verify(clubCardRepository).findClubCardByClientId(CLIENT_ID)
        );
    }

    @Test
    void clientNotFoundTest() throws Exception {
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Client with id = 2 not found\"}"))
                .andDo(print());

        assertAll(
                () -> verify(clientRepository).findById(CLIENT_ID)
        );
    }

    @Test
    void findClientCardNotFoundTest() throws Exception {
        Client client = mapper.readValue(getFileContent(CLIENT_JSON_PATH), Client.class);
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(clubCardRepository.findClubCardByClientId(anyLong())).thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(get("/api/clients/2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ClientDto response = mapper.readValue(result.getResponse().getContentAsString(), ClientDto.class);
        ClientDto reference = mapper.readValue(getFileContent(CLIENT_DTO_NULL_CARD_REFERENCE_JSON_PATH), ClientDto.class);

        assertAll(
                () -> assertEquals(reference, response),
                () -> verify(clientRepository).findById(CLIENT_ID),
                () -> verify(clubCardRepository).findClubCardByClientId(CLIENT_ID)
        );
    }

    @Test
    void findClientByCardPositiveTest() throws Exception {
        ClubCard clubCard = mapper.readValue(getFileContent(CLUB_CARD_JSON_PATH), ClubCard.class);
        when(clubCardRepository.findClubCardByNumber(anyString())).thenReturn(Optional.of(clubCard));

        MvcResult result = mockMvc.perform(get("/api/clients/cards/55555"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ClientDto response = mapper.readValue(result.getResponse().getContentAsString(), ClientDto.class);
        ClientDto reference = mapper.readValue(getFileContent(CLIENT_DTO_REFERENCE_JSON_PATH), ClientDto.class);

        assertAll(
                () -> assertEquals(reference, response),
                () -> verify(clubCardRepository).findClubCardByNumber(CLUB_CARD_NUMBER)
        );
    }

    @Test
    void findClientByCardNotFoundTest() throws Exception {
        when(clubCardRepository.findClubCardByNumber(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/cards/55555"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Club card with number = 55555 not found\"}"))
                .andDo(print());

        assertAll(
                () -> verify(clubCardRepository).findClubCardByNumber(CLUB_CARD_NUMBER)
        );
    }

    private String getFileContent(String path) throws Exception {
        URI fileUri = getClass().getResource(path).toURI();
        List<String> strings = Files.readAllLines(Paths.get(fileUri));
        return String.join("", strings);
    }
}
