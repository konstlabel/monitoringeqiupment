package com.suaistuds.monitoringequipment.payload.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) для обновления существующего резервирования оборудования.
 * Содержит все необходимые данные для изменения параметров бронирования.
 *
 * <p>Основные характеристики:
 * <ul>
 *   <li>Все поля обязательны для заполнения ({@code @NotNull})</li>
 *   <li>Требует указания идентификатора существующего резервирования (id)</li>
 *   <li>Включает проверку временных промежутков (startDate < endDate)</li>
 * </ul>
 *
 * <p>Отличия от {@link CreateReservationRequest}:
 * <ul>
 *   <li>Содержит обязательное поле id</li>
 *   <li>Поле responsibleId также обязательно</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
public class UpdateReservationRequest {

    /**
     * Идентификатор обновляемого резервирования
     * <p><b>Тип:</b> Long
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующей записи</li>
     * </ul>
     */
    @NotNull
    private Long id;

    /**
     * Идентификатор оборудования
     * <p><b>Тип:</b> Long
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему оборудованию</li>
     * </ul>
     */
    @NotNull
    private Long equipmentId;

    /**
     * Идентификатор пользователя
     * <p><b>Тип:</b> Long
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему пользователю</li>
     * </ul>
     */
    @NotNull
    private Long userId;

    /**
     * Идентификатор ответственного лица
     * <p><b>Тип:</b> Long
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему пользователю</li>
     * </ul>
     */
    @NotNull
    private Long responsibleId;

    /**
     * Новая дата начала бронирования
     * <p><b>Формат:</b> ISO-8601 (yyyy-MM-dd'T'HH:mm:ss)
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должно быть раньше endDate</li>
     *   <li>Должно быть валидной датой в будущем</li>
     * </ul>
     */
    @NotNull
    private LocalDateTime startDate;

    /**
     * Новая дата окончания бронирования
     * <p><b>Формат:</b> ISO-8601 (yyyy-MM-dd'T'HH:mm:ss)
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должно быть позже startDate</li>
     *   <li>Должно быть валидной датой в будущем</li>
     * </ul>
     */
    @NotNull
    private LocalDateTime endDate;

    /**
     * Идентификатор нового статуса резервирования
     * <p><b>Тип:</b> Long
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему статусу</li>
     * </ul>
     */
    @NotNull
    private Long statusReservationId;
}
