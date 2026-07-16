package com.mohsinon.shared.query.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.mohsinon.shared.query.criteria.FilterOperator;
import com.mohsinon.shared.query.criteria.SearchCriteria;

@Data
public class FilterRequest {

    private List<SearchCriteria> criteria = new ArrayList<>();

    public void add(
            String key,
            FilterOperator operation,
            Object value) {

        criteria.add(
                new SearchCriteria(
                        key,
                        operation,
                        value
                )
        );
    }

}