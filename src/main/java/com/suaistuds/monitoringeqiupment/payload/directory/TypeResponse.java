package com.suaistuds.monitoringeqiupment.payload.directory;

import com.suaistuds.monitoringeqiupment.model.enums.TypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) для передачи информации о типах оборудования.
 * Предоставляет структурированные данные о категориях/типах оборудования в системе.
 *
 * <p>Особенности реализации:
 * <ul>
 *   <li>{@code @Data} - автоматически генерирует стандартные методы (геттеры, сеттеры, equals, hashCode, toString)</li>
 *   <li>{@code @Builder} - реализует паттерн Builder для удобного создания объектов</li>
 *   <li>{@code @AllArgsConstructor} - создает конструктор со всеми полями класса</li>
 *   <li>{@code @NoArgsConstructor} - создает конструктор по умолчанию без параметров</li>
 * </ul>
 *
 * @see TypeName Перечисление возможных типов оборудования
 *
 * @since 2025-07-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TypeResponse {

    /**
     * Уникальный идентификатор типа оборудования в системе.
     * Используется для однозначной идентификации категории оборудования.
     */
    private Long id;

    /**
     * Наименование типа оборудования из перечисления {@link TypeName}.
     * Определяет категорию оборудования (например: "Ноутбук", "Сервер", "Сетевое оборудование").
     * Значения должны соответствовать бизнес-логике системы.
     */
    private TypeName name;
}