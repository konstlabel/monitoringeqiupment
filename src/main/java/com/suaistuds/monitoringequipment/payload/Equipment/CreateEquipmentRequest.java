package com.suaistuds.monitoringequipment.payload.Equipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) для запроса на создание нового оборудования.
 * Содержит валидируемые поля с базовой информацией о добавляемом оборудовании.
 *
 * <p>Используется для передачи данных от клиента к серверу при создании
 * новой записи об оборудовании в системе мониторинга.
 *
 * <p>Класс включает следующие валидации:
 * <ul>
 *   <li>Автоматическую генерацию методов доступа через {@code @Data}</li>
 *   <li>Проверку обязательности полей через Jakarta Validation</li>
 *   <li>Валидацию длины строковых полей</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
public class CreateEquipmentRequest {

    /**
     * Наименование оборудования.
     * <p>Ограничения:
     * <ul>
     *   <li>Не может быть пустым ({@code @NotBlank})</li>
     *   <li>Длина от 3 до 100 символов ({@code @Size})</li>
     * </ul>
     *
     */
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    /**
     * Серийный номер оборудования.
     * <p>Ограничения:
     * <ul>
     *   <li>Не может быть пустым ({@code @NotBlank})</li>
     *   <li>Должен быть уникальным в системе</li>
     * </ul>
     *
     */
    @NotBlank
    private String serialNumber;

    /**
     * Идентификатор текущего статуса оборудования.
     * <p>Ограничения:
     * <ul>
     *   <li>Не может быть null ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему статусу в системе</li>
     * </ul>
     *
     */
    @NotNull
    private Long statusId;

    /**
     * Идентификатор типа оборудования.
     * <p>Ограничения:
     * <ul>
     *   <li>Не может быть null ({@code @NotNull})</li>
     *   <li>Должен соответствовать существующему типу в системе</li>
     * </ul>
     *
     */
    @NotNull
    private Long typeId;
}