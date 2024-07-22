package com.shdv09.appointmentservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shdv09.appointmentservice.dto.request.CreateAppointmentDto;
import com.shdv09.appointmentservice.dto.response.AppointmentDto;
import com.shdv09.appointmentservice.exception.RequestValidationException;
import com.shdv09.appointmentservice.model.TimeSlot;
import com.shdv09.appointmentservice.model.Trainer;
import com.shdv09.appointmentservice.repository.TimeSlotRepository;
import com.shdv09.appointmentservice.repository.TrainerRepository;
import com.shdv09.appointmentservice.validation.CreateAppointmentValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
    private static final Long TRAINER_ID = 1L;
    private static final Long CLIENT_ID = 2L;
    private static final LocalDate WORKOUT_DATE = LocalDate.of(2024, 7, 21);
    private static final String TRAINER_JSON_PATH = "/controller/appointment/trainer.json";
    private static final String TIMESLOT_JSON_PATH = "/controller/appointment/timeslot.json";
    private static final String APPOINTMENT_DTO_JSON_PATH = "/controller/appointment/appointmentDto.json";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeSlotRepository timeSlotRepository;

    @MockBean
    private TrainerRepository trainerRepository;

    @MockBean
    private CreateAppointmentValidator createAppointmentValidator;

    @BeforeEach
    void init() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(timeSlotRepository, trainerRepository, createAppointmentValidator);
    }

    @Test
    void createAppointmentPositiveTest() throws Exception {
        doNothing().when(createAppointmentValidator).validateRequest(any());
        Trainer trainer = mapper.readValue(getFileContent(TRAINER_JSON_PATH), Trainer.class);
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.of(trainer));
        when(timeSlotRepository.findById(any())).thenReturn(Optional.empty());
        TimeSlot timeSlot = mapper.readValue(getFileContent(TIMESLOT_JSON_PATH), TimeSlot.class);
        when(timeSlotRepository.save(any())).thenReturn(timeSlot);
        CreateAppointmentDto request = new CreateAppointmentDto(TRAINER_ID, CLIENT_ID, WORKOUT_DATE, 10);

        MvcResult result = mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        AppointmentDto response = mapper.readValue(result.getResponse().getContentAsString(), AppointmentDto.class);
        AppointmentDto reference = mapper.readValue(getFileContent(APPOINTMENT_DTO_JSON_PATH), AppointmentDto.class);

        ArgumentCaptor<TimeSlot> captor = ArgumentCaptor.forClass(TimeSlot.class);
        assertAll(
                () -> verify(createAppointmentValidator).validateRequest(request),
                () -> assertEquals(reference, response),
                () -> verify(trainerRepository).findById(TRAINER_ID),
                () -> verify(timeSlotRepository).findById(new TimeSlot.TimeSlotPK(TRAINER_ID, WORKOUT_DATE, 10)),
                () -> verify(timeSlotRepository).save(captor.capture()),
                () -> assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(timeSlot)
        );
    }

    @Test
    void createAppointmentTrainerMissingTest() throws Exception {
        doNothing().when(createAppointmentValidator).validateRequest(any());
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.empty());
        CreateAppointmentDto request = new CreateAppointmentDto(TRAINER_ID, CLIENT_ID, LocalDate.of(2024, 7, 21), 10);

        mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(content().json("{\"error\":\"Trainer with id = 1 not found\"}"))
                .andExpect(status().isNotFound())
                .andDo(print());

        assertAll(
                () -> verify(createAppointmentValidator).validateRequest(request),
                () -> verify(trainerRepository).findById(TRAINER_ID)
        );
    }

    @Test
    void createAppointmentTimeSlotBusyTest() throws Exception {
        doNothing().when(createAppointmentValidator).validateRequest(any());
        Trainer trainer = mapper.readValue(getFileContent(TRAINER_JSON_PATH), Trainer.class);
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.of(trainer));
        TimeSlot timeSlot = mapper.readValue(getFileContent(TIMESLOT_JSON_PATH), TimeSlot.class);
        when(timeSlotRepository.findById(any())).thenReturn(Optional.of(timeSlot));
        CreateAppointmentDto request = new CreateAppointmentDto(TRAINER_ID, CLIENT_ID, WORKOUT_DATE, 10);

        mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(content().json("{\"error\":\"Timeslot busy, try another time\"}"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertAll(
                () -> verify(createAppointmentValidator).validateRequest(request),
                () -> verify(trainerRepository).findById(TRAINER_ID),
                () -> verify(timeSlotRepository).findById(new TimeSlot.TimeSlotPK(TRAINER_ID, WORKOUT_DATE, 10))
        );
    }

    @Test
    void createAppointmentDataIntegrityErrorTest() throws Exception {
        doNothing().when(createAppointmentValidator).validateRequest(any());
        Trainer trainer = mapper.readValue(getFileContent(TRAINER_JSON_PATH), Trainer.class);
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.of(trainer));
        when(timeSlotRepository.findById(any())).thenReturn(Optional.empty());
        TimeSlot timeSlot = mapper.readValue(getFileContent(TIMESLOT_JSON_PATH), TimeSlot.class);
        when(timeSlotRepository.save(any())).thenThrow(new DataIntegrityViolationException("record exists"));
        CreateAppointmentDto request = new CreateAppointmentDto(TRAINER_ID, CLIENT_ID, WORKOUT_DATE, 10);

        mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(content().json("{\"error\":\"Timeslot busy, try another time\"}"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        ArgumentCaptor<TimeSlot> captor = ArgumentCaptor.forClass(TimeSlot.class);
        assertAll(
                () -> verify(createAppointmentValidator).validateRequest(request),
                () -> verify(trainerRepository).findById(TRAINER_ID),
                () -> verify(timeSlotRepository).findById(new TimeSlot.TimeSlotPK(TRAINER_ID, WORKOUT_DATE, 10)),
                () -> verify(timeSlotRepository).save(captor.capture()),
                () -> assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(timeSlot)
        );
    }

    @Test
    void createAppointmentValidationTest() throws Exception {
        CreateAppointmentDto request = new CreateAppointmentDto();

        mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(content().json("{\"error\":\"Validation failed for: CreateAppointmentDto(trainerId=null, clientId=null, date=null, hour=null)\"}"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void createAppointmentValidatorTest() throws Exception {
        doThrow(new RequestValidationException("Invalid date, you can't create appointment in the past"))
                .when(createAppointmentValidator).validateRequest(any());
        CreateAppointmentDto request = new CreateAppointmentDto(TRAINER_ID, CLIENT_ID, WORKOUT_DATE, 10);


        mockMvc.perform(post("/api/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(content().json("{\"error\":\"Invalid date, you can't create appointment in the past\"}"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        assertAll(
                () -> verify(createAppointmentValidator).validateRequest(request)
        );
    }

    private String getFileContent(String path) throws Exception {
        URI fileUri = getClass().getResource(path).toURI();
        List<String> strings = Files.readAllLines(Paths.get(fileUri));
        return String.join("", strings);
    }
}
