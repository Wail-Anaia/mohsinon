package com.mohsinon.shared.query.request;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationRequest {
	
	@Schema(
            description = "رقم الصفحة",
            example = "0"
    )
    private int page = 0;
	
	@Schema(
            description = "عدد العناصر في الصفحة",
            example = "10"
    )
    private int size = 10;
	
	@Schema(
            description = "اسم الحقل المستخدم للترتيب",
            example = "createdAt"
    )
    private String sortBy = "createdAt";
	
	@Schema(
            description = "اتجاه الترتيب",
            example = "DESC"
    )
    private String direction = "desc";

}