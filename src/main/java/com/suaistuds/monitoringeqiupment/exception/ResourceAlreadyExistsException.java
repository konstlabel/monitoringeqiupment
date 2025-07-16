package com.suaistuds.monitoringeqiupment.exception;

import com.suaistuds.monitoringeqiupment.payload.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;


/**
 * Исключение, выбрасываемое при попытке создания ресурса, который уже существует.
 * Автоматически возвращает HTTP статус 409 (CONFLICT) при обработке Spring MVC.
 *
 * <p>Содержит информацию о:
 * <ul>
 *   <li>Типе ресурса (resourceName)</li>
 *   <li>Названии поля (fieldName)</li>
 *   <li>Значении поля (fieldValue)</li>
 * </ul>
 * Формирует автоматический API-ответ с описанием конфликта.
 *
 * @since 2025-07-13
 */
@Getter
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private transient ApiResponse apiResponse;
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    /**
     * Создает исключение с информацией о конфликте ресурсов.
     *
     * @param resourceName название типа ресурса (например, "User", "Equipment")
     * @param fieldName название поля, вызвавшего конфликт (например, "email", "serialNumber")
     * @param fieldValue значение поля, вызвавшего конфликт
     */
    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super();
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        setApiResponse();
    }

    /**
     * Формирует API-ответ с сообщением о конфликте.
     */
    private void setApiResponse() {
        String message = String.format("%s already exists with %s: '%s'",
                resourceName, fieldName, fieldValue);
        apiResponse = new ApiResponse(Boolean.FALSE, message);
    }
}