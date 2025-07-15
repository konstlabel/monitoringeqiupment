package com.suaistuds.monitoringeqiupment.payload.audit;

import lombok.Data;

import java.time.Instant;

/**
 * Абстрактный класс для аудита дат создания и обновления.
 * Содержит общие поля для отслеживания временных меток создания и обновления сущностей.
 * Используется как базовый класс для DTO (Data Transfer Objects), требующих аудита дат.
 *
 * <p> Поля:
 * - createdAt - временная метка создания записи
 * - updatedAt - временная метка последнего обновления записи </p>
 *
 * <p> Аннотация {@code @Data} из проекта Lombok автоматически генерирует:
 * - геттеры и сеттеры для всех полей
 * - методы toString(), equals() и hashCode() </p>
 *
 * @since 2025-07-13
 */
@Data
public abstract class DateAuditPayload {

    /**
     * Дата и время создания записи в формате Instant.
     * Заполняется автоматически при создании сущности.
     */
    private Instant createdAt;

    /**
     * Дата и время последнего обновления записи в формате Instant.
     * Заполняется автоматически при каждом обновлении сущности.
     */
    private Instant updatedAt;
}