package com.SXN.Vendor.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
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


    private static final String LIST_COLLECTION_NAME = "Vendor's List";
    private static final String CATALOGUE_COLLECTION_NAME = "Catalogue";


    @Autowired
    private VendorService vendorService;

    //http://localhost:8085/api/VendorList/addCategoryDetails?outOfStock=false&description=demo&price=1234&units=19&pictures=https://firebasestorage.googleapis.com/v0/b/duds-68a6d.appspot.com/o/pic1.jpg?alt=media%26token=452ba8c3-928b-490e-87e6-d567419bbf5f,https://firebasestorage.googleapis.com/v0/b/duds-68a6d.appspot.com/o/pic1.jpg?alt=media%26token=452ba8c3-928b-490e-87e6-d567419bbf5f,https://firebasestorage.googleapis.com/v0/b/duds-68a6d.appspot.com/o/pic1.jpg?alt=media%26token=452ba8c3-928b-490e-87e6-d567419bbf5f&itemId=11&vendorId=vendor&lockinPeriod=15&S=10&L=23&XL=3&XXL=0&M=1&subcategory=Kurtas
   //http://localhost:8085/api/VendorList/addCategoryDetails?outOfStock=false&description=demo&price=1234&units=19&itemId=33&vendorId=vendor&lockinPeriod=15&S=10&L=23&XL=3&XXL=0&M=1&pictures=https://firebasestorage.googleapis.com/v0/b/duds-68a6d.appspot.com/o/pic1.jpg?alt=media%26token=452ba8c3-928b-490e-87e6-d567419bbf5f,https://firebasestorage.googleapis.com/v0/b/duds-68a6d.appspot.com/o/pic1.jpg?alt=media%26token=452ba8c3-928b-490e-87e6-d567419bbf5f,https://firebasestorage.googleapis.com/v0/b/duds-68a6d.appspot.com/o/pic1.jpg?alt=media%26token=452ba8c3-928b-490e-87e6-d567419bbf5f&subcategory=Shirts&Category=Menswear
    @Override
    public Map<String, Object> saveCategoryDetails(String vendorId,String Category, String subcategory, String description, String itemId,
                                                   List<String> pictures, int price,
                                                   Map<String, Integer> size, boolean outOfStock,
                                                   int lockinPeriod, Timestamp lockinStart)
            throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Map<String, Object> savedCategoryData = new HashMap<>();

        try {
            if (itemId == null || itemId.isEmpty()) {
                itemId = UUID.randomUUID().toString();
            }

            // Create a map for the item details
            Map<String, Object> itemDetails = new HashMap<>();
            itemDetails.put("vid", vendorId);
            itemDetails.put("Description", description);
            itemDetails.put("Images", pictures != null ? pictures : new ArrayList<>(3));
            itemDetails.put("Price", price);
            itemDetails.put("Size", size);
            itemDetails.put("outOfStock", outOfStock);
            itemDetails.put("Category", Category);
            itemDetails.put("subCategory", subcategory);
            itemDetails.put("lockinPeriod", lockinPeriod);
            itemDetails.put("lockinStart", lockinStart);

            // Add the item ID to the articles list
            List<String> articles = new ArrayList<>();
            articles.add(itemId);

            // Saving items in subcategory
            DocumentReference categoryDocRef = dbFirestore.collection(LIST_COLLECTION_NAME)
                    .document(vendorId)
                    .collection(Category)
                    .document(subcategory);

            categoryDocRef.update(itemId, itemDetails);

            // Fetch existing articles list
            DocumentSnapshot categoryDoc = categoryDocRef.get().get();
            List<String> existingArticles = (List<String>) categoryDoc.get("articles");

            // Append new itemId to the existing articles list
            if (existingArticles == null) {
                existingArticles = new ArrayList<>();
            }
            existingArticles.add(itemId);

            // Update the articles field with the updated list
            categoryDocRef.update("articles", existingArticles);

            savedCategoryData.put(itemId, itemDetails);
            savedCategoryData.put("articles", existingArticles);

            // Saving items in catalogue
            DocumentReference catalogueRef = dbFirestore.collection(LIST_COLLECTION_NAME)
                    .document(vendorId)
                    .collection("Catalogue")
                    .document(itemId);
            catalogueRef.set(itemDetails);

            // Saving items in womenswear collections
            DocumentReference collectionRef = dbFirestore.collection(subcategory)
                    .document(itemId);
            collectionRef.set(itemDetails);

            return savedCategoryData;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save category details: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Map<String, Map<String, Object>>> getCatalogue(String vendorId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        // Reference to the catalogue collection
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = dbFirestore.collection(LIST_COLLECTION_NAME)
                .document(vendorId)
                .collection(CATALOGUE_COLLECTION_NAME)
                .get();

        // Get the documents from the catalogue collection
        QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
        List<Map<String, Map<String, Object>>> catalogue = new ArrayList<>();

        // Iterate through the documents and add them to the catalogue list
        for (DocumentSnapshot document : querySnapshot) {
            String itemId = document.getId();
            Map<String, Object> itemDetails = document.getData();
            Map<String, Map<String, Object>> item = new HashMap<>();
            item.put(itemId, itemDetails);
            catalogue.add(item);
        }

        return catalogue;
    }

/*
    @Override
    public Map<String, List<Category>> getAllItemsBySubcategory(String vendorId, String categoryName) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference categoryCollectionRef = dbFirestore.collection(COLLECTION_NAME)
                .document(vendorId)
                .collection(categoryName);

        Map<String, List<Category>> itemsBySubcategory = new HashMap<>();

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = categoryCollectionRef.get();
        try {
            QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                String subCategory = documentSnapshot.getId();
                List<Category> items = Collections.singletonList(documentSnapshot.toObject(Category.class));
                itemsBySubcategory.put(subCategory, items);
            }
            return itemsBySubcategory;
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error retrieving items by subcategory: {}", e.getMessage(), e);
            throw e;
        }
    }*/

  /*  @Override
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
    }*/

    /*@Override
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
    }*/

   /* @Override
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


*/
}





