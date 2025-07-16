package com.suaistuds.monitoringeqiupment.exception;

import com.suaistuds.monitoringeqiupment.payload.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему ресурсу.
 * Автоматически возвращает HTTP статус 404 (NOT_FOUND) при обработке Spring MVC.
 *
 * <p>Содержит детализированную информацию о:
 * <ul>
 *   <li>Типе ресурса (resourceName)</li>
 *   <li>Названии поля (fieldName)</li>
 *   <li>Значении поля (fieldValue)</li>
 * </ul>
 * Автоматически формирует API-ответ с описанием ошибки.
 *
 * @since 2025-07-13
 */
@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private transient ApiResponse apiResponse;
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    /**
     * Создает исключение с информацией о не найденном ресурсе.
     *
     * @param resourceName название типа ресурса (например, "User", "Equipment")
     * @param fieldName название поля, по которому искали ресурс
     * @param fieldValue значение поля, по которому искали ресурс
     * @since 2025-07-13
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super();
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Формирует API-ответ с сообщением об ошибке.
     * Сообщение имеет формат: "[resourceName] not found with [fieldName]: '[fieldValue]'"
     * @since 2025-07-13
     */
    private void setApiResponse() {
        String message = String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue);

        apiResponse = new ApiResponse(Boolean.FALSE, message);
    }
}
