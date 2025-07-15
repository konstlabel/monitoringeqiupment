package com.suaistuds.monitoringeqiupment.repository;

import com.suaistuds.monitoringeqiupment.model.directory.StatusEquipment;
import com.suaistuds.monitoringeqiupment.model.directory.Type;
import com.suaistuds.monitoringeqiupment.model.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    Optional<Equipment> findBySerialNumber(String serialNumber);

    Page<Equipment> findByStatus(StatusEquipment status, Pageable pageable);

    Page<Equipment> findByType(Type type, Pageable pageable);

    Page<Equipment> findByCreatedBy(Long id, Pageable pageable);
}


