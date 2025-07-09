package com.suaistuds.monitoringeqiupment.payload.History;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.suaistuds.monitoringeqiupment.model.directory.StatusHistory;
import com.suaistuds.monitoringeqiupment.model.entity.Equipment;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.payload.audit.UserDateAuditPayload;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistorySummary extends UserDateAuditPayload {
    private Long equipmentId;
    private Long userId;
    private Long responsibleId;
    private Long statusHistoryId;
    private LocalDateTime date;
}
