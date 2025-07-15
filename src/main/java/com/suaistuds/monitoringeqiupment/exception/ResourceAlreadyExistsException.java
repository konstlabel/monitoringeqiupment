package com.suaistuds.monitoringeqiupment.exception;

import com.suaistuds.monitoringeqiupment.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private transient ApiResponse apiResponse;

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super();
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        setApiResponse();
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    private void setApiResponse() {
        String message = String.format("%s already exists with %s: '%s'",
                resourceName, fieldName, fieldValue);
        apiResponse = new ApiResponse(Boolean.FALSE, message);
    }
}