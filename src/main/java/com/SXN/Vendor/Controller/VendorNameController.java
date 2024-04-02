package com.SXN.Vendor.Controller;
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

    @PostMapping("saveVendorNamesFromList")
    public ResponseEntity<ApiResponse<String>> saveVendorNamesFromList() {
        try {
            String message = vendorNameService.saveVendorNamesFromList();
            log.info(message);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(message));
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error saving vendor names from list: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("getVendorNameDetails")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getVendorNameDetails() {
        try {
            Map<String, Object> vendorDetails = vendorNameService.getVendorNameDetails();
            log.info("Retrieved vendor name details: {}", vendorDetails);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(vendorDetails));
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error retrieving vendor name details: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("saveVendorslocation")
    public ResponseEntity<ApiResponse<String>> saveVendorslocation() {
        try {
            String message = vendorNameService.saveVendorslocation();
            log.info(message);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(message));
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error saving vendor names from list: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createErrorResponse(e.getMessage()));
        }
    }
}

