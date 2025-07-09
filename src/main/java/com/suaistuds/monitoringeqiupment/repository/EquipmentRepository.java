package com.suaistuds.monitoringeqiupment.repository;

import com.suaistuds.monitoringeqiupment.model.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query("""
        SELECT e FROM Equipment e
        WHERE (:statusId IS NULL OR e.status.id = :statusId)
          AND (:typeId IS NULL OR e.type.id = :typeId)
          AND (:serialFragment IS NULL OR LOWER(e.serialNumber) LIKE LOWER(CONCAT('%', :serialFragment, '%')))
          AND (:nameFragment IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :nameFragment, '%')))
    """)
    List<Equipment> searchEquipment(
        @Param("statusId") Long statusId,
        @Param("typeId") Long typeId,
        @Param("serialFragment") String serialFragment,
        @Param("nameFragment") String nameFragment
    );

    Page<Equipment> findByCreatedBy(Long id, Pageable pageable);
}


