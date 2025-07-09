package com.suaistuds.monitoringeqiupment.model.directory;

import com.suaistuds.monitoringeqiupment.model.enums.StatusHistoryName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "status_history")
public class StatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private StatusHistoryName name;

}
