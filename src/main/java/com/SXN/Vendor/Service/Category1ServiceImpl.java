package com.SXN.Vendor.Service;

import com.SXN.Vendor.Entity.Category1;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class Category1ServiceImpl implements Category1Service {

    private static final String COLLECTION_NAME = "Category1";

    @Override
    public String saveCategoryDetails(Category1 category) throws ExecutionException, InterruptedException {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> writeResult = dbFirestore.collection(COLLECTION_NAME).document(category.getItemId()).set(category);
            log.info("Saved category details with item ID: {}", category.getItemId());
            return writeResult.get().getUpdateTime().toString();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error saving category details: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Category1> getAllCategories() throws ExecutionException, InterruptedException {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> query = dbFirestore.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> documents = query.get().getDocuments();
            List<Category1> categories = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                Category1 category = document.toObject(Category1.class);
                categories.add(category);
                log.debug("Retrieved category: {}", category);
            }
            return categories;
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error retrieving categories: {}", e.getMessage(), e);
            throw e;
        }
    }
}
