package com.SXN.Vendor.Controller;

import com.SXN.Vendor.Entity.Category;
import com.SXN.Vendor.ResponseUtils.ApiResponse;
import com.SXN.Vendor.ResponseUtils.ResponseUtils;
import com.SXN.Vendor.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/VendorList")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/addCategoryDetails")
    public ResponseEntity<ApiResponse<Category>> saveCategory(
            @RequestParam String vendorId,
            @RequestParam String categoryName,
            @RequestParam String subCategory,
            @RequestParam String description,
            @RequestParam List<String> pictures,
            @RequestParam Double price,
            @RequestParam Map<String, Integer> size,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime lockin_start,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime lockin_end,
            @RequestParam int units,
            @RequestParam boolean outOfStock) {
        try {
            // Set default values for lockin_start and lockin_end if not provided
            if (lockin_start == null) {
                lockin_start = LocalDateTime.now();
            }
            if (lockin_end == null) {
                lockin_end = lockin_start.plusDays(15);
            }

            // Create Category object
            Category category = new Category();
            category.setVendorId(vendorId);
            category.setCategoryName(categoryName);
            category.setSubCategory(subCategory);
            category.setDescription(description);
            category.setPictures(pictures);
            category.setPrice(price);
            category.setSize(size);
            category.setLockin_start(lockin_start);
            category.setLockin_end(lockin_end);
            category.setUnits(units);
            category.setOutOfStock(outOfStock);

            // Save category details
            String itemId = categoryService.saveCategoryDetails(category);

            // Prepare response
            ApiResponse<Category> response = ResponseUtils.createOkResponse(category);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createErrorResponse("Error saving category: " + e.getMessage()));
        }
    }

    @GetMapping("/getCategoryDetails")
    public ResponseEntity<ApiResponse<List<Category>>> getItemDetails(@RequestParam String vendorId, @RequestParam String categoryName) throws ExecutionException, InterruptedException {
        try {
            List<Category> items = categoryService.getItemDetailsByVendorId(vendorId, categoryName);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(items));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createErrorResponse("Error retrieving items: " + e.getMessage()));
        }
    }

    @PostMapping("/updateUnits")
    public ResponseEntity<ApiResponse<String>> updateUnits(
            @RequestParam String vendorId,
            @RequestParam String categoryName,
            @RequestParam String itemId,
            @RequestParam int Units) {

        try {
            int updatedUnits = categoryService.updateUnits(vendorId, categoryName, itemId, Units);
            return ResponseEntity.ok(ResponseUtils.createOkResponse("Units updated successfully. New units: " + updatedUnits));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.createErrorResponse("Error updating units of an item: " + e.getMessage()));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createErrorResponse("Error updating units of an item: " + e.getMessage()));
        }
    }

    @PostMapping("/updateOutOfStock")
    public ResponseEntity<ApiResponse<String>> updateOutOfStock(
            @RequestParam String vendorId,
            @RequestParam String categoryName,
            @RequestParam String itemId) {

        try {
            boolean outOfStock = categoryService.updateOutOfStock(vendorId, categoryName, itemId);
            String message = outOfStock ? "Out of stock." : "In stock.";
            ApiResponse<String> response = ResponseUtils.createOkResponse(String.valueOf(outOfStock));
            response.setMessage(message);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<String> errorResponse = ResponseUtils.createErrorResponse("Error updating outOfStock field of an item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
