package com.suaistuds.monitoringeqiupment.exception;


import com.suaistuds.monitoringeqiupment.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Исключение, выбрасываемое при попытке доступа к защищенным ресурсам без аутентификации.
 * Автоматически возвращает HTTP статус 401 (UNAUTHORIZED) при обработке Spring MVC.
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
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;


    private ApiResponse apiResponse;
    private String message;

    /**
     * Создает исключение с кастомным API-ответом.
     *
     * @param apiResponse объект API-ответа с деталями ошибки
     */
    public UnauthorizedException(ApiResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }

    /**
     * Создает исключение с текстовым сообщением.
     *
     * @param message описание ошибки аутентификации
     */
    public UnauthorizedException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Создает исключение с текстовым сообщением и причиной.
     *
     * @param message описание ошибки аутентификации
     * @param cause исключение-причина
     */
    public UnauthorizedException(String message, Throwable cause) {
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

    /**
     * Устанавливает API-ответ для исключения.
     *
     * @param apiResponse объект ApiResponse
     */
    public void setApiResponse(ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }

    /**
     * Возвращает сообщение об ошибке.
     *
     * @return текст сообщения об ошибке
     */
    public String getMessage() {
        return message;
    }

    /**
     * Устанавливает сообщение об ошибке.
     *
     * @param message текст сообщения об ошибке
     */
    public void setMessage(String message) {
        this.message = message;
    }

}