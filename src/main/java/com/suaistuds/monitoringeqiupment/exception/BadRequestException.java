package com.suaistuds.monitoringeqiupment.exception;

import com.suaistuds.monitoringeqiupment.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Исключение, выбрасываемое при некорректных или недопустимых запросах.
 * Автоматически возвращает HTTP статус 400 (BAD_REQUEST) при обработке Spring MVC.
 *
 * <p>Поддерживает несколько вариантов создания:
 * <ul>
 *   <li>С кастомным API-ответом ({@link ApiResponse})</li>
 *   <li>С текстовым сообщением об ошибке</li>
 *   <li>С текстовым сообщением и причиной ошибки</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private ApiResponse apiResponse;

    /**
     * Создает исключение с кастомным API-ответом.
     *
     * @param apiResponse объект API-ответа с деталями ошибки
     */
    public BadRequestException(ApiResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }

    /**
     * Создает исключение с текстовым сообщением.
     *
     * @param message описание ошибки
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Создает исключение с текстовым сообщением и причиной.
     *
     * @param message описание ошибки
     * @param cause исключение-причина
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Возвращает API-ответ с деталями ошибки.
     *
     * @return объект ApiResponse или null, если не задан
     */
    public ApiResponse getApiResponse() {
        return apiResponse;
    }
}
