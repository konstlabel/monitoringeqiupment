package com.suaistuds.monitoringeqiupment.payload.user;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserIdentityAvailability {
    private Boolean available;
}