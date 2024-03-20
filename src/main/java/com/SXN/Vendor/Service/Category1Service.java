package com.SXN.Vendor.Service;

import com.SXN.Vendor.Entity.Category1;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface Category1Service {
    String saveCategoryDetails(Category1 category) throws ExecutionException, InterruptedException;

    List<Category1> getAllCategories() throws ExecutionException, InterruptedException;
}
