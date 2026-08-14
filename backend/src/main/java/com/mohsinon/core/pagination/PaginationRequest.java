package com.mohsinon.core.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

/**
 * Standard request DTO for paginated and sorted endpoints.
 */
public class PaginationRequest {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;
    private static final String DEFAULT_SORT_BY = "createdAt";

    @Min(value = 0, message = "Page index must not be less than zero.")
    private int page = DEFAULT_PAGE;

    @Min(value = 1, message = "Page size must be at least 1.")
    @Max(value = MAX_SIZE, message = "Page size must not exceed 100.")
    private int size = DEFAULT_SIZE;

    private String sortBy = DEFAULT_SORT_BY;
    private SortDirection direction = SortDirection.DESC;

    public PaginationRequest() {
    }

    public PaginationRequest(int page, int size, String sortBy, SortDirection direction) {
        this.page = Math.max(0, page);
        this.size = (size >= 1 && size <= MAX_SIZE) ? size : DEFAULT_SIZE;
        this.sortBy = (sortBy != null && !sortBy.trim().isEmpty()) ? sortBy.trim() : DEFAULT_SORT_BY;
        this.direction = Objects.requireNonNullElse(direction, SortDirection.DESC);
    }

    public static PaginationRequest of(int page, int size) {
        return new PaginationRequest(page, size, DEFAULT_SORT_BY, SortDirection.DESC);
    }

    public static PaginationRequest of(int page, int size, String sortBy, SortDirection direction) {
        return new PaginationRequest(page, size, sortBy, direction);
    }

    public Pageable toPageable() {
        Sort.Direction dir = direction == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(dir, sortBy));
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(0, page);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = (size >= 1 && size <= MAX_SIZE) ? size : DEFAULT_SIZE;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = (sortBy != null && !sortBy.trim().isEmpty()) ? sortBy.trim() : DEFAULT_SORT_BY;
    }

    public SortDirection getDirection() {
        return direction;
    }

    public void setDirection(SortDirection direction) {
        this.direction = Objects.requireNonNullElse(direction, SortDirection.DESC);
    }
}
