package com.suaistuds.monitoringeqiupment.repository;

import com.suaistuds.monitoringeqiupment.model.entity.History;
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
public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query("""
        SELECT h FROM History h
        WHERE (:statusHistoryId IS NULL OR h.statusHistory.id = :statusHistoryId)
          AND (:startDate IS NULL OR h.date >= :startDate)
          AND (:endDate IS NULL OR h.date <= :endDate)
          AND (:responsibleFragment IS NULL OR LOWER(h.responsible.username) LIKE LOWER(CONCAT('%', :responsibleFragment, '%')))
          AND (:userFragment IS NULL OR LOWER(h.user.username) LIKE LOWER(CONCAT('%', :userFragment, '%')))
          AND (:serialFragment IS NULL OR LOWER(h.equipment.serialNumber) LIKE LOWER(CONCAT('%', :serialFragment, '%')))
    """)
    List<History> searchHistory(
        @Param("statusHistoryId") Long statusHistoryId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("responsibleFragment") String responsibleFragment,
        @Param("userFragment") String userFragment,
        @Param("serialFragment") String serialFragment
    );

    Page<History> findByUser(User user, Pageable pageable);
}
