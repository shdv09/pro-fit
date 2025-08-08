package com.shdv09.clientservice.repository;

import com.shdv09.clientservice.model.ClubCard;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClubCardRepository extends CrudRepository<ClubCard, Long> {
    Optional<ClubCard> findClubCardByNumber(String number);

    Optional<ClubCard> findClubCardByClientId(Long clientId);
}
