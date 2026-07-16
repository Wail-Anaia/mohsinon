package com.mohsinon.shared.query.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mohsinon.shared.query.request.PaginationRequest;
import com.mohsinon.shared.query.response.PageResponse;

public final class PaginationUtils {

    private PaginationUtils() {
    }

    public static Pageable pageable(PaginationRequest request) {

        Sort.Direction direction =
                request.getDirection().equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        return PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(direction, request.getSortBy())
        );
    }

    public static <T> PageResponse<T> response(Page<T> page) {

        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

}