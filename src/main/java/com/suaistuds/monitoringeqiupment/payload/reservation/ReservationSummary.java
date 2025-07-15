package com.suaistuds.monitoringeqiupment.payload.reservation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.suaistuds.monitoringeqiupment.payload.audit.UserDateAuditPayload;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) для краткого представления информации о резервировании оборудования.
 * Наследует базовые поля аудита от {@link UserDateAuditPayload} и содержит только ключевые данные о бронировании.
 *
 * <p>Основные характеристики:
 * <ul>
 *   <li>Используется для передачи сокращенной информации о резервировании</li>
 *   <li>Исключает null-значения при сериализации ({@code @JsonInclude(Include.NON_NULL)})</li>
 *   <li>Включает метаданные о создании/изменении записи из родительского класса</li>
 * </ul>
 *
 * <p>Отличия от {@link ReservationResponse}:
 * <ul>
 *   <li>Не содержит идентификатор записи (id)</li>
 *   <li>Используется для summary-представлений и списков</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationSummary extends UserDateAuditPayload {

    /**
     * Идентификатор зарезервированного оборудования
     * <p><b>Тип:</b> Long
     * <p>Обязательное поле
     */
    private Long equipmentId;

    /**
     * Идентификатор пользователя, оформившего бронирование
     * <p><b>Тип:</b> Long
     * <p>Обязательное поле
     */
    private Long userId;

    /**
     * Идентификатор ответственного лица
     * <p><b>Тип:</b> Long
     * <p>Опциональное поле
     */
    private Long responsibleId;

    /**
     * Дата и время начала бронирования
     * <p><b>Тип:</b> ISO-8601 (yyyy-MM-dd'T'HH:mm:ss)
     * <p>Обязательное поле
     */
    private LocalDateTime startDate;

    /**
     * Дата и время окончания бронирования
     * <p><b>Тип:</b> ISO-8601 (yyyy-MM-dd'T'HH:mm:ss)
     * <p>Обязательное поле
     */
    private LocalDateTime endDate;

    /**
     * Идентификатор статуса резервирования
     * <p><b>Тип:</b> Long
     * <p>Обязательное поле

     */
    private Long statusReservationId;
}
