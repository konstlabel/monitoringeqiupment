package com.suaistuds.monitoringeqiupment.model.directory;

import com.suaistuds.monitoringeqiupment.model.enums.StatusReservationName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

/**
 * Сущность статуса резервации оборудования.
 *
 * <p>Соответствует таблице {@code status_reservation}.
 * Хранит идентификатор и наименование статуса из перечисления {@link StatusReservationName}.</p>
 *
 * @since 2025-07-13
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "status_reservation")
public class StatusReservation {

    /**
     * Уникальный идентификатор записи.
     * <p>Генерируется базой данных автоматически.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Наименование статуса резервации.
     * <p>Хранится как строковое значение перечисления {@link StatusReservationName}.
     * Является натуральным идентификатором записи.</p>
     */
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = "name")
    private StatusReservationName name;
}
