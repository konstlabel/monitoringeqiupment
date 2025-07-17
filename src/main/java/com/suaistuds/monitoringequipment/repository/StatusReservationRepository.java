package com.suaistuds.monitoringequipment.repository;

import com.suaistuds.monitoringequipment.model.directory.StatusReservation;
import com.suaistuds.monitoringequipment.model.enums.StatusReservationName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link StatusReservation}.
 * Предоставляет методы доступа к данным о статусах резервирования оборудования.
 *
 * <p>Наследует стандартные CRUD-операции от {@link JpaRepository}
 * и добавляет специализированные методы для работы со статусами резервирования.
 */
@Repository
public interface StatusReservationRepository extends JpaRepository<StatusReservation, Long> {

    /**
     * Находит статус резервирования по его наименованию.
     *
     * @param name наименование статуса из перечисления {@link StatusReservationName}
     * @return {@link Optional}, содержащий статус резервирования если найден,
     *         либо пустой Optional если статус с указанным именем отсутствует
     */
    Optional<StatusReservation> findByName(StatusReservationName name);
}
