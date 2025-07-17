package com.suaistuds.monitoringequipment.repository;


import com.suaistuds.monitoringequipment.model.directory.StatusHistory;
import com.suaistuds.monitoringequipment.model.enums.StatusHistoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link StatusHistory}.
 * Предоставляет методы для доступа к данным о статусах в истории изменений оборудования.
 *
 * <p>Наследует базовые CRUD-операции от {@link JpaRepository<StatusHistory, Long>}
 * и добавляет специализированные методы для работы со статусами истории.
 */
@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {

    /**
     * Находит статус истории по его наименованию.
     *
     * @param name наименование статуса из перечисления {@link StatusHistoryName}
     * @return {@link Optional}, содержащий статус истории, если найден,
     *         или пустой Optional, если статус с указанным именем не существует
     */
    Optional<StatusHistory> findByName(StatusHistoryName name);
}
