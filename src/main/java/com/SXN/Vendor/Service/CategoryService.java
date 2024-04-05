package com.SXN.Vendor.Service;


import com.google.cloud.Timestamp;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface CategoryService {

    Map<String, Object> saveCategoryDetails(String vendorId,String Category, String subcategory,String description, String itemId,
                                                   List<String> pictures, int price,
                                                   Map<String, Integer> size, boolean outOfStock,
                                                   int lockinPeriod, Timestamp lockinStart)
            throws ExecutionException, InterruptedException;

    List<Map<String, Map<String, Object>>> getCatalogue(String vendorId) throws ExecutionException, InterruptedException;
}
