package com.shdv09.visitservice.service;

import com.shdv09.visitservice.dto.response.clientservice.CardType;
import com.shdv09.visitservice.dto.response.clientservice.ClubCardDto;
import com.shdv09.visitservice.exception.AccessDeniedException;
import com.shdv09.visitservice.feign.ClientServiceProxy;
import com.shdv09.visitservice.dto.mapper.VisitMapper;
import com.shdv09.visitservice.dto.request.ProcessVisitDto;
import com.shdv09.visitservice.dto.response.VisitDto;
import com.shdv09.visitservice.dto.response.clientservice.ClientDto;
import com.shdv09.visitservice.model.Visit;
import com.shdv09.visitservice.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;

    private final ClientServiceProxy clientServiceProxy;

    private final VisitMapper visitMapper;

    @Transactional
    @Override
    public VisitDto processVisit(ProcessVisitDto dto) {
        ClientDto client = clientServiceProxy.findClientByCardNumber(dto.getCardNumber());
        validateCard(client.getCard());
        Visit visit = visitRepository.findFirstByClientIdOrderByStartTimeDesc(client.getId());
        if (visit == null || visit.getEndTime() != null) {
            visit = new Visit(null, new Date(), null, client.getId());
        } else {
            visit.setEndTime(new Date());
        }
        return visitMapper.toDto(visitRepository.save(visit), client);
    }

    private void validateCard(ClubCardDto card) {
        CardType cardType = card.getCardType();
        LocalTime now = LocalTime.now();
        LocalTime start = cardType.getStart();
        LocalTime end = cardType.getEnd();
        if (now.isBefore(start) || now.isAfter(end)) {
            throw new AccessDeniedException("Access to club denied for card %s, invalid hours"
                    .formatted(card.getNumber()));
        }

    }
}

