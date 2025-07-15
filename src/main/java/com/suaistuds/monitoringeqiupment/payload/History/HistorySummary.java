package com.suaistuds.monitoringeqiupment.payload.History;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.suaistuds.monitoringeqiupment.payload.audit.UserDateAuditPayload;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) для представления записи истории изменений оборудования.
 * Наследует от {@link UserDateAuditPayload}, добавляя информацию о времени создания/изменения и пользователях.
 *
 * <p>Основные характеристики:
 * <ul>
 *   <li>Предоставляет частичную информацию о событии в истории оборудования</li>
 *   <li>Включает метаданные о создании/изменении записи (из UserDateAuditPayload)</li>
 *   <li>Исключает null-поля при сериализации в JSON ({@code @JsonInclude(Include.NON_NULL)})</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistorySummary extends UserDateAuditPayload {

    /**
     * Идентификатор оборудования, к которому относится запись
     * <p>Соответствует ID существующей единицы оборудования
     */
    private Long equipmentId;

    /**
     * Идентификатор пользователя, инициировавшего изменение
     * <p>Ссылается на аккаунт пользователя в системе
     */
    private Long userId;

    /**
     * Идентификатор ответственного лица
     * <p>Может отличаться от userId (например, при изменении статуса администратором)
     */
    private Long responsibleId;

    /**
     * Идентификатор статуса из истории изменений
     * <p>Соответствует значению из справочника статусов истории
     */
    private Long statusHistoryId;

    /**
     * Дата и время фактического события
     * <p>Формат: ISO-8601 (yyyy-MM-dd'T'HH:mm:ss)
     * <p>Может отличаться от createdAt (например, задним числом)
     */
    private LocalDateTime date;
}
