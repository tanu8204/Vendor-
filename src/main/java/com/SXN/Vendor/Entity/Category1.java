package com.SXN.Vendor.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Category1 {

    @Id //from data jpa dependency
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String itemId = UUID.randomUUID().toString();
    private String vendorId;
    private String categoryName;
    private String description;
    private String pictures;
    private Integer price;
    private double size;
    private String subCategory;
    private String type;
    private Integer unit;
}