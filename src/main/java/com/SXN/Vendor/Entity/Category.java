package com.SXN.Vendor.Entity;

import javax.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Getter
@Setter
@ToString
@Entity //from javax.persistence.Entity;
public class Category {

    @Id //from data jpa dependency
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Map<String,String> itemId=new HashMap<>();
    private String vendorId;
    private String categoryName;
    private String subCategory;
    private String description;
    private List<String> pictures = new ArrayList<>(3);
    private Double price;
    private Map<String, Integer> size = new HashMap<>(); // Map to store size and quantity
    private LocalDateTime lockin_start;
    private LocalDateTime lockin_end;
    private int units;
    private boolean outOfStock;
    private List<String> articles = new ArrayList<>();

    // Constructor to set default values for lockin_start and lockin_end
    public Category() {
        Instant now = Instant.now();
        this.lockin_start = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        this.lockin_end = this.lockin_start.plusDays(15);
    }

}
