package com.suaistuds.monitoringeqiupment.payload.Equipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.suaistuds.monitoringeqiupment.payload.audit.UserDateAuditPayload;
import lombok.*;

/**
 * DTO (Data Transfer Object) для представления информации об оборудовании.
 * Расширяет {@link UserDateAuditPayload}, добавляя аудит пользователей и временных меток.
 *
 * <p>Используется для сериализации и передачи данных об оборудовании между слоями приложения,
 * включая информацию о создании/изменении записи.
 *
 * <p>Особенности реализации:
 * <ul>
 *   <li>{@code @EqualsAndHashCode(callSuper = true)} - включает поля родительского класса в equals/hashCode</li>
 *   <li>{@code @Data} - автоматически генерирует геттеры, сеттеры, toString()</li>
 *   <li>{@code @JsonInclude(Include.NON_NULL)} - исключает null-поля при сериализации JSON</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentResponse extends UserDateAuditPayload {

    /**
     * Уникальный идентификатор оборудования в системе
     */
    private Long id;

    /**
     * Наименование оборудования
     */
    private String name;

    /**
     * Уникальный серийный номер оборудования
     */
    private String serialNumber;

    /**
     * Идентификатор текущего статуса оборудования
     * <p>Соответствует записи в справочнике статусов
     */
    private Long statusId;

    /**
     * Идентификатор типа оборудования
     * <p>Соответствует записи в справочнике типов оборудования
     */
    private Long typeId;
}