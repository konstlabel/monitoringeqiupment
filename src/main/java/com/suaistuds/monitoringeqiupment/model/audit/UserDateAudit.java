package com.suaistuds.monitoringeqiupment.model.audit;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.io.Serial;

/**
 * Абстрактный базовый класс для сущностей, которым необходимо хранить
 * информацию о пользователе, создавшем и последнем изменившем запись,
 * поверх метаданных о датах создания и обновления из {@link DateAudit}.
 *
 * <p>Аннотации:
 * <ul>
 *   <li>{@code @MappedSuperclass} — указывает, что данная сущность не имеет
 *       собственной таблицы, а предоставляет поля потомкам.</li>
 *   <li>{@code @EqualsAndHashCode(callSuper = true)} — генерирует методы {@code equals}
 *       и {@code hashCode}, включая поля родительского класса {@link DateAudit}.</li>
 *   <li>{@code @Data} (Lombok) — автоматически генерирует геттеры, сеттеры,
 *       {@code equals}, {@code hashCode}, {@code toString}.</li>
 *   <li>{@code @JsonIgnoreProperties} — скрывает поля {@code createdBy} и {@code updatedBy}
 *       при сериализации в JSON, но остаётся возможность читать их через геттеры.</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
@JsonIgnoreProperties(
        value = { "createdBy", "updatedBy" },
        allowGetters = true
)
public abstract class UserDateAudit extends DateAudit {

    /**
     * Версия для сериализации, чтобы гарантировать совместимость при
     * десериализации объектов разных версий класса.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Идентификатор пользователя, создавшего сущность.
     * <p>Поле заполняется автоматически фреймворком аудитинга Spring Data.
     *
     * @see org.springframework.data.annotation.CreatedBy
     */
    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    /**
     * Идентификатор пользователя, который в последний раз изменил сущность.
     * <p>Поле обновляется автоматически при каждом сохранении сущности.
     *
     * @see org.springframework.data.annotation.LastModifiedBy
     */
    @LastModifiedBy
    private Long updatedBy;
}