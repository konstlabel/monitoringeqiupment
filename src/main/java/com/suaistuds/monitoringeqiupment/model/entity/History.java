package com.suaistuds.monitoringeqiupment.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.suaistuds.monitoringeqiupment.model.audit.UserDateAudit;
import com.suaistuds.monitoringeqiupment.model.directory.StatusHistory;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * Сущность истории операций с оборудованием.
 *
 * <p>Хранит записи о выдаче, возврате и других изменениях статуса оборудования,
 * а также информацию об ответственном пользователе и времени операции.</p>
 *
 * <p>Аннотации класса:
 * <ul>
 *   <li>{@code @Entity} — JPA-сущность;</li>
 *   <li>{@code @EqualsAndHashCode(callSuper = true)} — включает поля родителя в сравнение;</li>
 *   <li>{@code @Data}, {@code @Builder}, {@code @NoArgsConstructor}, {@code @AllArgsConstructor} — Lombok;</li>
 *   <li>{@code @JsonIdentityInfo} — для сериализации с сохранением ссылок по свойству {@code id}.</li>
 * </ul>
 * </p>
 *
 * @see StatusHistory
 * @since 2025-07-13
 */
@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class History extends UserDateAudit {

    /**
     * Версия для сериализации, чтобы гарантировать совместимость при
     * десериализации объектов разных версий класса.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Уникальный идентификатор записи истории, генерируется автоматически. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Оборудование, к которому относится данная запись истории. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    /** Пользователь, для которого выполнялась операция. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** Ответственный пользователь, выполнивший операцию. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_id")
    private User responsible;

    /** Статус операции из справочника {@link StatusHistory}. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_history_id")
    private StatusHistory statusHistory;

    /** Дата и время операции. */
    @Column(name = "date")
    private LocalDateTime date;
}