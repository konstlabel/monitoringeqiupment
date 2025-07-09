package com.suaistuds.monitoringeqiupment.model.directory;

import com.suaistuds.monitoringeqiupment.model.enums.StatusReservationName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "status_reservation")
public class StatusReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = "name")
    private StatusReservationName name;
}
