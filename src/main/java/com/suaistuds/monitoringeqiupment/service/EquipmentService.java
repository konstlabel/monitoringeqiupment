package com.suaistuds.monitoringeqiupment.service;

import ch.qos.logback.core.status.Status;
import com.suaistuds.monitoringeqiupment.model.directory.Type;
import com.suaistuds.monitoringeqiupment.payload.Equipment.UpdateEquipmentRequest;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import com.suaistuds.monitoringeqiupment.payload.Equipment.CreateEquipmentRequest;
import com.suaistuds.monitoringeqiupment.payload.Equipment.EquipmentResponse;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;

public interface EquipmentService {

    PagedResponse<EquipmentResponse> getAll(int page, int size);

    EquipmentResponse getById(Long id);

    EquipmentResponse getBySerialNumber(String serialNumber);

    PagedResponse<EquipmentResponse> getByStatus(Long statusId, int page, int size);

    PagedResponse<EquipmentResponse> getByType(Long typeId, int page, int size);

    PagedResponse<EquipmentResponse> getByCreatedBy(String username, int page, int size);

    PagedResponse<EquipmentResponse> getByCreatedById(Long userId, int page, int size);

    EquipmentResponse create(CreateEquipmentRequest createRequest, UserPrincipal currentUser);

    EquipmentResponse update(UpdateEquipmentRequest updateRequest, UserPrincipal currentUser);

    void delete(Long id, UserPrincipal currentUser);
}
