package com.shdv09.clientservice.specification;

import com.shdv09.clientservice.model.Client;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@RequiredArgsConstructor
public class BirthDateSpecification implements Specification<Client> {
    private final transient LocalDate birthDate;

    @Override
    public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (birthDate == null) {
            return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }
        return criteriaBuilder.equal(root.get("birthDate"), this.birthDate);
    }
}
