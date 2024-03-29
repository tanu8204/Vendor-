package com.SXN.Vendor.Service;

import com.SXN.Vendor.Entity.VendorNames;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class VendorNameServiceImpl implements VendorNameService {

    @Autowired
    private VendorService vendorService;

    private static final String COLLECTION_NAME = "Vendor's Names";

    @Override
    public String saveVendorName(VendorNames vendorNames) throws ExecutionException, InterruptedException {
        String vendorId = vendorNames.getVendorId();
        String vendorName = vendorNames.getVendorName();

        Firestore dbFirestore = FirestoreClient.getFirestore();
        Map<String, Object> data = new HashMap<>();
        data.put(vendorId, vendorName);

        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME)
                .document("Vendors") // Use "Vendors" as document ID
                .set(data, SetOptions.merge());

        try {
            WriteResult writeResult = collectionApiFuture.get();
            log.info("Saved vendor name with ID {} and name {} at {}", vendorId, vendorName, writeResult.getUpdateTime());
            return writeResult.getUpdateTime().toString();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error saving vendor name: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> getVendorNameDetails(String vendors) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(vendors);

        ApiFuture<DocumentSnapshot> future = documentReference.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                Map<String, Object> documentData = document.getData();
                log.info("Retrieved vendor name details for {}: {}", vendors, documentData);
                return documentData;
            } else {
                log.info("Vendor name details not found for {}", vendors);
                return Collections.emptyMap();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error retrieving vendor name details: {}", e.getMessage(), e);
            throw e;
        }
    }
}
