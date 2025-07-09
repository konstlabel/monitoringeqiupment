package com.suaistuds.monitoringeqiupment.payload.reservation;

import com.suaistuds.monitoringeqiupment.model.directory.StatusReservation;
import com.suaistuds.monitoringeqiupment.model.entity.Equipment;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateReservationRequest {

    @NotNull
    private Long id;

    @NotNull
    private Long equipmentId;

    @NotNull
    private Long userId;

    @NotNull
    private Long responsibleId;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private Long statusReservationId;
}
