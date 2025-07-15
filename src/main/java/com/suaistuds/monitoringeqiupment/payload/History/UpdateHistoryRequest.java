package com.suaistuds.monitoringeqiupment.payload.History;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) для обновления записи в истории изменений оборудования.
 * Содержит информацию о событии, связанном с изменением состояния оборудования.
 *
 * <p>Особенности класса:
 * <ul>
 *   <li>{@code @Data} - автоматически генерирует геттеры, сеттеры, toString()</li>
 *   <li>{@code @Builder} - реализует паттерн Builder для удобного создания объектов</li>
 *   <li>{@code @AllArgsConstructor} - создает конструктор со всеми полями</li>
 *   <li>{@code @NoArgsConstructor} - создает конструктор по умолчанию</li>
 *   <li>Все поля обязательны для заполнения ({@code @NotNull})</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateHistoryRequest {

    /**
     * Идентификатор оборудования, к которому относится запись
     * <p>Валидация:
     * <ul>
     *   <li>Не может быть null ({@code @NotNull})</li>
     * </ul>
     */
    @NotNull private Long id;

    /**
     * Идентификатор оборудования, к которому относится запись истории
     * <p>Валидация:
     * <ul>
     *   <li>Не может быть null ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему оборудованию</li>
     * </ul>
     */
    @NotNull
    private Long equipmentId;

    /**
     * Идентификатор пользователя, инициировавшего изменение
     * <p>Валидация:
     * <ul>
     *   <li>Не может быть null ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему пользователю</li>
     * </ul>
     */
    @NotNull
    private Long userId;

    /**
     * Идентификатор ответственного лица за изменение
     * <p>Валидация:
     * <ul>
     *   <li>Не может быть null ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему пользователю</li>
     * </ul>
     */
    @NotNull
    private Long responsibleId;

    /**
     * Идентификатор статуса из истории изменений
     * <p>Валидация:
     * <ul>
     *   <li>Не может быть null ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему статусу в справочнике</li>
     * </ul>
     */
    @NotNull
    private Long statusHistoryId;

    /**
     * Дата и время события
     * <p>Валидация:
     * <ul>
     *   <li>Не может быть null ({@code @NotNull})</li>
     *   <li>Должна быть актуальной датой (не будущее время для большинства сценариев)</li>
     * </ul>
     * <p>Формат: LocalDateTime (yyyy-MM-dd'T'HH:mm:ss)
     */
    @NotNull
    private LocalDateTime date;
}
