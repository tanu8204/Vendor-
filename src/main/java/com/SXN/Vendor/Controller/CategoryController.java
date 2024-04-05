package com.SXN.Vendor.Controller;

import com.SXN.Vendor.ResponseUtils.ApiResponse;
import com.SXN.Vendor.ResponseUtils.ResponseUtils;
import com.SXN.Vendor.Service.CategoryService;
import com.google.cloud.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/VendorList")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //http://localhost:8085/api/VendorList/addCategoryDetails?outOfStock=false&description=demo&price=1234&units=19&pictures=https://firebasestorage.googleapis.com/v0/b/duds-68a6d.appspot.com/o/pic1.jpg?alt=media%26token=452ba8c3-928b-490e-87e6-d567419bbf5f,https://firebasestorage.googleapis.com/v0/b/duds-68a6d.appspot.com/o/pic1.jpg?alt=media%26token=452ba8c3-928b-490e-87e6-d567419bbf5f,https://firebasestorage.googleapis.com/v0/b/duds-68a6d.appspot.com/o/pic1.jpg?alt=media%26token=452ba8c3-928b-490e-87e6-d567419bbf5f&itemId=11&vendorId=vendor&lockinPeriod=15&S=10&L=23&XL=3&XXL=0&M=1&subcategory=Kurtas
    //http://localhost:8085/api/VendorList/addCategoryDetails?outOfStock=false&description=demo&price=1234&units=19&itemId=33&vendorId=vendor&lockinPeriod=15&S=10&L=23&XL=3&XXL=0&M=1&pictures=https://firebasestorage.googleapis.com/v0/b/duds-68a6d.appspot.com/o/pic1.jpg?alt=media%26token=452ba8c3-928b-490e-87e6-d567419bbf5f,https://firebasestorage.googleapis.com/v0/b/duds-68a6d.appspot.com/o/pic1.jpg?alt=media%26token=452ba8c3-928b-490e-87e6-d567419bbf5f,https://firebasestorage.googleapis.com/v0/b/duds-68a6d.appspot.com/o/pic1.jpg?alt=media%26token=452ba8c3-928b-490e-87e6-d567419bbf5f&subcategory=Shirts&Category=Menswear
    @PostMapping("/addCategoryDetails")
    public ResponseEntity<ApiResponse<Map<String, Object>>> saveCategoryDetails(
            @RequestParam String vendorId,
            @RequestParam String Category,
            @RequestParam String subcategory,
            @RequestParam String description,
            @RequestParam(required = false) String itemId,
            @RequestParam List<String> pictures,
            @RequestParam int price,
            @RequestParam boolean outOfStock,
            @RequestParam int S,
            @RequestParam int M,
            @RequestParam int L,
            @RequestParam int XL,
            @RequestParam int XXL,
            @RequestParam int lockinPeriod) {
        try {
            // Convert lockinStart String to Timestamp
          /*  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date startDate = dateFormat.parse(lockinStart);
             com.google.cloud.Timestamp startTimestamp = com.google.cloud.Timestamp.of(startDate);

*/
            Timestamp currentTimestamp = Timestamp.ofTimeSecondsAndNanos(Instant.now().getEpochSecond(), Instant.now().getNano());

            // Convert Date to com.google.cloud.Timestamp

            Map<String, Integer> size = new HashMap<>();
            size.put("S", S);
            size.put("M", M);
            size.put("L", L);
            size.put("XL", XL);
            size.put("XXL", XXL);

            Map<String, Object> savedCategory = categoryService.saveCategoryDetails(vendorId,Category,subcategory,description, itemId, pictures, price, size, outOfStock, lockinPeriod, currentTimestamp);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(savedCategory));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createErrorResponse("Failed to add category: " + e.getMessage()));
        }
    }

    //http://localhost:8085/api/VendorList/getcatalogue?vendorId=vendor
    @GetMapping("/getcatalogue")
    public ResponseEntity<ApiResponse<List<Map<String, Map<String, Object>>>>> getCatalogue(@RequestParam String vendorId) {
        try {
            List<Map<String, Map<String, Object>>> catalogue = categoryService.getCatalogue(vendorId);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(catalogue));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createErrorResponse("Failed to fetch catalogue: " + e.getMessage()));
        }
    }

/*
    @GetMapping("/getCategoryDetails")
    public ResponseEntity<ApiResponse<List<Category>>> getItemDetails(@RequestParam String vendorId, @RequestParam String categoryName) throws ExecutionException, InterruptedException {
        try {
            List<Category> items = categoryService.getItemDetailsByVendorId(vendorId, categoryName);
            return ResponseEntity.ok(ResponseUtils.createOkResponse(items));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createErrorResponse("Error retrieving items: " + e.getMessage()));
        }
    }

    @PostMapping("/updateUnits")
    public ResponseEntity<ApiResponse<String>> updateUnits(
            @RequestParam String vendorId,
            @RequestParam String categoryName,
            @RequestParam String itemId,
            @RequestParam int Units) {

        try {
            int updatedUnits = categoryService.updateUnits(vendorId, categoryName, itemId, Units);
            return ResponseEntity.ok(ResponseUtils.createOkResponse("Units updated successfully. New units: " + updatedUnits));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.createErrorResponse("Error updating units of an item: " + e.getMessage()));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createErrorResponse("Error updating units of an item: " + e.getMessage()));
        }
    }

    @PostMapping("/updateOutOfStock")
    public ResponseEntity<ApiResponse<String>> updateOutOfStock(
            @RequestParam String vendorId,
            @RequestParam String categoryName,
            @RequestParam String itemId) {

        try {
            boolean outOfStock = categoryService.updateOutOfStock(vendorId, categoryName, itemId);
            String message = outOfStock ? "Out of stock." : "In stock.";
            ApiResponse<String> response = ResponseUtils.createOkResponse(String.valueOf(outOfStock));
            response.setMessage(message);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<String> errorResponse = ResponseUtils.createErrorResponse("Error updating outOfStock field of an item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }*/
}
