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
public class LastNameSpecification implements Specification<Client> {
    private final transient String lastName;

    @Override
    public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (!StringUtils.hasLength(lastName)) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.equal(cb.lower(root.get("lastName")), this.lastName.toLowerCase());
    }
}
