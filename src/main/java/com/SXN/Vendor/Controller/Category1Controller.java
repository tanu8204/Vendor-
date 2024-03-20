package com.SXN.Vendor.Controller;

import com.SXN.Vendor.Entity.Category1;
import com.SXN.Vendor.ResponseUtils.ApiResponse;
import com.SXN.Vendor.ResponseUtils.ResponseUtils;
import com.SXN.Vendor.Service.Category1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/Category1/")
public class Category1Controller {

    @Autowired
    private Category1Service category1Service;


    @PostMapping("/save")
    public ResponseEntity<ApiResponse<String>> saveCategory1(@RequestBody Category1 category1) {
        try {
            String itemId = category1Service.saveCategoryDetails(category1);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(category1));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createErrorResponse("Error saving category: " + e.getMessage()));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<String>> getAllCategories() {
        try {
            List<Category1> categories = category1Service.getAllCategories();
            return ResponseEntity.ok(ResponseUtils.createOkResponse(categories));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createErrorResponse("Error fetching categories: " + e.getMessage()));
        }
    }

}
