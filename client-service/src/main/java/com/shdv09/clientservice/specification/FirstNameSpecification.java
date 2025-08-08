package com.shdv09.clientservice.specification;

import com.shdv09.clientservice.model.Client;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class FirstNameSpecification implements Specification<Client> {
    private final transient String firstName;

    @Override
    public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (!StringUtils.hasLength(firstName)) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.equal(cb.lower(root.get("firstName")), this.firstName.toLowerCase());
    }
}
