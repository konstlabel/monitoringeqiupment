package com.suaistuds.monitoringequipment.repository;

import com.suaistuds.monitoringequipment.model.directory.StatusReservation;
import com.suaistuds.monitoringequipment.model.entity.Equipment;
import com.suaistuds.monitoringequipment.model.entity.Reservation;
import com.suaistuds.monitoringequipment.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Reservation}.
 * Предоставляет методы для доступа к данным о резервировании оборудования.
 *
 * <p>Наследует стандартные CRUD-операции от {@link JpaRepository}
 * и добавляет специализированные методы для поиска записей о резервировании.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Находит страницу резервирований по оборудованию.
     *
     * @param equipment оборудование, для которого ищутся резервирования
     * @param pageable параметры пагинации
     * @return страница с резервированиями ({@link Page})
     */
    Page<Reservation> findByEquipment(Equipment equipment, Pageable pageable);

    /**
     * Находит страницу резервирований по пользователю.
     *
     * @param user пользователь, оформивший резервирование
     * @param pageable параметры пагинации
     * @return страница с резервированиями ({@link Page})
     */
    Page<Reservation> findByUser(User user, Pageable pageable);

    /**
     * Находит страницу резервирований по ответственному лицу.
     *
     * @param user ответственное лицо за резервирование
     * @param pageable параметры пагинации
     * @return страница с резервированиями ({@link Page})
     */
    Page<Reservation> findByResponsible(User user, Pageable pageable);

    /**
     * Находит страницу резервирований по статусу.
     *
     * @param statusReservation статус резервирования
     * @param pageable параметры пагинации
     * @return страница с резервированиями ({@link Page})
     */
    Page<Reservation> findByStatusReservation(StatusReservation statusReservation, Pageable pageable);

    /**
     * Находит страницу резервирований по дате начала.
     *
     * @param startDate дата начала резервирования
     * @param pageable параметры пагинации
     * @return страница с резервированиями ({@link Page})
     */
    Page<Reservation> findByStartDate(LocalDateTime startDate, Pageable pageable);

    /**
     * Находит страницу резервирований по дате окончания.
     *
     * @param endDate дата окончания резервирования
     * @param pageable параметры пагинации
     * @return страница с резервированиями ({@link Page})
     */
    Page<Reservation> findByEndDate(LocalDateTime endDate, Pageable pageable);

    /**
     * Находит страницу резервирований, начавшихся в указанный период.
     *
     * @param startDateFrom начальная дата периода
     * @param startDateTo конечная дата периода
     * @param pageable параметры пагинации
     * @return страница с резервированиями ({@link Page})
     */
    Page<Reservation> findByStartDateBetween(LocalDateTime startDateFrom, LocalDateTime startDateTo, Pageable pageable);

    /**
     * Находит страницу резервирований, завершившихся в указанный период.
     *
     * @param endDateFrom начальная дата периода
     * @param endDateTo конечная дата периода
     * @param pageable параметры пагинации
     * @return страница с резервированиями ({@link Page})
     */
    Page<Reservation> findByEndDateBetween(LocalDateTime endDateFrom, LocalDateTime endDateTo, Pageable pageable);

    /**
     * Находит страницу активных резервирований на указанную дату.
     *
     * @param date дата для проверки активности резервирования
     * @param sameDate та же дата для формирования диапазона
     * @param pageable параметры пагинации
     * @return страница с активными резервированиями ({@link Page})
     */
    Page<Reservation> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDateTime date, LocalDateTime sameDate, Pageable pageable);

    /**
     * Находит страницу активных резервирований оборудования в указанный период.
     *
     * @param equipment оборудование для проверки
     * @param start начальная дата периода
     * @param end конечная дата периода
     * @param pageable параметры пагинации
     * @return страница с активными резервированиями ({@link Page})
     */
    Page<Reservation> findByEquipmentAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Equipment equipment, LocalDateTime start, LocalDateTime end, Pageable pageable);

    /**
     * Проверяет, есть ли резервации, пересекающиеся с указанным интервалом.
     * Условие пересечения: существующая.start <= newEnd && existing.end >= newStart
     *
     * @param equipment оборудование
     * @param newStart  новая дата начала
     * @param newEnd    новая дата окончания
     * @return список пересекающихся резерваций
     */
    @Query("""
       select r from Reservation r
       where r.equipment = :equipment
         and r.startDate <= :newEnd
         and r.endDate   >= :newStart
         and (:excludeId is null or r.id <> :excludeId)
    """)
    List<Reservation> findOverlapping(
            @Param("equipment")   Equipment equipment,
            @Param("newStart")    LocalDateTime newStart,
            @Param("newEnd")      LocalDateTime newEnd,
            @Param("excludeId")   Long excludeId  // для update: не учитывать текущую резервацию
    );
}
