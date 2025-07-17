package com.suaistuds.monitoringequipment.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.suaistuds.monitoringequipment.model.audit.UserDateAudit;
import com.suaistuds.monitoringequipment.model.directory.StatusEquipment;
import com.suaistuds.monitoringequipment.model.directory.Type;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

/**
 * Сущность оборудования.
 *
 * <p>Наследует аудирование по датам и пользователям из {@link UserDateAudit}:
 * автоматически хранит информацию о времени создания/изменения и об авторах операций.</p>
 *
 * <p>Аннотации класса:
 * <ul>
 *   <li>{@code @Entity} — отмечает как JPA-сущность;</li>
 *   <li>{@code @Table(name = "equipment", uniqueConstraints = @UniqueConstraint(columnNames = "serial_number"))}
 *       — задаёт имя таблицы и уникальное ограничение на серийный номер;</li>
 *   <li>{@code @EqualsAndHashCode(callSuper = true)} — включает в {@code equals/hashCode}
 *       поля из родительского класса;</li>
 *   <li>{@code @Data} — Lombok-генерация геттеров/сеттеров, {@code toString}, {@code equals}, {@code hashCode};</li>
 *   <li>{@code @Builder} — поддержка построителя объекта (Builder pattern);</li>
 *   <li>{@code @NoArgsConstructor}, {@code @AllArgsConstructor} — автогенерация конструкторов без аргументов
 *       и со всеми полями;</li>
 *   <li>{@code @JsonIdentityInfo} — конфигурирует сериализацию в JSON для корректного обхода циклических ссылок.</li>
 * </ul>
 * </p>
 *
 * @see StatusEquipment
 * @see Type
 * @since 2025-07-13
 */
@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "equipment", uniqueConstraints = { @UniqueConstraint(columnNames = {"serial_number"}) })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Equipment extends UserDateAudit {

    /**
     * Версия для сериализации, чтобы гарантировать совместимость при
     * десериализации объектов разных версий класса.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Уникальный идентификатор оборудования.
     * <p>Генерируется автоматически базой данных при вставке записи.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Наименование оборудования. */
    @Column(name = "name")
    private String name;

    /**
     * Серийный номер оборудования.
     * <p>Уникален в таблице благодаря ограничению {@code uniqueConstraints}.</p>
     */
    @Column(name = "serial_number")
    private String serialNumber;

    /**
     * Текущий статус оборудования.
     *
     * @see StatusEquipment
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private StatusEquipment status;

    /**
     * Тип оборудования (камера, объектив, и т.д.).
     *
     * @see Type
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private Type type;
}