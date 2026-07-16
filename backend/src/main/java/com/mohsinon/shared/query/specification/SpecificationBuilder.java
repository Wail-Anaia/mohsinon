package com.mohsinon.shared.query.specification;

import org.springframework.data.jpa.domain.Specification;

import com.mohsinon.shared.query.criteria.SearchCriteria;
import com.mohsinon.shared.query.request.FilterRequest;

public class SpecificationBuilder<T> {

    private Specification<T> specification;

    public SpecificationBuilder() {
        specification = Specification.where(null);
    }

    public SpecificationBuilder<T> with(SearchCriteria criteria) {

        specification = specification.and(
                new GenericSpecification<>(criteria));

        return this;
    }

    public SpecificationBuilder<T> with(FilterRequest filter) {

        if (filter == null ||
                filter.getCriteria() == null) {
            return this;
        }

        filter.getCriteria()
                .forEach(this::with);

        return this;
    }
    
    public Specification<T> build() {

        return specification.and(
                (root, query, cb) ->
                        cb.isFalse(root.get("deleted")));
    }

}