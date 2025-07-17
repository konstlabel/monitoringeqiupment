package com.suaistuds.monitoringequipment.model.directory;

import com.suaistuds.monitoringequipment.model.enums.StatusEquipmentName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

/**
 * Сущность статуса оборудования.
 *
 * <p>Соответствует таблице {@code status}.
 * Хранит идентификатор и наименование статуса из перечисления {@link StatusEquipmentName}.</p>
 *
 * @since 2025-07-13
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "status")
public class StatusEquipment {

    /**
     * Уникальный идентификатор записи.
     * <p>Генерируется базой данных автоматически.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Наименование статуса оборудования.
     * <p>Хранится как строковое значение перечисления {@link StatusEquipmentName}.
     * Является натуральным идентификатором записи.</p>
     */
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = "name")
    private StatusEquipmentName name;
}
