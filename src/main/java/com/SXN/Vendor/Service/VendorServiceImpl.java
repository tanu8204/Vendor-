package com.SXN.Vendor.Service;

import com.SXN.Vendor.Entity.VendorIdDetails;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class VendorServiceImpl implements VendorService {

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
    public VendorIdDetails getVendorDetailsById(String vendorId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(vendorId);

        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        if (document.exists()) {
            VendorIdDetails vendor = document.toObject(VendorIdDetails.class);
            log.debug("Retrieved vendor details by ID {}: {}", vendorId, vendor);
            return vendor;
        } else {
            log.info("Vendor with ID {} does not exist", vendorId);
            return null;
        }
    }
}
