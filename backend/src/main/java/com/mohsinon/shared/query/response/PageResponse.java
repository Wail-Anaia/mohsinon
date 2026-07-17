package com.mohsinon.shared.query.response;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {

    private List<T> content;
    
    @Schema(description = "رقم الصفحة الحالية")
    private int page;
    
    @Schema(description = "عدد العناصر")
    private int size;

    @Schema(description = "إجمالي العناصر")
    private long totalElements;
    
    @Schema(description = "إجمالي الصفحات")
    private int totalPages;
    
    @Schema(description = "الأول")
    private boolean first;
    
    @Schema(description = "الأخير")
    private boolean last;

}