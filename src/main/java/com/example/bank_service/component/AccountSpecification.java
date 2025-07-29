package com.example.bank_service.component;

import com.example.bank_service.entity.Account;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class AccountSpecification {

    public static Specification<Account> filterBy(String cardNumber, String cardName) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (cardNumber != null && !cardNumber.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("cardNumber"), "%" + cardNumber + "%"));
            }

            if (cardName != null && !cardName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("cardName"), "%" + cardName + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
