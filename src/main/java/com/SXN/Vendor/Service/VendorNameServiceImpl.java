package com.SXN.Vendor.Service;

import com.SXN.Vendor.Entity.VendorNames;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class VendorNameServiceImpl implements VendorNameService{

    @Autowired
    private  VendorService vendorService;
    private static final String COLLECTION_NAME = "Vendor's Names";


    @Override
    public String saveVendorName(VendorNames vendorNames) throws ExecutionException, InterruptedException {

        String vendorId=vendorNames.getVendorId();
        String vendorName=vendorNames.getVendorName();

        Firestore dbFirestore = FirestoreClient.getFirestore();
        Map<String, Object> data = new HashMap<>();

        data.put(vendorId, vendorName);

        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME)
                .document("Vendors") // Use vendor ID as document ID
                .set(data,SetOptions.merge());

        return collectionApiFuture.get().getUpdateTime().toString();
    }

    @Override
    public Map<String, Object> getVendorNameDetails(String vendors) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(vendors);

        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Map<String, Object> documentData = document.getData();

            // Return document data as is
            return documentData;
        } else {
            // Document not found, return appropriate response
            return Collections.emptyMap();
        }
    }

}
