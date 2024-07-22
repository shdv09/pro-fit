package com.shdv09.clientservice.service;

import com.shdv09.clientservice.dto.mapper.ClientMapper;
import com.shdv09.clientservice.dto.mapper.ClubCardMapper;
import com.shdv09.clientservice.dto.response.ClientDto;
import com.shdv09.clientservice.dto.response.ClubCardDto;
import com.shdv09.clientservice.exception.NotFoundException;
import com.shdv09.clientservice.model.ClubCard;
import com.shdv09.clientservice.repository.ClientRepository;
import com.shdv09.clientservice.repository.ClubCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final ClubCardRepository clubCardRepository;

    private final ClientMapper clientMapper;

    private final ClubCardMapper clubCardMapper;

    @Transactional(readOnly = true)
    @Override
    public ClientDto findClient(Long id) {
        ClientDto result = clientRepository.findById(id)
                .map(clientMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Client with id = %d not found".formatted(id)));
        ClubCardDto clubCardDto = clubCardRepository.findClubCardByClientId(id)
                .map(clubCardMapper::toDto)
                .orElse(null);
        result.setCard(clubCardDto);
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public ClientDto findClientByCardNumber(String cardNumber) {
        ClubCard card = clubCardRepository.findClubCardByNumber(cardNumber)
                .orElseThrow(() -> new NotFoundException("Club card with number = %s not found".formatted(cardNumber)));
        ClientDto clientDto = clientMapper.toDto(card.getClient());
        clientDto.setCard(clubCardMapper.toDto(card));
        return clientDto;
    }
}
