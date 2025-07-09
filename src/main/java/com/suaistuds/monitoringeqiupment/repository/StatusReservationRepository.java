package com.suaistuds.monitoringeqiupment.repository;

import com.suaistuds.monitoringeqiupment.model.directory.StatusReservation;
import com.suaistuds.monitoringeqiupment.model.enums.StatusReservationName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusReservationRepository extends JpaRepository<StatusReservation, Long> {
    Optional<StatusReservation> findByName(StatusReservationName name);
}
