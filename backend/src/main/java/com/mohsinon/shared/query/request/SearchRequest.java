package com.mohsinon.shared.query.request;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest extends PaginationRequest {

    /**
     * البحث النصي العام
     */
    private String search;
    
    @Schema(hidden = true)
    private Map<String, String> parameters = new HashMap<>();

}