package com.suaistuds.monitoringeqiupment.payload.History;

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
public class CreateHistoryRequest {

    @NotNull private Long equipmentId;
    @NotNull private Long userId;
    @NotNull private Long responsibleId;
    @NotNull private Long statusHistoryId;
    @NotNull private LocalDateTime date;
}
