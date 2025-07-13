package com.suaistuds.monitoringeqiupment.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.suaistuds.monitoringeqiupment.model.audit.DateAudit;
import com.suaistuds.monitoringeqiupment.model.directory.Role;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

/**
 * Сущность пользователя системы.
 *
 * <p>Хранит логин, email, пароль и роль пользователя. Наследует аудирование по датам
 * из {@link DateAudit} (без информации о пользователе-авторе).</p>
 *
 * <p>Аннотации класса:
 * <ul>
 *   <li>{@code @Entity} — JPA-сущность;</li>
 *   <li>{@code @Table(name = "user", uniqueConstraints = {...})} — имя таблицы
 *       и уникальные ограничения на поля {@code username} и {@code email};</li>
 *   <li>{@code @EqualsAndHashCode(callSuper = true)} — включает в сравнение
 *       поля родителя;</li>
 *   <li>{@code @Data}, {@code @Builder}, {@code @NoArgsConstructor}, {@code @AllArgsConstructor} — Lombok;</li>
 *   <li>{@code @JsonIdentityInfo} — сериализация с сохранением ссылок по {@code id}.</li>
 * </ul>
 * </p>
 *
 * @see Role
 * @since 2025-07-13
 */
@Entity
@EqualsAndHashCode(callSuper = true)
@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user",
        uniqueConstraints =  { @UniqueConstraint(columnNames = { "username" }), @UniqueConstraint(columnNames = { "email" }) })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends DateAudit {

    /**
     * Версия для сериализации, чтобы гарантировать совместимость при
     * десериализации объектов разных версий класса.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Уникальный идентификатор пользователя. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Логин пользователя (уникальный). */
    @Column(name = "username")
    private String username;

    /** Электронная почта пользователя (уникальная). */
    @Column(name = "email")
    private String email;

    /** Хешированный пароль пользователя. */
    @Column(name = "password")
    private String password;

    /** Роль пользователя в системе. {@link Role} */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
}
