package com.suaistuds.monitoringequipment.repository;

import com.suaistuds.monitoringequipment.model.directory.Type;
import com.suaistuds.monitoringequipment.model.enums.TypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Type}.
 * Предоставляет методы доступа к данным о типах оборудования в системе.
 *
 * <p>Наследует базовые CRUD-операции от {@link JpaRepository<Type, Long>}
 * и добавляет специализированные методы для работы с типами оборудования.
 */
@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    /**
     * Находит тип оборудования по его наименованию.
     *
     * @param name наименование типа из перечисления {@link TypeName}
     * @return {@link Optional}, содержащий тип оборудования если найден,
     *         либо пустой Optional если тип с указанным именем отсутствует
     */
    Optional<Type> findByName(TypeName name);
}
