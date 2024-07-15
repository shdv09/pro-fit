package com.shdv09.visitservice.service;

import com.shdv09.visitservice.dto.request.CreateVisitDto;
import com.shdv09.visitservice.dto.response.VisitDto;
import com.shdv09.visitservice.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;

    @Override
    public VisitDto createUpdateVisit(CreateVisitDto dto) {
        return null;
    }
}

