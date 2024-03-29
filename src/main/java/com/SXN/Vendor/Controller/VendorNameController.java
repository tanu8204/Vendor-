package com.SXN.Vendor.Controller;

import com.SXN.Vendor.Entity.VendorNames;
import com.SXN.Vendor.ResponseUtils.ApiResponse;
import com.SXN.Vendor.ResponseUtils.ResponseUtils;
import com.SXN.Vendor.Service.VendorNameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/api/VendorName/")
public class VendorNameController {

    @Autowired
    private VendorNameService vendorNameService;

    @PostMapping("addVendors")
    public ResponseEntity<ApiResponse<String>> saveVendor(@RequestBody VendorNames vendorNames) {
        try {
            String savedVendor = vendorNameService.saveVendorName(vendorNames);
            log.info("Saved vendor: {}", vendorNames);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(vendorNames));
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error saving vendor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("{vendors}/List")
    public ResponseEntity<ApiResponse<String>> getVendorNameDetails(@PathVariable String vendors) {
        try {
            Map<String, Object> vendorDetails = vendorNameService.getVendorNameDetails(vendors);
            if (vendorDetails != null) {
                log.debug("Retrieved vendor details for vendor: {}", vendors);
                return ResponseEntity.ok((ApiResponse<String>) ResponseUtils.createOkResponse(vendorDetails));
            } else {
                log.info("Vendor not found for ID: {}", vendors);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body((ApiResponse<String>) ResponseUtils.createErrorResponse("Vendor not found"));
            }
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error retrieving vendor details: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((ApiResponse<String>) ResponseUtils.createErrorResponse(e.getMessage()));
        }
    }
}
