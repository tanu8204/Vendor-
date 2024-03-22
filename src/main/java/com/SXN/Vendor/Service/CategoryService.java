package com.SXN.Vendor.Service;

import com.SXN.Vendor.Entity.Category;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CategoryService {

    String saveCategoryDetails(Category category) throws ExecutionException, InterruptedException;

    List<Category> getItemDetailsByVendorId(String vendorId, String categoryName) throws ExecutionException, InterruptedException;

    int updateUnits(String vendorId, String categoryName, String itemId, Category category) throws ExecutionException, InterruptedException;

    boolean updateOutOfStock(String vendorId, String categoryName, String itemId);

}
