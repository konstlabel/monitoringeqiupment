package com.suaistuds.monitoringeqiupment.model.directory;

import com.suaistuds.monitoringeqiupment.model.enums.TypeName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

/**
 * Сущность типа оборудования.
 *
 * <p>Соответствует таблице {@code type}.
 * Хранит идентификатор и наименование типа из перечисления {@link TypeName}.</p>
 *
 * @since 2025-07-13
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "type")
public class Type {

    /**
     * Уникальный идентификатор записи.
     * <p>Генерируется базой данных автоматически.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Наименование типа оборудования.
     * <p>Хранится как строковое значение перечисления {@link TypeName}.
     * Является натуральным идентификатором записи.</p>
     */
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = "name")
    private TypeName name;
}
