package com.suaistuds.monitoringeqiupment.payload.directory;

import com.suaistuds.monitoringeqiupment.model.enums.StatusReservationName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для передачи информации о статусе резервирования оборудования.
 * Используется для сериализации данных о состоянии бронирования между слоями приложения.
 *
 * <p>Класс реализует следующие возможности через Lombok аннотации:
 * <ul>
 *   <li>{@code @Data} - автоматическая генерация методов доступа (геттеры/сеттеры),
 *       toString(), equals() и hashCode()</li>
 *   <li>{@code @Builder} - поддержка паттерна Builder для удобного конструирования объектов</li>
 *   <li>{@code @AllArgsConstructor} - конструктор со всеми полями класса</li>
 *   <li>{@code @NoArgsConstructor} - конструктор по умолчанию</li>
 * </ul>
 *
 * @see StatusReservationName Перечисление возможных статусов резервирования
 *
 * @since 2025-07-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusReservationResponse {

    /**
     * Уникальный идентификатор статуса резервирования.
     * Используется для однозначной идентификации записи в системе.
     */
    private Long id;

    /**
     * Текущий статус резервирования из перечисления {@link StatusReservationName}.
     * Определяет состояние бронирования.
     */
    private StatusReservationName name;
}