package com.suaistuds.monitoringeqiupment.repository;

import com.suaistuds.monitoringeqiupment.model.directory.StatusEquipment;
import com.suaistuds.monitoringeqiupment.model.enums.StatusEquipmentName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusEquipmentRepository extends JpaRepository<StatusEquipment, Long> {
    Optional<StatusEquipment> findByName(StatusEquipmentName name);
}
