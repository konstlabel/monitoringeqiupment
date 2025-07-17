package com.suaistuds.monitoringequipment.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.suaistuds.monitoringequipment.model.audit.UserDateAudit;
import com.suaistuds.monitoringequipment.model.directory.StatusReservation;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * Сущность бронирования оборудования.
 *
 * <p>Фиксирует периоды аренды и статус резервации, а также ответственных пользователей.</p>
 *
 * <p>Аннотации класса:
 * <ul>
 *   <li>{@code @Entity} — JPA-сущность;</li>
 *   <li>{@code @EqualsAndHashCode(callSuper = true)} — сравнение включает поля родителя;</li>
 *   <li>{@code @Data}, {@code @Builder}, {@code @NoArgsConstructor}, {@code @AllArgsConstructor} — Lombok;</li>
 *   <li>{@code @Table(name = "reservation")} — имя таблицы;</li>
 *   <li>{@code @JsonIdentityInfo} — сериализация ссылок по {@code id}.</li>
 * </ul>
 * </p>
 *
 * @see StatusReservation
 * @since 2025-07-13
 */
@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Reservation extends UserDateAudit {

    /**
     * Версия для сериализации, чтобы гарантировать совместимость при
     * десериализации объектов разных версий класса.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Уникальный идентификатор брони. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Оборудование, которое бронируется. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    /** Пользователь, оформивший бронирование. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** Ответственный за подтверждение или отмену брони. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "responsible_id")
    private User responsible;

    /** Дата и время начала бронирования. */
    @Column (name = "start_date")
    private LocalDateTime startDate;

    /** Дата и время окончания бронирования. */
    @Column (name = "end_date")
    private LocalDateTime endDate;

    /** Текущий статус бронирования из справочника {@link StatusReservation}. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_reservation_id")
    private StatusReservation statusReservation;
}