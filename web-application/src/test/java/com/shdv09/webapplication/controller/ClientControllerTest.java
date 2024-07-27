package com.shdv09.webapplication.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shdv09.webapplication.dto.request.AddClientDto;
import com.shdv09.webapplication.dto.request.BindCardRequest;
import com.shdv09.webapplication.dto.request.UpdateClientDto;
import com.shdv09.webapplication.dto.response.ClientDto;
import com.shdv09.webapplication.feign.ClientServiceProxy;
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
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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
    private static final String FIRST_NAME = "Ivan";
    private static final String LAST_NAME = "Ivanov";
    private static final String DATE_OF_BIRTH = "2007-12-03";
    private static final String CLIENT_DTO_JSON_PATH = "/controller/client/clientDto.json";
    private static final String CLIENT_DTO_LIST_JSON_PATH = "/controller/client/clientDtoList.json";
    private static final String ADD_CLIENT_DTO_JSON_PATH = "/controller/client/addClientDto.json";
    private static final String UPDATE_CLIENT_DTO_JSON_PATH = "/controller/client/updateClientDto.json";
    private static final String BIND_CARD_REQUEST_JSON_PATH = "/controller/client/bindCardRequest.json";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientServiceProxy clientServiceProxy;

    @BeforeEach
    void init() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(clientServiceProxy);
    }

    @Test
    @WithMockUser(username = "user")
    void findClientPositiveTest() throws Exception {
        ClientDto client = mapper.readValue(getFileContent(CLIENT_DTO_JSON_PATH), ClientDto.class);
        when(clientServiceProxy.findClient(anyLong())).thenReturn(client);

        MvcResult result = mockMvc.perform(get("/api/clients/2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ClientDto response = mapper.readValue(result.getResponse().getContentAsString(), ClientDto.class);
        assertAll(
                () -> assertEquals(client, response),
                () -> verify(clientServiceProxy).findClient(CLIENT_ID)
        );
    }

    @Test
    @WithMockUser(username = "user")
    void findClientErrorTest() throws Exception {
        FeignException ex = mock(FeignException.class);
        when(ex.status()).thenReturn(404);
        when(ex.getMessage()).thenReturn("Client not found");
        when(clientServiceProxy.findClient(anyLong())).thenThrow(ex);

        mockMvc.perform(get("/api/clients/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Client not found\"}"))
                .andDo(print());

        assertAll(
                () -> verify(clientServiceProxy).findClient(CLIENT_ID)
        );
    }

    @Test
    @WithMockUser(username = "user")
    void findClientsPositiveTest() throws Exception {
        List<ClientDto> clients = mapper.readValue(getFileContent(CLIENT_DTO_LIST_JSON_PATH), new TypeReference<>() {});
        when(clientServiceProxy.findClients(anyString(), anyString(), any(LocalDate.class))).thenReturn(clients);

        MvcResult result = mockMvc.perform(get("/api/clients")
                        .param("firstName", FIRST_NAME)
                        .param("lastName", LAST_NAME)
                        .param("birthDate", DATE_OF_BIRTH))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<ClientDto> response = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertAll(
                () -> assertEquals(clients, response),
                () -> verify(clientServiceProxy).findClients(FIRST_NAME, LAST_NAME, LocalDate.parse(DATE_OF_BIRTH))
        );
    }

    @Test
    @WithMockUser(username = "user")
    void findClientsErrorTest() throws Exception {
        FeignException ex = mock(FeignException.class);
        when(ex.getMessage()).thenReturn("Server error");
        when(clientServiceProxy.findClients(anyString(), anyString(), any(LocalDate.class))).thenThrow(ex);

        mockMvc.perform(get("/api/clients")
                        .param("firstName", FIRST_NAME)
                        .param("lastName", LAST_NAME)
                        .param("birthDate", DATE_OF_BIRTH))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"error\":\"Server error\"}"))
                .andDo(print());

        assertAll(
                () -> verify(clientServiceProxy).findClients(FIRST_NAME, LAST_NAME, LocalDate.parse(DATE_OF_BIRTH))
        );
    }

    @Test
    void findClientsUnauthorizedTest() throws Exception {
        mockMvc.perform(get("/api/clients")
                        .param("firstName", FIRST_NAME)
                        .param("lastName", LAST_NAME)
                        .param("birthDate", DATE_OF_BIRTH))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ROLE_ADMIN"})
    void addClientPositiveTest() throws Exception {
        ClientDto client = mapper.readValue(getFileContent(CLIENT_DTO_JSON_PATH), ClientDto.class);
        when(clientServiceProxy.addClient(any(AddClientDto.class))).thenReturn(client);
        AddClientDto request = mapper.readValue(getFileContent(ADD_CLIENT_DTO_JSON_PATH), AddClientDto.class);

        MvcResult result = mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ClientDto response = mapper.readValue(result.getResponse().getContentAsString(), ClientDto.class);
        assertAll(
                () -> assertEquals(client, response),
                () -> verify(clientServiceProxy).addClient(request)
        );
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ROLE_ADMIN"})
    void addClientErrorTest() throws Exception {
        FeignException ex = mock(FeignException.class);
        when(ex.getMessage()).thenReturn("Server error");
        when(clientServiceProxy.addClient(any(AddClientDto.class))).thenThrow(ex);
        AddClientDto request = mapper.readValue(getFileContent(ADD_CLIENT_DTO_JSON_PATH), AddClientDto.class);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"error\":\"Server error\"}"))
                .andDo(print());

        assertAll(
                () -> verify(clientServiceProxy).addClient(request)
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
                .andExpect(content().json("{\"error\":\"Validation failed for: AddClientDto(firstName=null, lastName=null, gender=null, birthDate=null)\"}"))
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
        ClientDto client = mapper.readValue(getFileContent(CLIENT_DTO_JSON_PATH), ClientDto.class);
        when(clientServiceProxy.updateClient(any(UpdateClientDto.class))).thenReturn(client);
        UpdateClientDto request = mapper.readValue(getFileContent(UPDATE_CLIENT_DTO_JSON_PATH), UpdateClientDto.class);

        MvcResult result = mockMvc.perform(put("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ClientDto response = mapper.readValue(result.getResponse().getContentAsString(), ClientDto.class);
        assertAll(
                () -> assertEquals(client, response),
                () -> verify(clientServiceProxy).updateClient(request)
        );
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ROLE_ADMIN"})
    void updateClientErrorTest() throws Exception {
        FeignException ex = mock(FeignException.class);
        when(ex.getMessage()).thenReturn("Server error");
        when(clientServiceProxy.updateClient(any(UpdateClientDto.class))).thenThrow(ex);
        UpdateClientDto request = mapper.readValue(getFileContent(UPDATE_CLIENT_DTO_JSON_PATH), UpdateClientDto.class);

        mockMvc.perform(put("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"error\":\"Server error\"}"))
                .andDo(print());

        assertAll(
                () -> verify(clientServiceProxy).updateClient(request)
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
                .andExpect(content().json("{\"error\":\"Validation failed for: UpdateClientDto(id=null, firstName=null, lastName=null, gender=null, birthDate=null)\"}"))
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
        ClientDto client = mapper.readValue(getFileContent(CLIENT_DTO_JSON_PATH), ClientDto.class);
        when(clientServiceProxy.bindCardToClient(any(BindCardRequest.class))).thenReturn(client);
        BindCardRequest request = mapper.readValue(getFileContent(BIND_CARD_REQUEST_JSON_PATH), BindCardRequest.class);

        MvcResult result = mockMvc.perform(put("/api/clients/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ClientDto response = mapper.readValue(result.getResponse().getContentAsString(), ClientDto.class);
        assertAll(
                () -> assertEquals(client, response),
                () -> verify(clientServiceProxy).bindCardToClient(request)
        );
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ROLE_ADMIN"})
    void bindCardWithClientErrorTest() throws Exception {
        FeignException ex = mock(FeignException.class);
        when(ex.status()).thenReturn(404);
        when(ex.getMessage()).thenReturn("Client not found");
        when(clientServiceProxy.bindCardToClient(any(BindCardRequest.class))).thenThrow(ex);
        BindCardRequest request = mapper.readValue(getFileContent(BIND_CARD_REQUEST_JSON_PATH), BindCardRequest.class);

        mockMvc.perform(put("/api/clients/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Client not found\"}"))
                .andDo(print());

        assertAll(
                () -> verify(clientServiceProxy).bindCardToClient(request)
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
                .andExpect(content().json("{\"error\":\"Validation failed for: BindCardRequest(clientId=null, cardId=null)\"}"))
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
