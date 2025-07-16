package com.suaistuds.monitoringeqiupment.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * Стандартный ответ API, используемый для унифицированной структуры всех ответов сервера.
 * Реализует интерфейс {@link Serializable} для поддержки сериализации.
 *
 * <p>Основные характеристики:
 * <ul>
 *   <li>Содержит базовые поля для статуса операции и сообщения</li>
 *   <li>Поддерживает кастомный порядок полей при сериализации JSON</li>
 *   <li>Позволяет передавать HTTP статус без его сериализации в ответ</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
@NoArgsConstructor
@JsonPropertyOrder({
        "success",
        "message"
})
public class ApiResponse implements Serializable {

    /**
     * Идентификатор версии для механизма сериализации
     */
    @Serial
    @JsonIgnore
    private static final long serialVersionUID = 7702134516418120340L;

    /**
     * Флаг успешности выполнения операции
     * <p><b>Тип:</b> Boolean
     * <p><b>JSON свойство:</b> "success"
     */
    @JsonProperty("success")
    private Boolean success;

    /**
     * Информационное сообщение или описание ошибки
     * <p><b>Тип:</b> String
     * <p><b>JSON свойство:</b> "message"
     */
    @JsonProperty("message")
    private String message;

    /**
     * HTTP статус ответа (не сериализуется в JSON)
     * <p><b>Тип:</b> {@link HttpStatus}
     * <p><b>Использование:</b> Внутреннее использование для обработки на сервере
     */
    @JsonIgnore
    private HttpStatus status;

    /**
     * Конструктор для базового ответа
     * @param success флаг успешности операции
     * @param message информационное сообщение
     */
    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Конструктор для ответа с HTTP статусом
     * @param success флаг успешности операции
     * @param message информационное сообщение
     * @param httpStatus HTTP статус ответа
     */
    public ApiResponse(Boolean success, String message, HttpStatus httpStatus) {
        this.success = success;
        this.message = message;
        this.status = httpStatus;
    }
}