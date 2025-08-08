package com.shdv09.webapplication.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shdv09.webapplication.dto.request.CreateAppointmentDto;
import com.shdv09.webapplication.dto.response.AppointmentDto;
import com.shdv09.webapplication.feign.AppointmentServiceProxy;
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
class AppointmentControllerTest {
    private static final String APPOINTMENT_DTO_JSON_PATH = "/controller/appointment/appointmentDto.json";
    private static final String CREATE_APPOINTMENT_DTO_JSON_PATH = "/controller/appointment/createAppointmentDto.json";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentServiceProxy appointmentServiceProxy;

    @BeforeEach
    void init() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(appointmentServiceProxy);
    }

    @Test
    @WithMockUser(username = "user")
    void createAppointmentPositiveTest() throws Exception {
        AppointmentDto appointment = mapper.readValue(getFileContent(APPOINTMENT_DTO_JSON_PATH), AppointmentDto.class);
        when(appointmentServiceProxy.createAppointment(any(CreateAppointmentDto.class))).thenReturn(appointment);
        CreateAppointmentDto request = mapper.readValue(getFileContent(CREATE_APPOINTMENT_DTO_JSON_PATH), CreateAppointmentDto.class);

        MvcResult result = mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        AppointmentDto response = mapper.readValue(result.getResponse().getContentAsString(), AppointmentDto.class);
        assertAll(
                () -> assertEquals(appointment, response),
                () -> verify(appointmentServiceProxy).createAppointment(request)
        );
    }

    @Test
    @WithMockUser(username = "user")
    void createAppointmentErrorTest() throws Exception {
        FeignException ex = mock(FeignException.class);
        when(ex.status()).thenReturn(404);
        when(ex.getMessage()).thenReturn("Trainer not found");
        when(appointmentServiceProxy.createAppointment(any(CreateAppointmentDto.class))).thenThrow(ex);
        CreateAppointmentDto request = mapper.readValue(getFileContent(CREATE_APPOINTMENT_DTO_JSON_PATH), CreateAppointmentDto.class);

        mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Trainer not found\"}"))
                .andDo(print());

        assertAll(
                () -> verify(appointmentServiceProxy).createAppointment(request)
        );
    }

    @Test
    @WithMockUser(username = "user")
    void createAppointmentValidationErrorTest() throws Exception {
        CreateAppointmentDto request = new CreateAppointmentDto();

        mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Validation failed for: clientId, date, hour, trainerId\"}"))
                .andDo(print());
    }

    @Test
    void createAppointmentUnauthorizedTest() throws Exception {
        CreateAppointmentDto request = mapper.readValue(getFileContent(CREATE_APPOINTMENT_DTO_JSON_PATH), CreateAppointmentDto.class);

        mockMvc.perform(post("/api/appointment")
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
