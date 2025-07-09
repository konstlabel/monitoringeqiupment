package com.suaistuds.monitoringeqiupment.payload.Equipment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.suaistuds.monitoringeqiupment.payload.audit.UserDateAuditPayload;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentResponse extends UserDateAuditPayload {
    private Long id;
    private String name;
    private String serialNumber;
    private Long statusId;
    private Long typeId;
}
