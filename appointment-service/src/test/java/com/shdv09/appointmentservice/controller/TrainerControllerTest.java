package com.shdv09.appointmentservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shdv09.appointmentservice.dto.response.TimeslotDto;
import com.shdv09.appointmentservice.dto.response.TrainerDto;
import com.shdv09.appointmentservice.model.TimeSlot;
import com.shdv09.appointmentservice.model.Trainer;
import com.shdv09.appointmentservice.repository.TimeSlotRepository;
import com.shdv09.appointmentservice.repository.TrainerRepository;
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
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrainerControllerTest {
    private static final Long TRAINER_ID = 1L;
    private static final LocalDate WORKOUT_DATE = LocalDate.of(2024, 7, 21);
    private static final String TRAINERS_LIST_JSON_PATH = "/controller/trainer/trainers.json";
    private static final String TRAINERS_REFERENCE_JSON_PATH = "/controller/trainer/trainersReference.json";
    private static final String TIMESLOTS_JSON_PATH = "/controller/trainer/timeSlots.json";
    private static final String TIMESLOTS_REFERENCE_JSON_PATH = "/controller/trainer/timeSlotsReference.json";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeSlotRepository timeSlotRepository;

    @MockBean
    private TrainerRepository trainerRepository;

    @BeforeEach
    void init() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(timeSlotRepository, trainerRepository);
    }

    @Test
    void getAllTrainersPositiveTest() throws Exception {
        List<Trainer> trainers = mapper.readValue(getFileContent(TRAINERS_LIST_JSON_PATH), new TypeReference<>() {});
        when(trainerRepository.findAll()).thenReturn(trainers);

        MvcResult result = mockMvc.perform(get("/api/trainers"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<TrainerDto> response = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        List<TrainerDto> reference = mapper.readValue(getFileContent(TRAINERS_REFERENCE_JSON_PATH), new TypeReference<>() {});

        assertAll(
                () -> assertEquals(reference, response),
                () -> verify(trainerRepository).findAll()
        );
    }

    @Test
    void getAllTrainersErrorTest() throws Exception {
        when(trainerRepository.findAll()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/trainers"))
                .andExpect(status().isInternalServerError())
                .andDo(print());

        assertAll(
                () -> verify(trainerRepository).findAll()
        );
    }

    @Test
    void getBusyTimeslotsPositiveTest() throws Exception {
        List<TimeSlot> timeSlots = mapper.readValue(getFileContent(TIMESLOTS_JSON_PATH), new TypeReference<>() {});
        when(timeSlotRepository.findBypKeyWorkoutDateAndTrainerId(any(LocalDate.class), anyLong())).thenReturn(timeSlots);
        when(trainerRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);

        MvcResult result = mockMvc.perform(get("/api/trainers/1/timeslots")
                        .param("date", "2024-07-21"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<TimeslotDto> response = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        List<TimeslotDto> reference = mapper.readValue(getFileContent(TIMESLOTS_REFERENCE_JSON_PATH), new TypeReference<>() {});

        assertAll(
                () -> assertEquals(reference, response),
                () -> verify(trainerRepository).existsById(TRAINER_ID),
                () -> verify(timeSlotRepository).findBypKeyWorkoutDateAndTrainerId(WORKOUT_DATE, TRAINER_ID)
        );
    }

    @Test
    void getBusyTimeslotsTrainerNotFoundTest() throws Exception {
        when(trainerRepository.existsById(anyLong())).thenReturn(Boolean.FALSE);

        mockMvc.perform(get("/api/trainers/1/timeslots")
                        .param("date", "2024-07-21"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Trainer with id=1 not found\"}"))
                .andDo(print())
                .andReturn();

        assertAll(
                () -> verify(trainerRepository).existsById(TRAINER_ID)
        );
    }

    @Test
    void getBusyTimeslotsErrorTest() throws Exception {
        when(trainerRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        when(timeSlotRepository.findBypKeyWorkoutDateAndTrainerId(any(LocalDate.class), anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/trainers/1/timeslots")
                        .param("date", "2024-07-21"))
                .andExpect(status().isInternalServerError())
                .andDo(print());

        assertAll(
                () -> verify(trainerRepository).existsById(TRAINER_ID),
                () -> verify(timeSlotRepository).findBypKeyWorkoutDateAndTrainerId(WORKOUT_DATE, TRAINER_ID)
        );
    }

    private String getFileContent(String path) throws Exception {
        URI fileUri = getClass().getResource(path).toURI();
        List<String> strings = Files.readAllLines(Paths.get(fileUri));
        return String.join("", strings);
    }
}
