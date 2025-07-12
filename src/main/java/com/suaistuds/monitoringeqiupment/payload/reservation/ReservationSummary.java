package com.suaistuds.monitoringeqiupment.payload.reservation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.suaistuds.monitoringeqiupment.payload.audit.UserDateAuditPayload;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationSummary extends UserDateAuditPayload {
    private Long equipmentId;
    private Long userId;
    private Long responsibleId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long statusReservationId;
}
