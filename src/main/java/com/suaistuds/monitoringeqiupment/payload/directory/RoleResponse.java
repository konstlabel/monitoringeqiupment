package com.suaistuds.monitoringeqiupment.payload.directory;

import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {
    Long id;
    RoleName name;
}
