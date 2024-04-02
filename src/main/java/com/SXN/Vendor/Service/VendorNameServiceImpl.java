/*
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

    private static final String VENDOR_LIST_COLLECTION = "Vendor's List";

*/
/*
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
*//*


    @Override
    public Map<String, Object> getVendorNameDetails() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference vendorListCollectionRef = dbFirestore.collection(VENDOR_LIST_COLLECTION);

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = vendorListCollectionRef.get();
        try {
            QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                String vendorId = documentSnapshot.getId();
                Map<String, Object> vendorData = documentSnapshot.getData();

                // Construct VendorNames object
                VendorNames vendorNames = new VendorNames();
                vendorNames.setVendorId(vendorId);
                vendorNames.setVendorName((String) vendorData.get("vendorName"));
                vendorNames.setPhoneNo((String) vendorData.get("phoneNo"));
                vendorNames.setLocation((String) vendorData.get("location"));

                // Save VendorNames object to Vendor's Names collection
                saveVendorName(vendorNames);
            }

            log.info("Retrieved and added vendor details to Vendor's Names collection");
            return null; // Or return some response if needed
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error retrieving and adding vendor details: {}", e.getMessage(), e);
            throw e;
        }
    }


}
*/
package com.SXN.Vendor.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class VendorNameServiceImpl implements VendorNameService {

    private static final String NAMES_COLLECTION_NAME = "Vendor's Names";
    private static final String LIST_COLLECTION_NAME = "Vendor's List";
    private static final String VENDORS_DOCUMENT_NAME = "Vendors";
    private static final String VENDORS_LOC_DOCUMENT_NAME="Geolocations";

    @Autowired
    private VendorService vendorService;

    @Override
    public String saveVendorNamesFromList() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference namesCollectionRef = dbFirestore.collection(LIST_COLLECTION_NAME);

        // Get all documents from Vendor's Names collection
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = namesCollectionRef.get();
        try {
            QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                String vendorId = documentSnapshot.getId();
                Map<String, Object> vendorData = documentSnapshot.getData();

                // Construct vendor details map
                Map<String, Object> vendorDetails = new HashMap<>();
                vendorDetails.put("location", vendorData.get("location"));
                vendorDetails.put("phno", vendorData.get("phno"));
                vendorDetails.put("vendorName", vendorData.get("vendorName"));

                // Save the vendor data to Vendor's List collection under the vendorId document
                DocumentReference vendorsDocumentRef = dbFirestore.collection(NAMES_COLLECTION_NAME)
                        .document(VENDORS_DOCUMENT_NAME);
                vendorsDocumentRef.set(Collections.singletonMap(vendorId, vendorDetails), SetOptions.mergeFields(vendorId));
                log.info("Vendor details added to Vendor's Name collection under vendorId: {}", vendorId);
            }
            return "Vendor details added from Vendor's Names collection to Vendor's List collection.";
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error saving vendor names from Vendor's Names collection to Vendor's List collection: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> getVendorNameDetails() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(NAMES_COLLECTION_NAME)
                .document(VENDORS_DOCUMENT_NAME);

        ApiFuture<DocumentSnapshot> future = documentReference.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                Map<String, Object> documentData = document.getData();
                log.info("Retrieved vendor name details: {}", documentData);
                return documentData;
            } else {
                log.info("No vendor name details found.");
                return new HashMap<>();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error retrieving vendor name details: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public String saveVendorslocation() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference namesCollectionRef = dbFirestore.collection(LIST_COLLECTION_NAME);

        // Get all documents from Vendor's Names collection
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = namesCollectionRef.get();
        try {
            QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                String vendorId = documentSnapshot.getId();
                Map<String, Object> vendorData = documentSnapshot.getData();

                // Construct vendor details map
                Map<String, Object> vendorDetails = new HashMap<>();
                vendorDetails.put("location", vendorData.get("location"));

                // Save the vendor data to Vendor's List collection under the vendorId document
                DocumentReference vendorsDocumentRef = dbFirestore.collection(NAMES_COLLECTION_NAME)
                        .document(VENDORS_LOC_DOCUMENT_NAME);
                vendorsDocumentRef.set(Collections.singletonMap(vendorId, vendorDetails), SetOptions.mergeFields(vendorId));
                log.info("Vendors location added to Vendor's Name collection under vendorId: {}", vendorId);
            }
            return "Vendor location added from Vendor's Names collection to Vendor's List collection.";
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error saving vendors location from Vendor's Names collection to Vendor's List collection: {}", e.getMessage(), e);
            throw e;
        }
    }
}
