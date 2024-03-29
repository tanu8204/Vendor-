package com.SXN.Vendor.Controller;

import com.SXN.Vendor.Entity.VendorIdDetails;
import com.SXN.Vendor.ResponseUtils.ApiResponse;
import com.SXN.Vendor.ResponseUtils.ResponseUtils;
import com.SXN.Vendor.Service.VendorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.SXN.Vendor.ResponseUtils.ResponseUtils.createErrorResponse;

@Slf4j
@RestController
@RequestMapping("/api/VendorList/")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("registration")
    public ResponseEntity<ApiResponse<String>> registerVendor(@RequestBody VendorIdDetails vendor) {
        try {
            if (vendor.getVendorId() == null) {
                vendor.setVendorId(UUID.randomUUID().toString());
            }
            if (vendor.getVendorName() == null) {
                vendor.setVendorName("Default Item Name");
            }
            String savedVendor = vendorService.saveVendor(vendor);
            log.info("Vendor registration successful for ID: {}", vendor.getVendorId());
            return ResponseEntity.ok(ResponseUtils.createOkResponse(vendor));
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error registering vendor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("LogIn/{vendorId}")
    public ResponseEntity<ApiResponse<String>> getVendorDetails(@PathVariable String vendorId) {
        try {
            VendorIdDetails getVendorDetails = vendorService.getVendorDetailsById(vendorId);
            log.info("Retrieved vendor details for ID: {}", vendorId);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(getVendorDetails));
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error retrieving vendor details: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(e.getMessage()));
        }
    }
}
