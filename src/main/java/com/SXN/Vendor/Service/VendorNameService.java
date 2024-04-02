package com.SXN.Vendor.Service;

import com.SXN.Vendor.Entity.VendorNames;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface VendorNameService {

//    String saveVendorName(VendorNames vendorNames) throws ExecutionException, InterruptedException;

    //Map<String, Object> getVendorNameDetails() throws ExecutionException, InterruptedException;
    String saveVendorNamesFromList() throws ExecutionException, InterruptedException;
    Map<String, Object> getVendorNameDetails() throws ExecutionException, InterruptedException;

    String saveVendorslocation() throws ExecutionException, InterruptedException;
}