package com.SXN.Vendor.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VendorNames {
        private String vendors="Vendors";
        private String vendorId;
        private String vendorName;
}

