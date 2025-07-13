package com.suaistuds.monitoringeqiupment.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;


/**
 * Абстрактный базовый класс для сущностей, которым необходимо хранить
 * информацию о времени создания и последнего обновления.
 *
 * <p>Аннотации:
 * <ul>
 *   <li>{@code @MappedSuperclass} — указывает, что данная сущность не имеет
 *       собственной таблицы, а предоставляет поля потомкам.</li>
 *   <li>{@code @EntityListeners(AuditingEntityListener.class)} — включает
 *       автоматическое заполнение полей {@code createdAt} и {@code updatedAt}.</li>
 *   <li>{@code @JsonIgnoreProperties} — скрывает эти поля в JSON (при сериализации
 *       они не будут включаться в выходной документ), но остаётся возможность
 *       читать их через геттеры.</li>
 *   <li>{@code @Data} (Lombok) — автоматически генерирует геттеры, сеттеры,
 *       {@code equals}, {@code hashCode}, {@code toString}.</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = { "createdAt", "updatedAt" },
        allowGetters = true
)
public abstract class DateAudit implements Serializable {

    /**
     * Версия для сериализации, чтобы гарантировать совместимость при
     * десериализации объектов разных версий класса.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Временная метка, когда сущность была создана.
     * <p>Заполняется автоматически при первом сохранении сущности.
     *
     * @see org.springframework.data.annotation.CreatedDate
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Временная метка последнего обновления сущности.
     * <p>Автоматически обновляется при каждом изменении и сохранении сущности.
     *
     * @see org.springframework.data.annotation.LastModifiedDate
     */
    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
}