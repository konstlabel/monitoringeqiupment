package com.suaistuds.monitoringeqiupment.model.directory;

import com.suaistuds.monitoringeqiupment.model.enums.StatusHistoryName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность записи истории выдачи/возврата оборудования.
 *
 * <p>Соответствует таблице {@code status_history}.
 * Хранит идентификатор и наименование статуса истории из перечисления {@link StatusHistoryName}.</p>
 *
 * @since 2025-07-13
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "status_history")
public class StatusHistory {

    /**
     * Уникальный идентификатор записи.
     * <p>Генерируется базой данных автоматически.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Наименование статуса истории.
     * <p>Хранится как строковое значение перечисления {@link StatusHistoryName}.</p>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private StatusHistoryName name;

}
