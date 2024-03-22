package com.SXN.Vendor.Entity;

import javax.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import java.util.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity //from javax.persistence.Entity;
public class Category {

    @Id //from data jpa dependency
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String itemId = UUID.randomUUID().toString();
    private String vendorId;
    private String categoryName;
    private String description;
    private List<String> pictures = new ArrayList<>();
    private Integer price;
    private double size;
    private String subCategory;
    private String type;
    private int units;
    private boolean outOfStock;

}
