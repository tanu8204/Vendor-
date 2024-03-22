package com.SXN.Vendor.ResponseUtils;

import lombok.*;
import org.springframework.http.HttpStatus;

public class ResponseUtils {
    public static <T> ApiResponse<String> createOkResponse(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setMessage("OK");
        response.setError(null);
        response.setStatus("success");
        response.setStatusCode(HttpStatus.OK.value());
        return (ApiResponse<String>) response;
    }

public static <T> ApiResponse<String> createErrorResponse(String errorMessage) {
    ApiResponse<String> errorResponse = new ApiResponse<>();
    errorResponse.setData(null);
    errorResponse.setMessage("Error occurred: " + errorMessage);
    errorResponse.setError(errorMessage);
    errorResponse.setStatus("error");
    errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    return errorResponse;
}

}
