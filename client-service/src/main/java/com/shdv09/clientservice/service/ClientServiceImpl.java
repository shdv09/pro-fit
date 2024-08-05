package com.shdv09.clientservice.service;

import com.shdv09.clientservice.dto.mapper.ClientMapper;
import com.shdv09.clientservice.dto.mapper.ClubCardMapper;
import com.shdv09.clientservice.dto.request.AddClientDto;
import com.shdv09.clientservice.dto.request.BindCardRequest;
import com.shdv09.clientservice.dto.request.UpdateClientDto;
import com.shdv09.clientservice.dto.response.ClientDto;
import com.shdv09.clientservice.dto.response.ClubCardDto;
import com.shdv09.clientservice.exception.NotFoundException;
import com.shdv09.clientservice.model.Client;
import com.shdv09.clientservice.model.ClubCard;
import com.shdv09.clientservice.repository.ClientRepository;
import com.shdv09.clientservice.repository.ClubCardRepository;
import com.shdv09.clientservice.specification.BirthDateSpecification;
import com.shdv09.clientservice.specification.FirstNameSpecification;
import com.shdv09.clientservice.specification.LastNameSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private static final String LOG_CODE = "CLIENT";

    private final ClientRepository clientRepository;

    private final ClubCardRepository clubCardRepository;

    private final ClientMapper clientMapper;

    private final ClubCardMapper clubCardMapper;

    @Transactional(readOnly = true)
    @Override
    public ClientDto findClient(Long id) {
        log.info("{}. Finding client by id. Id: {}", LOG_CODE, id);
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
        log.info("{}. Finding client by club card. Card number: {}", LOG_CODE, cardNumber);
        ClubCard card = clubCardRepository.findClubCardByNumber(cardNumber)
                .orElseThrow(() -> new NotFoundException("Club card with number = %s not found".formatted(cardNumber)));
        Client client = Optional.ofNullable(card.getClient())
                .orElseThrow(() -> new NotFoundException(
                        "Client with club card number = %s not found".formatted(cardNumber)));
        ClientDto clientDto = clientMapper.toDto(client);
        clientDto.setCard(clubCardMapper.toDto(card));
        return clientDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ClientDto> findClients(String firstName, String lastName, LocalDate birthDate) {
        log.info("{}. Finding client by criteria. First name: {}, last name: {}, date of birth: {}",
                LOG_CODE, firstName, lastName, birthDate);
        Specification<Client> specification = Specification.where(new LastNameSpecification(lastName))
                .and(new FirstNameSpecification(firstName))
                .and(new BirthDateSpecification(birthDate));
        return clientRepository.findAll(specification).stream()
                .map(clientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ClientDto addClient(AddClientDto request) {
        log.info("{}. Adding client. Request: {}", LOG_CODE, request);
        var client = clientMapper.fromDto(request);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Transactional
    @Override
    public ClientDto updateClient(UpdateClientDto request) {
        log.info("{}. Updating client. Request: {}", LOG_CODE, request);
        clientRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Client with id=%d not found".formatted(request.getId())));
        var client = clientMapper.fromDto(request);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Transactional
    @Override
    public ClientDto bindCard(BindCardRequest request) {
        log.info("{}. Binding club card with client. Request: {}", LOG_CODE, request);
        var client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new NotFoundException("Client with id=%d not found"
                        .formatted(request.getClientId())));
        var existedClubCard = clubCardRepository.findClubCardByClientId(request.getClientId())
                .orElse(null);
        if (existedClubCard != null) {
            existedClubCard.setClient(null);
            clubCardRepository.save(existedClubCard);
        }
        var clubCard = clubCardRepository.findById(request.getCardId())
                .orElseThrow(() -> new NotFoundException("Card with id=%d not found".formatted(request.getCardId())));
        clubCard.setClient(client);
        clubCard = clubCardRepository.save(clubCard);
        ClientDto result = clientMapper.toDto(client);
        result.setCard(clubCardMapper.toDto(clubCard));
        return result;
    }
}
