package com.mohsinon.shared.query.specification;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.mohsinon.shared.query.parser.SearchParser;
import com.mohsinon.shared.query.request.FilterRequest;

public final class SearchSpecificationFactory {

    private SearchSpecificationFactory() {
    }

    public static <T> Specification<T> build(
            Map<String, String> parameters) {

    	FilterRequest filter =
    	        SearchParser.parse(parameters);

    	return new SpecificationBuilder<T>()
    	        .with(filter)
    	        .build();
    }
    
    public static <T> Specification<T> build(
            FilterRequest filter) {

        return new SpecificationBuilder<T>()
                .with(filter)
                .build();
    }

    private static boolean ignore(String key) {

        return key.equals("page")
                || key.equals("size")
                || key.equals("sort")
                || key.equals("sortBy")
                || key.equals("direction")
                || key.equals("search");
    }

}