package com.suaistuds.monitoringequipment.payload.Equipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) для запроса на обновление существующего оборудования.
 * Содержит валидируемые поля с информацией для обновления записи оборудования.
 *
 * <p>Основные характеристики:
 * <ul>
 *   <li>Включает все обязательные поля для идентификации и обновления оборудования</li>
 *   <li>Реализует строгую валидацию входных данных через Jakarta Bean Validation</li>
 *   <li>Автоматически генерирует методы доступа через Lombok {@code @Data}</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
public class UpdateEquipmentRequest {

    /**
     * Уникальный идентификатор обновляемого оборудования.
     * <p>Валидация:
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     * </ul>
     */
    @NotNull
    private Long id;

    /**
     * Обновляемое наименование оборудования.
     * <p>Валидация:
     * <ul>
     *   <li>Обязательное поле ({@code @NotBlank})</li>
     *   <li>Длина от 3 до 100 символов ({@code @Size})</li>
     * </ul>
     */
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    /**
     * Серийный номер оборудования.
     * <p>Валидация:
     * <ul>
     *   <li>Обязательное поле ({@code @NotBlank})</li>
     *   <li>Должен быть уникальным в системе (кроме текущей записи)</li>
     * </ul>
     */
    @NotBlank
    private String serialNumber;

    /**
     * Идентификатор нового статуса оборудования.
     * <p>Валидация:
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему статусу в системе</li>
     * </ul>
     */
    @NotNull
    private Long statusId;

    /**
     * Идентификатор типа оборудования.
     * <p>Валидация:
     * <ul>
     *   <li>Обязательное поле ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему типу в системе</li>
     * </ul>
     */
    @NotNull
    private Long typeId;
}
