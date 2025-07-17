package com.suaistuds.monitoringequipment.exception;

import com.suaistuds.monitoringequipment.payload.ApiResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Исключение, выбрасываемое при попытке доступа к защищенным ресурсам без необходимых прав.
 * Автоматически возвращает HTTP статус 401 (UNAUTHORIZED) при обработке Spring MVC.
 *
 * <p>Поддерживает два варианта создания:
 * <ul>
 *   <li>С кастомным API-ответом ({@link ApiResponse})</li>
 *   <li>С текстовым сообщением об ошибке</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class AccessDeniedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter @Setter
    private ApiResponse apiResponse;
    @Getter @Setter
    private String message;

    /**
     * Создает исключение с кастомным API-ответом.
     *
     * @param apiResponse объект API-ответа с деталями ошибки
     */
    public AccessDeniedException(ApiResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }

    /**
     * Создает исключение с текстовым сообщением.
     *
     * @param message описание ошибки
     */
    public AccessDeniedException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Создает исключение с текстовым сообщением и причиной.
     *
     * @param message описание ошибки
     * @param cause исключение-причина
     */
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}