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

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    Page<History> findByEquipment(Equipment equipment, Pageable pageable);

    Page<History> findByUser(User user, Pageable pageable);

    Page<History> findByResponsible(User responsible, Pageable pageable);

    Page<History> findByStatusHistory(StatusHistory statusHistory, Pageable pageable);

    Page<History> findByDate(LocalDateTime date, Pageable pageable);

    Page<History> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
