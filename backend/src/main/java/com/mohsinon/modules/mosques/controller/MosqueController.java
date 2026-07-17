package com.mohsinon.modules.mosques.controller;

import com.mohsinon.modules.mosques.dto.request.CreateMosqueRequest;
import com.mohsinon.modules.mosques.dto.response.MosqueResponse;
import com.mohsinon.modules.mosques.service.MosqueService;
import com.mohsinon.shared.query.response.PageResponse;
import com.mohsinon.shared.Annotation.ApiRequestBody;
import com.mohsinon.shared.Annotation.IdParameter;
import com.mohsinon.shared.documentation.SwaggerTags;
import com.mohsinon.shared.documentation.ApiResponseDocumentation;
import com.mohsinon.shared.query.request.SearchRequest;
import com.mohsinon.shared.query.resolver.QueryRequestResolver;
import com.mohsinon.common.api.ApiConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(ApiConstants.API_V1 + "/mosques")
@Tag(name = SwaggerTags.MOSQUES, description = "إدارة المساجد")
@SecurityRequirement(name = "Bearer Authentication")
public class MosqueController {

    private final MosqueService mosqueService;
    private final QueryRequestResolver queryRequestResolver;

    public MosqueController(
            MosqueService mosqueService,
            QueryRequestResolver queryRequestResolver) {

        this.mosqueService = mosqueService;
        this.queryRequestResolver = queryRequestResolver;
    }
    
    /* =====================================================================
     * Create "إنشاء مسجد"
     * ===================================================================== */
    
    @Operation(summary = "إنشاء مسجد",description = "إنشاء مسجد جديد داخل المنصة.")
    @ApiResponseDocumentation.CreateApiResponses
    @PostMapping
    public ResponseEntity<MosqueResponse> create(
    		@ApiRequestBody
            @Valid @RequestBody CreateMosqueRequest request) {

        MosqueResponse response = mosqueService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /* =====================================================================
     * search "البحث عن المساجد"
     * ===================================================================== */
    
    @Operation(summary = "البحث عن المساجد",
    	    description = """
	    	        البحث مع دعم:
	
	    	        • Pagination
	
	    	        • Sorting
	
	    	        • Filtering
	    	        """
    	)
    @GetMapping("/search")
    public ResponseEntity<PageResponse<MosqueResponse>> search(
    		
    		@ParameterObject
            @ModelAttribute
            SearchRequest request,

            HttpServletRequest servletRequest) {

        return ResponseEntity.ok(mosqueService.search(request,queryRequestResolver.resolve(servletRequest)));
    }
 
    /* =====================================================================
     * findById "الحصول على تفاصيل مسجد"
     * ===================================================================== */
    
    @Operation(summary = "الحصول على تفاصيل مسجد",description = "يعيد بيانات مسجد محدد.")
    @ApiResponseDocumentation.GetApiResponses
    @GetMapping("/{id}")
    public ResponseEntity<MosqueResponse> findById(
    		@IdParameter
    		@PathVariable
    		UUID id) {

        return ResponseEntity.ok(mosqueService.findById(id));
    }

    /* =====================================================================
     * getAllMosques "الحصول على قائمة المساجد"
     * ===================================================================== */
    
    @Operation(summary = "الحصول على قائمة المساجد",
               description = """
                    يعيد قائمة المساجد مع دعم:

                    • Pagination

                    • Filtering

                    • Sorting
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "تم جلب قائمة المساجد بنجاح")
    })
    @GetMapping
    public PageResponse<MosqueResponse> getAllMosques(

            @ParameterObject
            @ModelAttribute
            SearchRequest request,

            HttpServletRequest servletRequest){

        return mosqueService.search(

                request,

                queryRequestResolver.resolve(servletRequest));

    }

    /* =====================================================================
     * update "تحديث مسجد"
     * ===================================================================== */
    
    @Operation(summary = "تحديث مسجد",description = "تحديث بيانات مسجد موجود.")
    @ApiResponseDocumentation.UpdateApiResponses
    @PutMapping("/{id}")
    public ResponseEntity<MosqueResponse> update(
    		@IdParameter
    		@PathVariable
    		UUID id,
    		@ApiRequestBody
            @Valid @RequestBody CreateMosqueRequest request) {

        return ResponseEntity.ok(mosqueService.update(id, request));
    }

    /* =====================================================================
     * delete "حذف مسجد"
     * ===================================================================== */
    
    @Operation(summary = "حذف مسجد",description = "حذف المسجد حذفًا منطقيًا (Soft Delete).")
    @ApiResponseDocumentation.DeleteApiResponses
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
    		@IdParameter
    		@PathVariable
    		UUID id) {

        mosqueService.delete(id);

        return ResponseEntity.noContent().build();
    }
    
    /* =====================================================================
     * restoreDeleted "استعادة مسجد محذوف"
     * ===================================================================== */
    
    @Operation(summary = "استعادة مسجد محذوف",description = "استعادة مسجد تم حذفه حذفًا منطقيًا.")
    @ApiResponseDocumentation.LifecycleApiResponses
    @PatchMapping("/{id}/restore")
    public ResponseEntity<MosqueResponse> restoreDeleted(
    		@IdParameter
    		@PathVariable
    		UUID id) {

        return ResponseEntity.ok(mosqueService.restoreDeleted(id));
    }
    
    /* =====================================================================
     * archive  "أرشفة مسجد"
     * ===================================================================== */
    
    @Operation(summary = "أرشفة مسجد",description = "نقل المسجد إلى الأرشيف.")
    @ApiResponseDocumentation.LifecycleApiResponses
    @PatchMapping("/{id}/archive")
    public ResponseEntity<MosqueResponse> archive(
    		@IdParameter
    		@PathVariable
    		UUID id) {

        return ResponseEntity.ok(mosqueService.archive(id));
    }
    
    /* =====================================================================
     * restoreArchive  "إلغاء أرشفة مسجد"
     * ===================================================================== */
    
    @Operation(summary = "إلغاء أرشفة مسجد",description = "إخراج المسجد من الأرشيف.")
    @ApiResponseDocumentation.LifecycleApiResponses
    @PatchMapping("/{id}/restore-archive")
    public ResponseEntity<MosqueResponse> restoreArchive(
    		@IdParameter
    		@PathVariable
    		UUID id) {

        return ResponseEntity.ok(mosqueService.restoreArchive(id));
    }
 
    /* =====================================================================
     * activate  "تفعيل مسجد"
     * ===================================================================== */
    
    @Operation(summary = "تفعيل مسجد",description = "تفعيل المسجد وإعادته إلى الحالة النشطة.")
    @ApiResponseDocumentation.LifecycleApiResponses
    @PatchMapping("/{id}/activate")
    public ResponseEntity<MosqueResponse> activate(
    		@IdParameter
    		@PathVariable
    		UUID id) {

        return ResponseEntity.ok(mosqueService.activate(id));
    }
    
    /* =====================================================================
     * deactivate  "تعطيل مسجد"
     * ===================================================================== */
    
    @Operation(summary = "تعطيل مسجد",description = "تعطيل المسجد دون حذفه.")
    @ApiResponseDocumentation.LifecycleApiResponses
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<MosqueResponse> deactivate(
    		@IdParameter
    		@PathVariable
    		UUID id) {

        return ResponseEntity.ok(mosqueService.deactivate(id));
    }

}