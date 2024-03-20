package com.SXN.Vendor.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.Id;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VendorIdDetails {

        @Id //from data jpa dependency
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private String vendorId = UUID.randomUUID().toString();
        private String gst_No;
        private String address;
        private String phoneNumber;
        private String location;
        private String regNo;
        private String vendorName;
        private String onboarding;


}