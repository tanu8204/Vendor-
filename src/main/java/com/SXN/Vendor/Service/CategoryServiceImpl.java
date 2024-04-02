package com.SXN.Vendor.Service;

import com.SXN.Vendor.Entity.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService{

    private static final String COLLECTION_NAME = "Vendor's List";

    private final VendorService vendorService;

    @Autowired
    public CategoryServiceImpl(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @Override
    public String saveCategoryDetails(Category category) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        VendorIdDetails vendor = vendorService.getVendorDetailsById(category.getVendorId());
        if (vendor == null) {
            throw new RuntimeException("Vendor not found with ID: " + category.getVendorId());
        }

        try {
            ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME)
                    .document(category.getVendorId())
                    .collection(category.getCategoryName())
                    .document(category.getSubCategory())
                    .set(category);

            collectionApiFuture.get(); // Wait for the operation to complete

            return "Category details saved successfully";
        } catch (InterruptedException | ExecutionException e) {
            // Log the error and rethrow it
            log.error("Error saving category details: {}", e.getMessage(), e);
            throw e;
        }
    }



    @Override
    public List<Category> getItemDetailsByVendorId(String vendorId, String categoryName) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> query = dbFirestore.collection(COLLECTION_NAME)
                .document(vendorId)
                .collection(categoryName).get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        List<Category> categories = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            categories.add(document.toObject(Category.class));
        }
        return categories;
    }

    @Override
    public int updateUnits(String vendorId, String categoryName, String itemId, int updatedUnits) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        // Get a reference to the category document
        DocumentReference categoryDocRef = dbFirestore.collection(COLLECTION_NAME)
                .document(vendorId)
                .collection(categoryName)
                .document(itemId);

        try {
            // Retrieve the current category document
            DocumentSnapshot documentSnapshot = categoryDocRef.get().get();
            if (documentSnapshot.exists()) {
                // Extract the existing category data
                Category existingCategory = documentSnapshot.toObject(Category.class);
                int currentUnits = existingCategory.getUnits();

                // Ensure sufficient units are available for the update
                if (currentUnits < updatedUnits) {
                    throw new IllegalArgumentException("Insufficient units available for item ID " + itemId);
                }

                // Update the unit count
                int newUnits = currentUnits - updatedUnits;
                existingCategory.setUnits(newUnits);

                // Update the category document in Firestore
                ApiFuture<WriteResult> updateFuture = categoryDocRef.set(existingCategory);
                updateFuture.get(); // Wait for the update operation to complete

                return newUnits;
            } else {
                throw new IllegalArgumentException("Category document with ID " + itemId + " does not exist");
            }
        } catch (FirestoreException e) {
            throw new RuntimeException("Error updating units: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateOutOfStock(String vendorId, String categoryName, String itemId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        // Get a reference to the category document
        DocumentReference categoryDocRef = dbFirestore.collection(COLLECTION_NAME)
                .document(vendorId)
                .collection(categoryName)
                .document(itemId);

        // Get the current units in the category document
        ApiFuture<DocumentSnapshot> future = categoryDocRef.get();
        DocumentSnapshot document;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error getting category document: " + e.getMessage(), e);
        }

        int currentUnits = document.getLong("units").intValue();

        // Update the outOfStock field
        try {
            categoryDocRef.update("outOfStock", currentUnits == 0);
        } catch (FirestoreException e) {
            throw new RuntimeException("Error updating outOfStock field: " + e.getMessage(), e);
        }

        return currentUnits == 0;
    }



}





