package com.suaistuds.monitoringeqiupment.payload.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) для создания новой записи резервирования оборудования.
 * Содержит все необходимые данные для бронирования оборудования пользователем.
 *
 * <p>Валидация полей:
 * <ul>
 *   <li>Обязательные поля помечены аннотацией {@code @NotNull}</li>
 *   <li>Даты должны быть валидными (startDate < endDate)</li>
 *   <li>Период бронирования не должен пересекаться с существующими резервами</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
public class CreateReservationRequest {

    /**
     * Идентификатор резервируемого оборудования
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему оборудованию</li>
     * </ul>
     */
    @NotNull
    private Long equipmentId;

    /**
     * Идентификатор пользователя, осуществляющего бронирование
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему пользователю</li>
     * </ul>
     */
    @NotNull
    private Long userId;

    /**
     * Идентификатор ответственного лица (может быть null)
     * <p>Используется для случаев, когда бронирование оформляется от лица другого пользователя
     * <p><b>Валидация:</b> Должен соответствовать существующему пользователю, если указан
     */
    private Long responsibleId;

    /**
     * Дата и время начала бронирования
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должно быть раньше endDate</li>
     * </ul>
     */
    @NotNull
    private LocalDateTime startDate;

    /**
     * Дата и время окончания бронирования
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должно быть позже startDate</li>
     * </ul>
     */
    @NotNull
    private LocalDateTime endDate;

    /**
     * Идентификатор статуса резервирования
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему статусу</li>
     * </ul>
     */
    @NotNull
    private Long statusReservationId;
}