package com.shdv09.webapplication.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shdv09.webapplication.dto.response.TimeslotDto;
import com.shdv09.webapplication.dto.response.TrainerDto;
import com.shdv09.webapplication.dto.response.TrainerScheduleDto;
import com.shdv09.webapplication.feign.AppointmentServiceProxy;
import feign.FeignException;
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
import static org.mockito.Mockito.mock;
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
    private static final String WORKOUT_DATE = "2024-07-26";
    private static final String TRAINER_DTO_LIST_JSON_PATH = "/controller/trainer/trainerList.json";
    private static final String TRAINER_DTO_JSON_PATH = "/controller/trainer/trainer.json";
    private static final String TIMESLOTS_DTO_JSON_PATH = "/controller/trainer/timeslots.json";
    private static final String SCHEDULE_DTO_REF_JSON_PATH = "/controller/trainer/schedule.json";

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
    void getAllTrainersPositiveTest() throws Exception {
        List<TrainerDto> trainers = mapper.readValue(getFileContent(TRAINER_DTO_LIST_JSON_PATH), new TypeReference<>() {
        });
        when(appointmentServiceProxy.getAllTrainers()).thenReturn(trainers);

        MvcResult result = mockMvc.perform(get("/api/trainers"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<TrainerDto> response = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertAll(
                () -> assertEquals(trainers, response),
                () -> verify(appointmentServiceProxy).getAllTrainers()
        );
    }

    @Test
    void getAllTrainersErrorTest() throws Exception {
        FeignException ex = mock(FeignException.class);
        when(ex.getMessage()).thenReturn("Server error");
        when(appointmentServiceProxy.getAllTrainers()).thenThrow(ex);

        mockMvc.perform(get("/api/trainers"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"error\":\"Server error\"}"))
                .andDo(print());

        assertAll(
                () -> verify(appointmentServiceProxy).getAllTrainers()
        );
    }

    @Test
    void findTrainerPositiveTest() throws Exception {
        TrainerDto trainer = mapper.readValue(getFileContent(TRAINER_DTO_JSON_PATH), TrainerDto.class);
        when(appointmentServiceProxy.findTrainer(anyLong())).thenReturn(trainer);

        MvcResult result = mockMvc.perform(get("/api/trainers/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        TrainerDto response = mapper.readValue(result.getResponse().getContentAsString(), TrainerDto.class);
        assertAll(
                () -> assertEquals(trainer, response),
                () -> verify(appointmentServiceProxy).findTrainer(TRAINER_ID)
        );
    }

    @Test
    void findTrainerErrorTest() throws Exception {
        FeignException ex = mock(FeignException.class);
        when(ex.status()).thenReturn(404);
        when(ex.getMessage()).thenReturn("Trainer not found");
        when(appointmentServiceProxy.findTrainer(anyLong())).thenThrow(ex);

        mockMvc.perform(get("/api/trainers/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Trainer not found\"}"))
                .andDo(print())
                .andReturn();

        assertAll(
                () -> verify(appointmentServiceProxy).findTrainer(TRAINER_ID)
        );
    }

    @Test
    void getTrainersSchedulePositiveTest() throws Exception {
        List<TimeslotDto> timeslots = mapper.readValue(getFileContent(TIMESLOTS_DTO_JSON_PATH), new TypeReference<>() {
        });
        when(appointmentServiceProxy.getBusyTimeslots(anyLong(), any(LocalDate.class))).thenReturn(timeslots);

        MvcResult result = mockMvc.perform(get("/api/trainers/1/schedule")
                        .param("id", TRAINER_ID.toString())
                        .param("date", WORKOUT_DATE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        TrainerScheduleDto response = mapper.readValue(result.getResponse().getContentAsString(), TrainerScheduleDto.class);
        TrainerScheduleDto scheduleReference = mapper.readValue(getFileContent(SCHEDULE_DTO_REF_JSON_PATH), TrainerScheduleDto.class);
        assertAll(
                () -> assertEquals(scheduleReference, response),
                () -> verify(appointmentServiceProxy).getBusyTimeslots(TRAINER_ID, LocalDate.parse(WORKOUT_DATE))
        );
    }

    @Test
    void getTrainersScheduleErrorTest() throws Exception {
        FeignException ex = mock(FeignException.class);
        when(ex.status()).thenReturn(404);
        when(ex.getMessage()).thenReturn("Trainer not found");
        when(appointmentServiceProxy.getBusyTimeslots(anyLong(), any(LocalDate.class))).thenThrow(ex);

        mockMvc.perform(get("/api/trainers/1/schedule")
                        .param("id", TRAINER_ID.toString())
                        .param("date", WORKOUT_DATE))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Trainer not found\"}"))
                .andDo(print());

        assertAll(
                () -> verify(appointmentServiceProxy).getBusyTimeslots(TRAINER_ID, LocalDate.parse(WORKOUT_DATE))
        );
    }

    private String getFileContent(String path) throws Exception {
        URI fileUri = getClass().getResource(path).toURI();
        List<String> strings = Files.readAllLines(Paths.get(fileUri));
        return String.join("", strings);
    }
}
