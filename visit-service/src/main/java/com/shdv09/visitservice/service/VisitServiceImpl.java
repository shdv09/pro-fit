package com.shdv09.visitservice.service;

import com.shdv09.visitservice.feign.ClientServiceProxy;
import com.shdv09.visitservice.dto.mapper.VisitMapper;
import com.shdv09.visitservice.dto.request.ProcessVisitDto;
import com.shdv09.visitservice.dto.response.VisitDto;
import com.shdv09.visitservice.dto.response.clientservice.ClientDto;
import com.shdv09.visitservice.model.Visit;
import com.shdv09.visitservice.repository.VisitRepository;
import com.shdv09.visitservice.validation.CardValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitServiceImpl implements VisitService {
    private static final String LOG_CODE = "VISIT";

    private final VisitRepository visitRepository;

    private final ClientServiceProxy clientServiceProxy;

    private final VisitMapper visitMapper;

    private final DateFactory dateFactory;

    private final CardValidator cardValidator;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public VisitDto processVisit(ProcessVisitDto request) {
        log.info("{}. Processing visit. Request: {}", LOG_CODE, request);
        ClientDto client = clientServiceProxy.findClientByCardNumber(request.getCardNumber());
        cardValidator.validateCard(client.getCard());
        Visit visit = visitRepository.findFirstByClientIdOrderByStartTimeDesc(client.getId());
        if (visit == null || visit.getEndTime() != null) {
            visit = new Visit(null, dateFactory.generateDate(), null, client.getId());
        } else {
            visit.setEndTime(dateFactory.generateDate());
        }
        return visitMapper.toDto(visitRepository.save(visit), client);
    }
}

