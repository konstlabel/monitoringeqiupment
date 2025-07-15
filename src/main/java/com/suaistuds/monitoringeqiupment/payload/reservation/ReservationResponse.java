package com.suaistuds.monitoringeqiupment.payload.reservation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.suaistuds.monitoringeqiupment.payload.audit.UserDateAuditPayload;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) для представления информации о резервировании оборудования.
 * Наследует базовые поля аудита от {@link UserDateAuditPayload} и добавляет специфичные для резервирования данные.
 *
 * <p>Основные характеристики:
 * <ul>
 *   <li>Предоставляет полную информацию о бронировании оборудования</li>
 *   <li>Включает метаданные о создании/изменении записи (из UserDateAuditPayload)</li>
 *   <li>Исключает null-значения при сериализации в JSON ({@code @JsonInclude(Include.NON_NULL)})</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationResponse extends UserDateAuditPayload {

    /**
     * Уникальный идентификатор записи резервирования
     */
    private Long id;

    /**
     * Идентификатор зарезервированного оборудования
     * <p><b>Соответствие:</b> Должен ссылаться на существующую единицу оборудования
     */
    private Long equipmentId;

    /**
     * Идентификатор пользователя, оформившего бронирование
     * <p><b>Соответствие:</b> Должен ссылаться на существующего пользователя системы
     */
    private Long userId;

    /**
     * Идентификатор ответственного лица
     * <p><b>Особенности:</b>
     * <ul>
     *   <li>Может отличаться от userId</li>
     *   <li>Может быть null, если не назначен</li>
     * </ul>
     */
    private Long responsibleId;

    /**
     * Дата и время начала бронирования
     * <p><b>Проверки:</b> Должно быть раньше endDate
     */
    private LocalDateTime startDate;

    /**
     * Дата и время окончания бронирования
     * <p><b>Проверки:</b> Должно быть позже startDate
     */
    private LocalDateTime endDate;

    /**
     * Идентификатор текущего статуса резервирования
     * <p><b>Соответствие:</b> Должен ссылаться на существующий статус из справочника

     */
    private Long statusReservationId;
}