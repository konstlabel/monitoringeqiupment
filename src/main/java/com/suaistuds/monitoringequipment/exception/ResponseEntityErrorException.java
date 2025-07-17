package com.suaistuds.monitoringequipment.exception;


import com.suaistuds.monitoringequipment.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.io.Serial;

/**
 * Исключение, содержащее готовый ResponseEntity с ошибкой API.
 * Используется для передачи уже сформированного HTTP-ответа с ошибкой
 * через механизм исключений Spring.
 *
 * <p>Основные особенности:
 * <ul>
 *   <li>Содержит полный HTTP-ответ ({@link ResponseEntity}) с ошибкой</li>
 *   <li>Позволяет передавать кастомные статусы и структуры ошибок</li>
 *   <li>Поле apiResponse помечено как transient для безопасной сериализации</li>
 * </ul>
 *
 * @since 2025-07-13
 */
public class ResponseEntityErrorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3156815846745801694L;

    private transient ResponseEntity<ApiResponse> apiResponse;

    /**
     * Создает исключение с готовым ResponseEntity, содержащим ошибку API.
     *
     * @param apiResponse ResponseEntity с ошибкой ({@link ApiResponse})
     */
    public ResponseEntityErrorException(ResponseEntity<ApiResponse> apiResponse) {
        this.apiResponse = apiResponse;
    }

    /**
     * Возвращает ResponseEntity с ошибкой API.
     *
     * @return ResponseEntity, содержащий детали ошибки
     */
    public ResponseEntity<ApiResponse> getApiResponse() {
        return apiResponse;
    }
}