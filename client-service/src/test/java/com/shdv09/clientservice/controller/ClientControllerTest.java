package com.shdv09.clientservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shdv09.clientservice.dto.mapper.ClientMapper;
import com.shdv09.clientservice.dto.request.AddClientDto;
import com.shdv09.clientservice.dto.request.BindCardRequest;
import com.shdv09.clientservice.dto.request.UpdateClientDto;
import com.shdv09.clientservice.dto.response.ClientDto;
import com.shdv09.clientservice.model.Client;
import com.shdv09.clientservice.model.ClubCard;
import com.shdv09.clientservice.repository.ClientRepository;
import com.shdv09.clientservice.repository.ClubCardRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private static final String CLUB_CARD_NO_CLIENT_JSON_PATH = "/controller/client/clubCardNoClient.json";
    private static final String CLIENT_DTO_REFERENCE_JSON_PATH = "/controller/client/clientDtoReference.json";
    private static final String CLIENT_DTO_NULL_CARD_REFERENCE_JSON_PATH = "/controller/client/clientDtoNullCardReference.json";
    private static final String ADD_CLIENT_DTO_JSON_PATH = "/controller/client/addClientDto.json";
    private static final String UPDATE_CLIENT_DTO_JSON_PATH = "/controller/client/updateClientDto.json";
    private static final String BIND_CARD_REQUEST_JSON_PATH = "/controller/client/bindCardRequest.json";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientMapper clientMapper;

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
    @WithMockUser(username = "user")
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
    @WithMockUser(username = "user")
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
    @WithMockUser(username = "user")
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
    void findClientUnauthorizedTest() throws Exception {
        mockMvc.perform(get("/api/clients/2"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user")
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
    @WithMockUser(username = "user")
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

    @Test
    void findClientByCardUnauthorizedTest() throws Exception {
        mockMvc.perform(get("/api/clients/cards/55555"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ROLE_ADMIN"})
    void addClientPositiveTest() throws Exception {
        Client client = mapper.readValue(getFileContent(CLIENT_JSON_PATH), Client.class);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        AddClientDto request = mapper.readValue(getFileContent(ADD_CLIENT_DTO_JSON_PATH), AddClientDto.class);

        MvcResult result = mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ClientDto response = mapper.readValue(result.getResponse().getContentAsString(), ClientDto.class);
        Client clientToSave = clientMapper.fromDto(request);
        ClientDto reference = mapper.readValue(getFileContent(CLIENT_DTO_NULL_CARD_REFERENCE_JSON_PATH), ClientDto.class);
        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        assertAll(
                () -> assertEquals(reference, response),
                () -> verify(clientRepository).save(captor.capture()),
                () -> assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(clientToSave)

        );
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ROLE_ADMIN"})
    void addClientErrorTest() throws Exception {
        when(clientRepository.save(any(Client.class))).thenThrow(new RuntimeException());
        AddClientDto request = mapper.readValue(getFileContent(ADD_CLIENT_DTO_JSON_PATH), AddClientDto.class);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"error\":null}"))
                .andDo(print());

        Client clientToSave = clientMapper.fromDto(request);
        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        assertAll(
                () -> verify(clientRepository).save(captor.capture()),
                () -> assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(clientToSave)
        );
    }

    @Test
    @WithMockUser(username = "user", authorities = {"SCOPE_ROLE_ADMIN"})
    void addClientValidationErrorTest() throws Exception {
        AddClientDto request = new AddClientDto();

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Validation failed for: birthDate, firstName, gender, lastName\"}"))
                .andDo(print());
    }

    @Test
    void addClientUnauthorizedTest() throws Exception {
        AddClientDto request = mapper.readValue(getFileContent(ADD_CLIENT_DTO_JSON_PATH), AddClientDto.class);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user")
    void addClientForbiddenTest() throws Exception {
        AddClientDto request = mapper.readValue(getFileContent(ADD_CLIENT_DTO_JSON_PATH), AddClientDto.class);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().json("{\"error\":\"Access Denied\"}"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ROLE_ADMIN"})
    void updateClientPositiveTest() throws Exception {
        Client client = mapper.readValue(getFileContent(CLIENT_JSON_PATH), Client.class);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        UpdateClientDto request = mapper.readValue(getFileContent(UPDATE_CLIENT_DTO_JSON_PATH), UpdateClientDto.class);

        MvcResult result = mockMvc.perform(put("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ClientDto response = mapper.readValue(result.getResponse().getContentAsString(), ClientDto.class);
        Client clientToSave = clientMapper.fromDto(request);
        ClientDto reference = mapper.readValue(getFileContent(CLIENT_DTO_NULL_CARD_REFERENCE_JSON_PATH), ClientDto.class);
        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        assertAll(
                () -> assertEquals(reference, response),
                () -> verify(clientRepository).findById(request.getId()),
                () -> verify(clientRepository).save(captor.capture()),
                () -> assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(clientToSave)
        );
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ROLE_ADMIN"})
    void updateClientErrorTest() throws Exception {
        Client client = mapper.readValue(getFileContent(CLIENT_JSON_PATH), Client.class);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenThrow(new RuntimeException());
        UpdateClientDto request = mapper.readValue(getFileContent(UPDATE_CLIENT_DTO_JSON_PATH), UpdateClientDto.class);

        mockMvc.perform(put("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"error\":null}"))
                .andDo(print());

        Client clientToSave = clientMapper.fromDto(request);
        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        assertAll(
                () -> verify(clientRepository).findById(request.getId()),
                () -> verify(clientRepository).save(captor.capture()),
                () -> assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(clientToSave)

        );
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ROLE_ADMIN"})
    void updateClientValidationErrorTest() throws Exception {
        UpdateClientDto request = new UpdateClientDto();

        mockMvc.perform(put("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Validation failed for: birthDate, firstName, gender, id, lastName\"}"))
                .andDo(print());
    }

    @Test
    void updateClientUnauthorizedTest() throws Exception {
        UpdateClientDto request = mapper.readValue(getFileContent(UPDATE_CLIENT_DTO_JSON_PATH), UpdateClientDto.class);

        mockMvc.perform(put("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user")
    void updateClientForbiddenTest() throws Exception {
        UpdateClientDto request = mapper.readValue(getFileContent(UPDATE_CLIENT_DTO_JSON_PATH), UpdateClientDto.class);

        mockMvc.perform(put("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().json("{\"error\":\"Access Denied\"}"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ROLE_ADMIN"})
    void bindCardWithClientPositiveTest() throws Exception {
        Client client = mapper.readValue(getFileContent(CLIENT_JSON_PATH), Client.class);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        ClubCard clubCard = mapper.readValue(getFileContent(CLUB_CARD_NO_CLIENT_JSON_PATH), ClubCard.class);
        when(clubCardRepository.findById(anyLong())).thenReturn(Optional.of(clubCard));
        ClubCard savedCard = mapper.readValue(getFileContent(CLUB_CARD_JSON_PATH), ClubCard.class);
        when(clubCardRepository.save(any())).thenReturn(savedCard);
        BindCardRequest request = mapper.readValue(getFileContent(BIND_CARD_REQUEST_JSON_PATH), BindCardRequest.class);

        MvcResult result = mockMvc.perform(put("/api/clients/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ClientDto response = mapper.readValue(result.getResponse().getContentAsString(), ClientDto.class);
        ClientDto reference = mapper.readValue(getFileContent(CLIENT_DTO_REFERENCE_JSON_PATH), ClientDto.class);
        clubCard.setClient(client);
        assertAll(
                () -> assertEquals(reference, response),
                () -> verify(clientRepository).findById(request.getClientId()),
                () -> verify(clubCardRepository).findById(request.getCardId()),
                () -> verify(clubCardRepository).save(clubCard)

        );
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ROLE_ADMIN"})
    void bindCardWithClientErrorTest() throws Exception {
        Client client = mapper.readValue(getFileContent(CLIENT_JSON_PATH), Client.class);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        ClubCard clubCard = mapper.readValue(getFileContent(CLUB_CARD_NO_CLIENT_JSON_PATH), ClubCard.class);
        when(clubCardRepository.findById(anyLong())).thenReturn(Optional.of(clubCard));
        when(clubCardRepository.save(any())).thenThrow(new RuntimeException());
        BindCardRequest request = mapper.readValue(getFileContent(BIND_CARD_REQUEST_JSON_PATH), BindCardRequest.class);

        mockMvc.perform(put("/api/clients/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"error\":null}"))
                .andDo(print());

        clubCard.setClient(client);
        assertAll(
                () -> verify(clientRepository).findById(request.getClientId()),
                () -> verify(clubCardRepository).findById(request.getCardId()),
                () -> verify(clubCardRepository).save(clubCard)

        );
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ROLE_ADMIN"})
    void bindCardWithClientValidationErrorTest() throws Exception {
        BindCardRequest request = new BindCardRequest();

        mockMvc.perform(put("/api/clients/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Validation failed for: cardId, clientId\"}"))
                .andDo(print());
    }

    @Test
    void bindCardWithClientUnauthorizedTest() throws Exception {
        BindCardRequest request = mapper.readValue(getFileContent(BIND_CARD_REQUEST_JSON_PATH), BindCardRequest.class);

        mockMvc.perform(put("/api/clients/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin")
    void bindCardWithClientForbiddenTest() throws Exception {
        BindCardRequest request = mapper.readValue(getFileContent(BIND_CARD_REQUEST_JSON_PATH), BindCardRequest.class);

        mockMvc.perform(put("/api/clients/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().json("{\"error\":\"Access Denied\"}"))
                .andDo(print());
    }

    private String getFileContent(String path) throws Exception {
        URI fileUri = getClass().getResource(path).toURI();
        List<String> strings = Files.readAllLines(Paths.get(fileUri));
        return String.join("", strings);
    }
}
