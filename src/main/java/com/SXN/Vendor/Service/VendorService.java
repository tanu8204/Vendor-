package com.SXN.Vendor.Service;

import com.SXN.Vendor.Entity.VendorIdDetails;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface VendorService {
    String saveVendor(VendorIdDetails vendor) throws ExecutionException, InterruptedException;

    VendorIdDetails getVendorDetailsById(String VendorId) throws ExecutionException, InterruptedException;
}