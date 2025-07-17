package com.suaistuds.monitoringequipment.payload;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO (Data Transfer Object) для стандартизированного представления ошибок API.
 * Содержит детализированную информацию об исключениях, возникающих в системе.
 *
 * <p>Особенности:
 * <ul>
 *   <li>Автоматически устанавливает временную метку при создании</li>
 *   <li>Обеспечивает неизменяемость списка сообщений</li>
 *   <li>Предоставляет защищенное копирование сообщений</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
public class ExceptionResponse {

    /**
     * Тип ошибки/исключения
     */
    private String error;

    /**
     * HTTP статус код ошибки
     */
    private Integer status;

    /**
     * Детализированные сообщения об ошибках
     * <p><b>Характеристики:</b>
     * <ul>
     *   <li>Неизменяемый список (immutable)</li>
     *   <li>Может содержать null</li>
     * </ul>
     */
    private List<String> messages;

    /**
     * Временная метка возникновения ошибки
     * <p><b>Формат:</b> Instant (UTC)
     * <p><b>Инициализация:</b> Устанавливается автоматически при создании объекта
     */
    private Instant timestamp;

    /**
     * Конструктор для создания ответа с ошибкой
     * @param messages детализированные сообщения об ошибках
     * @param error тип ошибки
     * @param status HTTP статус код
     */
    public ExceptionResponse(List<String> messages, String error, Integer status) {
        setMessages(messages);
        this.error = error;
        this.status = status;
        this.timestamp = Instant.now();
    }

    /**
     * Возвращает защищенную копию списка сообщений
     * @return новый ArrayList с сообщениями или null
     */
    public List<String> getMessages() {
        return messages == null ? null : new ArrayList<>(messages);
    }

    /**
     * Устанавливает неизменяемый список сообщений
     * @param messages список сообщений об ошибках
     */
    public final void setMessages(List<String> messages) {
        this.messages = (messages == null) ? null : Collections.unmodifiableList(messages);
    }
}