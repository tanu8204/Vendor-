package com.SXN.Vendor.Controller;
import com.SXN.Vendor.Entity.VendorIdDetails;
import com.SXN.Vendor.ResponseUtils.ApiResponse;
import com.SXN.Vendor.ResponseUtils.ResponseUtils;
import com.SXN.Vendor.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.SXN.Vendor.ResponseUtils.ResponseUtils.createErrorResponse;

@RestController
@RequestMapping("/api/VendorList/")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> registerVendor(@RequestBody VendorIdDetails vendor) {
        try {
            if (vendor.getVendorId() == null) {
                vendor.setVendorId(UUID.randomUUID().toString());
            }
            if (vendor.getVendorName() == null) {
                vendor.setVendorName("Default Item Name");
            }
            String savedVendor = vendorService.saveVendor(vendor);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(vendor));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("{vendorId}")
    public ResponseEntity<ApiResponse<String>> getVendorDetails(@PathVariable String vendorId) {
        try {
            VendorIdDetails getVendorDetails = vendorService.getVendorDetailsById(vendorId);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(getVendorDetails));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(e.getMessage()));
        }
    }
}
