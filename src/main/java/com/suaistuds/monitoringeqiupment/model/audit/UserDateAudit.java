package com.suaistuds.monitoringeqiupment.model.audit;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
@JsonIgnoreProperties(
        value = { "createdBy", "updatedBy" },
        allowGetters = true
)
public abstract class UserDateAudit extends DateAudit {

    @Serial
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    @LastModifiedBy
    private Long updatedBy;
}