package com.SXN.Vendor.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class VendorNameServiceImpl implements VendorNameService {

    private static final String NAMES_COLLECTION_NAME = "Vendor's Names";
    private static final String LIST_COLLECTION_NAME = "Vendor's List";
    private static final String VENDORS_DOCUMENT_NAME = "Vendors";
    private static final String VENDORS_LOC_DOCUMENT_NAME="Geolocations";

    @Override
    public Map<String, Object> saveVendorDetails() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference namesCollectionRef = dbFirestore.collection(LIST_COLLECTION_NAME);

        Map<String, Object> savedVendorData = new HashMap<>();
        Map<String, Object> savedVendorLocationData = new HashMap<>();

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

                // Save the vendor location to Vendor's List collection under the vendorId document
                Map<String, Object> vendorLocation = new HashMap<>();
                vendorLocation.put("location", vendorData.get("location"));
                DocumentReference vendorsLocationDocumentRef = dbFirestore.collection(NAMES_COLLECTION_NAME)
                        .document(VENDORS_LOC_DOCUMENT_NAME);
                vendorsLocationDocumentRef.set(Collections.singletonMap(vendorId, vendorLocation), SetOptions.mergeFields(vendorId));

                // Add vendor details to savedVendorData map
                savedVendorData.put(vendorId, vendorDetails);
                savedVendorLocationData.put(vendorId, vendorLocation);

                log.info("Vendor details added to Vendor's Name collection under vendorId: {}", vendorId);
            }
            return Map.of("vendorData", savedVendorData, "vendorLocationData", savedVendorLocationData);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error saving vendor details from Vendor's Names collection: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> getVendorDetails() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        // Retrieve vendor name details
        DocumentReference nameDocumentRef = dbFirestore.collection(NAMES_COLLECTION_NAME)
                .document(VENDORS_DOCUMENT_NAME);
        Map<String, Object> nameDetails = retrieveDocumentData(nameDocumentRef);

        // Retrieve vendor location details
        DocumentReference locationDocumentRef = dbFirestore.collection(NAMES_COLLECTION_NAME)
                .document(VENDORS_LOC_DOCUMENT_NAME);
        Map<String, Object> locationDetails = retrieveDocumentData(locationDocumentRef);

        // Combine both sets of details
        Map<String, Object> vendorDetails = new HashMap<>();
        vendorDetails.put("nameDetails", nameDetails);
        vendorDetails.put("locationDetails", locationDetails);

        return vendorDetails;
    }

    private Map<String, Object> retrieveDocumentData(DocumentReference documentReference)
            throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.getData();
            } else {
                log.info("No vendor details found.");
                return new HashMap<>();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error retrieving vendor details: {}", e.getMessage(), e);
            throw e;
        }
    }

}
