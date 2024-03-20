package com.SXN.Vendor.configuration;

import com.SXN.Vendor.Entity.VendorIdDetails;
import com.SXN.Vendor.Service.CategoryService;
import com.SXN.Vendor.Service.CategoryServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties

public class ServiceConfiguration {

    @Bean
    public VendorIdDetails vendorIdDetails() {
        return new VendorIdDetails();
    }


}