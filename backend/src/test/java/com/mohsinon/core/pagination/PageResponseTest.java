package com.mohsinon.core.pagination;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResponseTest {

    @Test
    @DisplayName("Should correctly map Spring Data Page to PageResponse")
    void shouldMapFromSpringDataPage() {
        List<String> items = List.of("Item A", "Item B", "Item C");
        Page<String> springPage = new PageImpl<>(items, PageRequest.of(0, 10), 25);

        PageResponse<String> response = PageResponse.from(springPage);

        assertThat(response.getContent()).containsExactly("Item A", "Item B", "Item C");
        assertThat(response.getPageNumber()).isEqualTo(0);
        assertThat(response.getPageSize()).isEqualTo(10);
        assertThat(response.getTotalElements()).isEqualTo(25);
        assertThat(response.getTotalPages()).isEqualTo(3);
        assertThat(response.isFirst()).isTrue();
        assertThat(response.isLast()).isFalse();
        assertThat(response.isHasNext()).isTrue();
        assertThat(response.isHasPrevious()).isFalse();
    }

    @Test
    @DisplayName("Should correctly transform items using mapper function")
    void shouldTransformItemsUsingMapper() {
        List<Integer> numbers = List.of(1, 2, 3);
        Page<Integer> springPage = new PageImpl<>(numbers, PageRequest.of(1, 3), 10);

        PageResponse<String> response = PageResponse.from(springPage, num -> "Value: " + num);

        assertThat(response.getContent()).containsExactly("Value: 1", "Value: 2", "Value: 3");
        assertThat(response.getPageNumber()).isEqualTo(1);
        assertThat(response.isFirst()).isFalse();
        assertThat(response.isLast()).isFalse();
    }

    @Test
    @DisplayName("PaginationRequest should enforce size bounds and default sorting")
    void paginationRequestShouldEnforceBounds() {
        PaginationRequest req1 = new PaginationRequest(-5, 500, null, null);
        assertThat(req1.getPage()).isEqualTo(0);
        assertThat(req1.getSize()).isEqualTo(20); // fallback to default when exceeding max 100
        assertThat(req1.getSortBy()).isEqualTo("createdAt");
        assertThat(req1.getDirection()).isEqualTo(SortDirection.DESC);

        PaginationRequest req2 = new PaginationRequest(2, 50, "name", SortDirection.ASC);
        assertThat(req2.getPage()).isEqualTo(2);
        assertThat(req2.getSize()).isEqualTo(50);
        assertThat(req2.getSortBy()).isEqualTo("name");
        assertThat(req2.getDirection()).isEqualTo(SortDirection.ASC);
    }
}
