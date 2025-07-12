package com.suaistuds.monitoringeqiupment.service;

import com.suaistuds.monitoringeqiupment.payload.Equipment.UpdateEquipmentRequest;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import com.suaistuds.monitoringeqiupment.payload.Equipment.CreateEquipmentRequest;
import com.suaistuds.monitoringeqiupment.payload.Equipment.EquipmentResponse;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;

public interface EquipmentService {
    PagedResponse<EquipmentResponse> getAll(int page, int size);
    EquipmentResponse getById(Long id);
    EquipmentResponse create(CreateEquipmentRequest createRequest, UserPrincipal currentUser);
    EquipmentResponse update(UpdateEquipmentRequest updateRequest, UserPrincipal currentUser);
    void delete(Long id, UserPrincipal currentUser);
    PagedResponse<EquipmentResponse> getByUser(String username, int page, int size);
}
