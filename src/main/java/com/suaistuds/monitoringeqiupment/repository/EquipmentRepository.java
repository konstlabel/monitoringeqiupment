package com.suaistuds.monitoringeqiupment.repository;

import com.suaistuds.monitoringeqiupment.model.directory.StatusEquipment;
import com.suaistuds.monitoringeqiupment.model.directory.Type;
import com.suaistuds.monitoringeqiupment.model.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Equipment}.
 * Предоставляет методы для доступа и управления данными об оборудовании.
 *
 * <p>Наследует стандартные CRUD-операции от {@link JpaRepository}.
 * Добавляет специализированные методы для поиска оборудования.
 */
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    /**
     * Находит оборудование по серийному номеру.
     *
     * @param serialNumber серийный номер оборудования
     * @return {@link Optional}, содержащий оборудование, если найдено
     */
    Optional<Equipment> findBySerialNumber(String serialNumber);

    /**
     * Находит страницу оборудования по статусу.
     *
     * @param status статус оборудования
     * @param pageable параметры пагинации
     * @return страница с оборудованием ({@link Page})
     */
    Page<Equipment> findByStatus(StatusEquipment status, Pageable pageable);

    /**
     * Находит страницу оборудования по типу.
     *
     * @param type тип оборудования
     * @param pageable параметры пагинации
     * @return страница с оборудованием ({@link Page})
     */
    Page<Equipment> findByType(Type type, Pageable pageable);

    /**
     * Находит страницу оборудования по идентификатору создавшего пользователя.
     *
     * @param id идентификатор пользователя
     * @param pageable параметры пагинации
     * @return страница с оборудованием ({@link Page})
     */
    Page<Equipment> findByCreatedBy(Long id, Pageable pageable);
}


