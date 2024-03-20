package com.SXN.Vendor.Controller;

import com.SXN.Vendor.Entity.VendorNames;

import com.SXN.Vendor.ResponseUtils.ApiResponse;
import com.SXN.Vendor.ResponseUtils.ResponseUtils;
import com.SXN.Vendor.Service.VendorNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/VendorName/")
public class VendorNameController {

    @Autowired
    private VendorNameService vendorNameService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> saveVendor(@RequestBody VendorNames vendorNames) {
        try {
            String savedVendor = vendorNameService.saveVendorName(vendorNames);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(vendorNames));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createErrorResponse(e.getMessage()));
        }
    }



@GetMapping("{vendors}")
public ResponseEntity<ApiResponse<String>> getVendorNameDetails(@PathVariable String vendors) throws ExecutionException, InterruptedException {
    try {
        Map<String, Object> vendorDetails = vendorNameService.getVendorNameDetails(vendors);
        if (vendorDetails != null) {
            return ResponseEntity.ok((ApiResponse<String>) ResponseUtils.createOkResponse(vendorDetails));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((ApiResponse<String>) ResponseUtils.createErrorResponse("Vendor not found"));
        }
    } catch (ExecutionException | InterruptedException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((ApiResponse<String>) ResponseUtils.createErrorResponse(e.getMessage()));
    }
}



}
