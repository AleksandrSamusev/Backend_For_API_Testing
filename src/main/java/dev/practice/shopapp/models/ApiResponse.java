package dev.practice.shopapp.models;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ApiResponse", description = "Standard API response wrapper for all endpoints")
public class ApiResponse<T> {
    @Schema(description = "Indicates if the operation was successful", example = "true")
    private boolean success;
    @Schema(description = "A human-readable message describing the result", example = "User successfully retrieved")
    private String message;
    @Schema(description = "The actual data payload (can be an object or a list)")
    private T data;
    @Schema(description = "List of specific error messages if success is false", example = "[\"Email is already in use\", \"Password too short\"]")
    private List<String> errors;
    @Schema(description = "Response status code", example = "404")
    private int errorCode;
    @Schema(description = "Timestamp of the response in milliseconds", example = "1708174320000")
    private long timestamp;
    @Schema(description = "The API path that was called", example = "/api/v1/users/1")
    private String path;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
