package com.suaistuds.monitoringeqiupment.payload.History;

import com.suaistuds.monitoringeqiupment.model.directory.StatusHistory;
import com.suaistuds.monitoringeqiupment.model.entity.Equipment;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.model.enums.StatusHistoryName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateHistoryRequest {
    @NotNull private Long id;
    @NotNull private Long equipmentId;
    @NotNull private Long userId;
    @NotNull private Long responsibleId;
    @NotNull private Long statusHistoryId;
    @NotNull private LocalDateTime date;
}
