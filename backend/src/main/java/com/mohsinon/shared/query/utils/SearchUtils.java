package com.mohsinon.shared.query.utils;

import com.mohsinon.shared.query.request.SearchRequest;
import com.mohsinon.shared.query.specification.SpecificationBuilder;

public final class SearchUtils {

    private SearchUtils() {
    }

    public static <T> SpecificationBuilder<T> builder(
            SearchRequest request) {

        SpecificationBuilder<T> builder =
                new SpecificationBuilder<>();

        if (request != null &&
                request.getFilters() != null) {

            builder.with(request.getFilters());
        }

        return builder;
    }

}