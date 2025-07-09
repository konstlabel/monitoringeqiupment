package com.suaistuds.monitoringeqiupment.repository;

import com.suaistuds.monitoringeqiupment.model.entity.Reservation;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
        SELECT r FROM Reservation r
        WHERE (:statusReservationId IS NULL OR r.statusReservation.id = :statusReservationId)
          AND (:startFrom IS NULL OR r.startDate >= :startFrom)
          AND (:startTo IS NULL OR r.startDate <= :startTo)
          AND (:endFrom IS NULL OR r.endDate >= :endFrom)
          AND (:endTo IS NULL OR r.endDate <= :endTo)
          AND (:serialFragment IS NULL OR LOWER(r.equipment.serialNumber) LIKE LOWER(CONCAT('%', :serialFragment, '%')))
          AND (:userFragment IS NULL OR LOWER(r.user.username) LIKE LOWER(CONCAT('%', :userFragment, '%')))
          AND (:responsibleFragment IS NULL OR LOWER(r.responsible.username) LIKE LOWER(CONCAT('%', :responsibleFragment, '%')))
    """)
    List<Reservation> searchReservation(
            @Param("statusReservationId") Long statusReservationId,
            @Param("startFrom") LocalDateTime startFrom,
            @Param("startTo") LocalDateTime startTo,
            @Param("endFrom") LocalDateTime endFrom,
            @Param("endTo") LocalDateTime endTo,
            @Param("serialFragment") String serialFragment,
            @Param("userFragment") String userFragment,
            @Param("responsibleFragment") String responsibleFragment
    );

    Page<Reservation> findByResponsible(User user, Pageable pageable);

    Page<Reservation> findByUser(User user, Pageable pageable);
}
