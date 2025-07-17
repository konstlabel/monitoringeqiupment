package com.suaistuds.monitoringequipment.payload.directory;

import com.suaistuds.monitoringequipment.model.enums.StatusHistoryName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для передачи данных об истории изменений статусов оборудования.
 * Предоставляет информацию о различных состояниях оборудования в хронологическом порядке.
 *
 * <p>Класс включает следующие Lombok аннотации для автоматической генерации кода:
 * <ul>
 *   <li>{@code @Data} - генерирует геттеры, сеттеры, toString(), equals() и hashCode()</li>
 *   <li>{@code @Builder} - реализует паттерн Builder для поэтапного создания объектов</li>
 *   <li>{@code @AllArgsConstructor} - создает конструктор со всеми полями класса</li>
 *   <li>{@code @NoArgsConstructor} - создает конструктор по умолчанию без параметров</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusHistoryResponse {

    /**
     * Уникальный идентификатор записи в истории статусов.
     * Используется для однозначной идентификации исторического события.
     */
    private Long id;

    /**
     * Наименование статуса из перечисления {@link StatusHistoryName}.
     * Определяет конкретное состояние оборудования в определенный момент времени.
     * Может включать такие значения как "Ремонт начат", "Ремонт завершен" и др.
     */
    private StatusHistoryName name;
}