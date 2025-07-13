package com.suaistuds.monitoringeqiupment.model.directory;

import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Сущность роли пользователя в системе.
 *
 * <p>Соответствует таблице {@code role}.
 * Хранит идентификатор и наименование роли из перечисления {@link RoleName}.</p>
 *
 * @since 2025-07-13
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "role")
public class Role {

    /**
     * Уникальный идентификатор записи.
     * <p>Генерируется базой данных автоматически.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Наименование роли.
     * <p>Хранится как строковое значение перечисления {@link RoleName}.
     * Является натуральным идентификатором записи.</p>
     */
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = "name")
    private RoleName name;
}