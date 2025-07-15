package com.suaistuds.monitoringeqiupment.payload.directory;

import com.suaistuds.monitoringeqiupment.model.enums.StatusEquipmentName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для передачи информации о статусе оборудования.
 * Используется для сериализации и передачи данных о состоянии оборудования между слоями приложения.
 *
 * <p>Класс включает следующие Lombok аннотации:
 * <ul>
 *   <li>{@code @Data} - автоматически генерирует стандартные методы (геттеры, сеттеры, toString и др.)</li>
 *   <li>{@code @Builder} - реализует паттерн Builder для удобного создания объектов</li>
 *   <li>{@code @AllArgsConstructor} - создаёт конструктор со всеми полями класса</li>
 *   <li>{@code @NoArgsConstructor} - создаёт конструктор без параметров</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusEquipmentResponse {

    /**
     * Уникальный идентификатор статуса оборудования в системе.
     * Используется для однозначной идентификации статуса в базе данных.
     */
    private Long id;

    /**
     * Наименование статуса оборудования из перечисления {@link StatusEquipmentName}.
     * Определяет текущее состояние оборудования (например, "В работе", "На обслуживании").
     */
    private StatusEquipmentName name;
}