package com.mohsinon.shared.query.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mohsinon.shared.query.request.FilterRequest;
import com.mohsinon.shared.query.request.SearchRequest;
import com.mohsinon.shared.query.specification.SearchSpecificationFactory;
import com.mohsinon.shared.query.utils.PaginationUtils;

@Service
public class SearchService {

    public Pageable pageable(SearchRequest request) {

        return PaginationUtils.pageable(request);
    }

    public <T> Specification<T> specification(FilterRequest filter) {

        return SearchSpecificationFactory.build(filter);
    }

}