package com.mohsinon.shared.query.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest extends PaginationRequest {

    /**
     * البحث النصي العام
     */
    private String search;

    /**
     * جميع الفلاتر الديناميكية
     */
    private FilterRequest filters = new FilterRequest();

}