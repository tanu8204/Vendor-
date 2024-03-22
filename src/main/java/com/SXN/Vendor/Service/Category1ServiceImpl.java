package com.SXN.Vendor.Service;

import com.SXN.Vendor.Entity.Category1;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class Category1ServiceImpl implements Category1Service {

    private static final String COLLECTION_NAME = "Category1";

    @Override
    public String saveCategoryDetails(Category1 category) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // Perform any validation or business logic here

        // Set vendorId and other fields as needed
        // category.setVendorId(...);

        ApiFuture<WriteResult> writeResult = dbFirestore.collection(COLLECTION_NAME).document(category.getItemId()).set(category);
        return writeResult.get().getUpdateTime().toString();
    }

    @Override
    public List<Category1> getAllCategories() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = dbFirestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        List<Category1> categories = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            categories.add(document.toObject(Category1.class));
        }
        return categories;
    }

}
