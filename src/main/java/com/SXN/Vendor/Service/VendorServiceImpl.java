package com.SXN.Vendor.Service;

import com.SXN.Vendor.Entity.Category;
import com.SXN.Vendor.Entity.VendorIdDetails;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.lang.*;

import java.util.concurrent.ExecutionException;

@Service
public class VendorServiceImpl implements VendorService{

    private static final String COLLECTION_NAME = "Vendor's List";



    @Override
    public String saveVendor(VendorIdDetails vendor) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME)
                .document(vendor.getVendorId().toString()) // Use vendor ID as document ID
                .set(vendor);

        return collectionApiFuture.get().getUpdateTime().toString();
    }

    @Override
    public VendorIdDetails getVendorDetailsById(String vendor) throws ExecutionException, InterruptedException {

        Firestore dbFirestore = FirestoreClient.getFirestore();

        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(vendor.toString());

        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        VendorIdDetails vendors = null;
        if (document.exists()) {
            vendors = document.toObject(VendorIdDetails.class);
            return vendors;
        } else {
            return null;
        }
    }




}