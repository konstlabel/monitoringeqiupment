package com.suaistuds.monitoringeqiupment.payload.audit;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Абстрактный класс для расширенного аудита сущностей с информацией о пользователях.
 * Наследует базовые поля аудита дат от {@link DateAuditPayload} и добавляет информацию
 * о пользователях, создавших и обновивших запись.
 *
 * <p>Используется как базовый класс для DTO, требующих отслеживания не только временных
 * меток, но и информации о пользователях, выполнявших действия.</p>
 *
 * <p>Аннотация {@code @EqualsAndHashCode(callSuper = true)} обеспечивает корректное
 * включение полей родительского класса в методы equals() и hashCode().</p>
 *
 * <p>Аннотация {@code @Data} автоматически генерирует:
 * <ul>
 *   <li>Геттеры и сеттеры для всех полей</li>
 *   <li>Методы toString(), equals() и hashCode()</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class UserDateAuditPayload extends DateAuditPayload {

    /**
     * Идентификатор пользователя, создавшего запись.
     * Должен заполняться автоматически при создании сущности.
     */
    private Long createdBy;

    /**
     * Идентификатор пользователя, выполнившего последнее обновление записи.
     * Должен обновляться автоматически при каждом изменении сущности.
     */
    private Long updatedBy;
}