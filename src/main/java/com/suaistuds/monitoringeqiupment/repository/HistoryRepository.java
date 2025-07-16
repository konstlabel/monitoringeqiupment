package com.suaistuds.monitoringeqiupment.repository;

import com.suaistuds.monitoringeqiupment.model.directory.StatusHistory;
import com.suaistuds.monitoringeqiupment.model.entity.Equipment;
import com.suaistuds.monitoringeqiupment.model.entity.History;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Репозиторий для работы с сущностью {@link History}.
 * Предоставляет методы для доступа к данным об истории изменений оборудования.
 *
 * <p>Наследует стандартные CRUD-операции от {@link JpaRepository}
 * и добавляет специализированные методы для поиска записей истории.
 */
@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    /**
     * Находит страницу записей истории по связанному оборудованию.
     *
     * @param equipment оборудование, для которого ищется история
     * @param pageable параметры пагинации
     * @return страница с записями истории ({@link Page})
     */
    Page<History> findByEquipment(Equipment equipment, Pageable pageable);

    /**
     * Находит страницу записей истории по пользователю, инициировавшему изменение.
     *
     * @param user пользователь, инициировавший изменения
     * @param pageable параметры пагинации
     * @return страница с записями истории ({@link Page})
     */
    Page<History> findByUser(User user, Pageable pageable);

    /**
     * Находит страницу записей истории по ответственному лицу.
     *
     * @param responsible ответственное лицо за изменение
     * @param pageable параметры пагинации
     * @return страница с записями истории ({@link Page})
     */
    Page<History> findByResponsible(User responsible, Pageable pageable);

    /**
     * Находит страницу записей истории по статусу изменения.
     *
     * @param statusHistory статус изменения в истории
     * @param pageable параметры пагинации
     * @return страница с записями истории ({@link Page})
     */
    Page<History> findByStatusHistory(StatusHistory statusHistory, Pageable pageable);

    /**
     * Находит страницу записей истории по точной дате изменения.
     *
     * @param date точная дата изменения
     * @param pageable параметры пагинации
     * @return страница с записями истории ({@link Page})
     */
    Page<History> findByDate(LocalDateTime date, Pageable pageable);

    /**
     * Находит страницу записей истории за указанный период времени.
     *
     * @param startDate начальная дата периода
     * @param endDate конечная дата периода
     * @param pageable параметры пагинации
     * @return страница с записями истории ({@link Page})
     */
    Page<History> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
