package com.suaistuds.monitoringeqiupment.repository;

import com.suaistuds.monitoringeqiupment.model.directory.StatusReservation;
import com.suaistuds.monitoringeqiupment.model.entity.Equipment;
import com.suaistuds.monitoringeqiupment.model.entity.Reservation;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Page<Reservation> findByEquipment(Equipment equipment, Pageable pageable);

    Page<Reservation> findByUser(User user, Pageable pageable);

    Page<Reservation> findByResponsible(User user, Pageable pageable);

    Page<Reservation> findByStatusReservation(StatusReservation statusReservation, Pageable pageable);

    Page<Reservation> findByStartDate(LocalDateTime startDate, Pageable pageable);

    Page<Reservation> findByEndDate(LocalDateTime endDate, Pageable pageable);

    Page<Reservation> findByStartDateBetween(LocalDateTime startDateFrom, LocalDateTime startDateTo, Pageable pageable);

    Page<Reservation> findByEndDateBetween(LocalDateTime endDateFrom, LocalDateTime endDateTo, Pageable pageable);

    Page<Reservation> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDateTime date, LocalDateTime sameDate, Pageable pageable);

    Page<Reservation> findByEquipmentAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Equipment equipment, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
