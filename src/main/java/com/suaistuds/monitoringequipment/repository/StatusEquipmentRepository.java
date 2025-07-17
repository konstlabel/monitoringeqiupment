package com.suaistuds.monitoringequipment.repository;

import com.suaistuds.monitoringequipment.model.directory.StatusEquipment;
import com.suaistuds.monitoringequipment.model.enums.StatusEquipmentName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Репозиторий для работы с сущностью {@link StatusEquipment}.
 * Предоставляет методы для доступа к данным о статусах оборудования.
 *
 * <p>Наследует стандартные CRUD-операции от {@link JpaRepository}
 * и добавляет специализированные методы для поиска статусов оборудования.
 */
@Repository
public interface StatusEquipmentRepository extends JpaRepository<StatusEquipment, Long> {

    /**
     * Находит статус оборудования по его наименованию.
     *
     * @param name наименование статуса из перечисления {@link StatusEquipmentName}
     * @return {@link Optional}, содержащий статус оборудования, если найден
     */
    Optional<StatusEquipment> findByName(StatusEquipmentName name);
}
