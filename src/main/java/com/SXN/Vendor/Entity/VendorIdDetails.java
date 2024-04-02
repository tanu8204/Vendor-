package com.SXN.Vendor.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import javax.persistence.Entity;
import java.util.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VendorIdDetails {

        @Id //from data jpa dependency
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private String vendorId = UUID.randomUUID().toString();
        private String gst_No;
        private String address;
        private String phoneNumber;
        private String regNo;
        private String vendorName;
        private String onboarding;
        private int isActive;
        @ElementCollection
        @CollectionTable(name = "vendor_location", joinColumns = @JoinColumn(name = "vendor_id"))
        @MapKeyColumn(name = "location_key")
        @Column(name = "location_value")
        private Map<String, Double> location = new HashMap<>();

        @PrePersist
        @PreUpdate
        private void ensureIsActiveValid() {
                isActive = (isActive == 0) ? 0 : 1;
        }
}