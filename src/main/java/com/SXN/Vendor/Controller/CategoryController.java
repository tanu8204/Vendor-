package com.SXN.Vendor.Controller;

import com.SXN.Vendor.Entity.*;
import com.SXN.Vendor.ResponseUtils.ApiResponse;
import com.SXN.Vendor.ResponseUtils.ResponseUtils;
import com.SXN.Vendor.Service.CategoryService;
import com.SXN.Vendor.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/VendorList/Category/{vendorId}")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> saveCategory(@PathVariable String vendorId,  @RequestBody Category category) {
        try {
            if (category.getItemId() == null) {
                category.setItemId(UUID.randomUUID().toString());
            }

            category.setVendorId(vendorId);
            String updateTime = categoryService.saveCategoryDetails(category);

            return ResponseEntity.ok(ResponseUtils.createOkResponse(category));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createErrorResponse("Error saving category: " + e.getMessage()));
        }
    }

    @GetMapping("/{categoryName}/items/")
    public ResponseEntity<ApiResponse<String>> getItemDetails(@PathVariable String vendorId, @PathVariable String categoryName) throws ExecutionException, InterruptedException {
        try {
            List<Category> items = categoryService.getItemDetailsByVendorId(vendorId, categoryName);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(items));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createErrorResponse("Error retrieving items: " + e.getMessage()));
        }
    }

    @PostMapping("/{categoryName}/items/{itemId}/updateUnits")
    public ResponseEntity<ApiResponse<String>> updateUnits(
            @PathVariable String vendorId,
            @PathVariable String categoryName,
            @PathVariable String itemId,
            @RequestBody Category category) {

        try {
            int updatedUnits = categoryService.updateUnits(vendorId, categoryName, itemId, category);
            return ResponseEntity.ok(ResponseUtils.createOkResponse("Units updated successfully. New units: " + updatedUnits));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.createErrorResponse("Error updating units of a item: " + e.getMessage()));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createErrorResponse("Error updating units of a item: " + e.getMessage()));

        }
    }

    @PostMapping("/{categoryName}/items/{itemId}/updateOutOfStock")
    public ResponseEntity<ApiResponse<String>> updateOutOfStock(
            @PathVariable String vendorId,
            @PathVariable String categoryName,
            @PathVariable String itemId) {

        try {
            boolean outOfStock = categoryService.updateOutOfStock(vendorId, categoryName, itemId);
            String message = outOfStock ? "Out of stock." : "In stock.";
            ApiResponse<String> response = ResponseUtils.createOkResponse(String.valueOf(outOfStock));
            response.setMessage(message);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<String> errorResponse = ResponseUtils.createErrorResponse("Error updating outOfStock field of a item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
