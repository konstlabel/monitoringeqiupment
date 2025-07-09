package com.suaistuds.monitoringeqiupment.model.directory;

import com.suaistuds.monitoringeqiupment.model.enums.StatusEquipmentName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Entity
@Data
@NoArgsConstructor
@Table(name = "status")
public class StatusEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = "name")
    private StatusEquipmentName name;
}
