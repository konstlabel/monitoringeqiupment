package com.suaistuds.monitoringeqiupment.payload.History;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.suaistuds.monitoringeqiupment.payload.audit.UserDateAuditPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoryResponse extends UserDateAuditPayload {
    private Long id;
    private Long equipmentId;
    private Long userId;
    private Long responsibleId;
    private Long statusHistoryId;
    private LocalDateTime date;
}
