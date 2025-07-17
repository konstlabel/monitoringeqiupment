package com.suaistuds.monitoringequipment.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Базовое исключение приложения для внутренних серверных ошибок.
 * Автоматически возвращает HTTP статус 500 (INTERNAL_SERVER_ERROR) при обработке Spring MVC.
 *
 * <p>Используется для обработки непредвиденных ошибок сервера, которые не относятся
 * к конкретным бизнес-сценариям. Рекомендуется создавать более специализированные
 * исключения для конкретных случаев.
 *
 * @since 2025-07-13
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Создает исключение с указанным сообщением об ошибке.
     *
     * @param message детализированное сообщение об ошибке
     */
    public AppException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и причиной.
     *
     * @param message детализированное сообщение об ошибке
     * @param cause исходное исключение, вызвавшее эту ошибку
     */
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}