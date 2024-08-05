package com.shdv09.appointmentservice.service;

import com.shdv09.appointmentservice.dto.request.CreateAppointmentDto;
import com.shdv09.appointmentservice.exception.DbLockException;
import com.shdv09.appointmentservice.validation.CreateAppointmentValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.integration.support.locks.LockRegistry;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AppointmentServiceTest {
    private static final Long TRAINER_ID = 1L;
    private static final Long CLIENT_ID = 2L;
    private static final LocalDate WORKOUT_DATE = LocalDate.of(2024, 7, 21);

    @MockBean
    private CreateAppointmentValidator createAppointmentValidator;

    @SpyBean
    private AppointmentService appointmentService;

    @MockBean
    private LockRegistry lockRegistry;

    @Test
    void testLock() {
        Lock lock = Mockito.mock(Lock.class);
        when(lock.tryLock()).thenReturn(Boolean.FALSE);
        when(lockRegistry.obtain(any())).thenReturn(lock);
        doNothing().when(createAppointmentValidator).validateRequest(any());
        CreateAppointmentDto request = new CreateAppointmentDto(TRAINER_ID, CLIENT_ID, WORKOUT_DATE, 10);
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(() -> appointmentService.createAppointment(request));
        assertThrows(DbLockException.class, () -> appointmentService.createAppointment(request));
    }
}
