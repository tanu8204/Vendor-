package com.SXN.Vendor.Controller;

import com.SXN.Vendor.Entity.VendorIdDetails;
import com.SXN.Vendor.ResponseUtils.ApiResponse;
import com.SXN.Vendor.ResponseUtils.ResponseUtils;
import com.SXN.Vendor.Service.VendorService;
import com.google.api.gax.rpc.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;


@Slf4j
@RestController
@RequestMapping("/api/VendorList/")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    //http://localhost:8085/api/VendorList/registration?vendorId=vendor1&vendorName=ExampleVendor&gst_No=1234567890&address=ExampleAddress&phoneNumber=1234567890&regNo=ABC123&onboarding=2024-04-07&isActive=1&latitude=37.7749&longitude=-122.4194
    @PostMapping("registration")
    public ResponseEntity<ApiResponse<VendorIdDetails>> registerVendor(
            @RequestParam(required = false) String vendorId,
            @RequestParam(required = false, defaultValue = "Default Item Name") String vendorName,
            @RequestParam(required = false) String gst_No,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String regNo,
            @RequestParam(required = false) String onboarding,
            @RequestParam(required = false, defaultValue = "1") int isActive,
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String longitude) {
        try {
            VendorIdDetails vendor = new VendorIdDetails();
            if (vendorId != null) {
                vendor.setVendorId(vendorId);
            } else {
                vendor.setVendorId(UUID.randomUUID().toString());
            }
            vendor.setVendorName(vendorName);
            vendor.setGst_No(gst_No);
            vendor.setAddress(address);
            vendor.setPhoneNumber(phoneNumber);
            vendor.setRegNo(regNo);
            vendor.setOnboarding(onboarding);
            vendor.setIsActive(isActive);

            Map<String, Double> location = new HashMap<>();
            if (latitude != null && longitude != null) {
                location.put("latitude", Double.parseDouble(latitude));
                location.put("longitude", Double.parseDouble(longitude));
            }
            vendor.setLocation(location);

            String savedVendor = vendorService.saveVendor(vendor);
            log.info("Vendor registration successful for ID: {}", vendor.getVendorId());

            return ResponseEntity.ok(ResponseUtils.createOkResponse(vendor));
        } catch (NumberFormatException | ExecutionException | InterruptedException e) {
            log.error("Error registering vendor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createErrorResponse("Error registering vendor: " + e.getMessage()));
        }
    }

    //http://localhost:8085/api/VendorList/LogIn?vendorId=vendor1
    @GetMapping("LogIn")
    public ResponseEntity<ApiResponse<VendorIdDetails>> getVendorDetails(@RequestParam String vendorId) {
        try {
            VendorIdDetails getVendorDetails = vendorService.getVendorDetailsById(vendorId);
            if (getVendorDetails == null) {
                log.info("Vendor not found for ID: {}", vendorId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.createErrorResponse("Vendor not found"));
            }
            log.info("Retrieved vendor details for ID: {}", vendorId);
            ApiResponse<VendorIdDetails> response = ResponseUtils.createOkResponse(getVendorDetails);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving vendor details: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createErrorResponse("Internal Server Error"));
        }
    }

}
