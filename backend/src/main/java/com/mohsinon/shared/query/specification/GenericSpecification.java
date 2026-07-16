package com.mohsinon.shared.query.specification;

import com.mohsinon.shared.query.criteria.FilterOperator;
import com.mohsinon.shared.query.criteria.SearchCriteria;
import com.mohsinon.shared.query.converter.ValueConverter;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenericSpecification<T> implements Specification<T> {

    private final SearchCriteria criteria;

    public GenericSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(
            Root<T> root,
            CriteriaQuery<?> query,
            CriteriaBuilder builder) {

        Path path = root.get(criteria.getKey());
        
        Object value =
                ValueConverter.convert(
                        path.getJavaType(),
                        criteria.getValue());

        Object valueTo =
                ValueConverter.convert(
                        path.getJavaType(),
                        criteria.getValueTo());

        FilterOperator operator = criteria.getOperator();

        return switch (operator) {

            case EQUAL ->
                    builder.equal(path, value);

            case NOT_EQUAL ->
                    builder.notEqual(path, value);

            case LIKE ->
                    builder.like(
                            builder.lower(path.as(String.class)),
                            "%" + value.toString().toLowerCase() + "%");

            case STARTS_WITH ->
                    builder.like(
                            builder.lower(path.as(String.class)),
                            value.toString().toLowerCase() + "%");

            case ENDS_WITH ->
                    builder.like(
                            builder.lower(path.as(String.class)),
                            "%" + value.toString().toLowerCase());

            case GREATER_THAN ->
                    builder.greaterThan(
                            path,
                            (Comparable) value);

            case GREATER_THAN_OR_EQUAL ->
                    builder.greaterThanOrEqualTo(
                            path,
                            (Comparable) value);

            case LESS_THAN ->
                    builder.lessThan(
                            path,
                            (Comparable) value);

            case LESS_THAN_OR_EQUAL ->
                    builder.lessThanOrEqualTo(
                            path,
                            (Comparable) value);

            case BETWEEN ->
                    builder.and(
                            builder.greaterThanOrEqualTo(
                                    path,
                                    (Comparable) value),
                            builder.lessThanOrEqualTo(
                                    path,
                                    (Comparable) valueTo));

            case IN ->
                    path.in(value);

            case IS_NULL ->
                    builder.isNull(path);

            case IS_NOT_NULL ->
                    builder.isNotNull(path);
        };
    }

}