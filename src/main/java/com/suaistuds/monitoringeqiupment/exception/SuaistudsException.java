package com.suaistuds.monitoringeqiupment.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * Базовое исключение приложения для обработки ошибок с указанием HTTP статуса.
 * Используется как родительский класс для всех кастомных исключений приложения.
 *
 * <p>Основные особенности:
 * <ul>
 *   <li>Содержит HTTP статус для возврата клиенту</li>
 *   <li>Поддерживает сообщение об ошибке</li>
 *   <li>Может содержать причину исключения (Throwable)</li>
 *   <li>Использует Lombok {@link Getter} для автоматической генерации геттеров</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Getter
public class SuaistudsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6593330219878485669L;

    private final HttpStatus status;
    private final String message;

    /**
     * Создает исключение с указанным HTTP статусом и сообщением.
     *
     * @param status HTTP статус ошибки
     * @param message детализированное сообщение об ошибке
     */
    public SuaistudsException(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    /**
     * Создает исключение с HTTP статусом, сообщением и причиной.
     *
     * @param status HTTP статус ошибки
     * @param message детализированное сообщение об ошибке
     * @param exception исходное исключение, вызвавшее эту ошибку
     */
    public SuaistudsException(HttpStatus status, String message, Throwable exception) {
        super(exception);
        this.status = status;
        this.message = message;
    }

}