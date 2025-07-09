package com.suaistuds.monitoringeqiupment.payload.Equipment;

import com.suaistuds.monitoringeqiupment.model.directory.StatusEquipment;
import com.suaistuds.monitoringeqiupment.model.directory.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateEquipmentRequest {

    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank
    private String serialNumber;

    @NotNull
    private Long statusId;

    @NotNull
    private Long typeId;
}
