package com.suaistuds.monitoringeqiupment.service;

import com.suaistuds.monitoringeqiupment.model.directory.StatusHistory;
import com.suaistuds.monitoringeqiupment.payload.History.CreateHistoryRequest;
import com.suaistuds.monitoringeqiupment.payload.History.HistoryResponse;
import com.suaistuds.monitoringeqiupment.payload.History.UpdateHistoryRequest;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;

import java.time.LocalDateTime;

public interface HistoryService {

    PagedResponse<HistoryResponse> getAll(int page, int size);

    HistoryResponse getById(Long id);

    PagedResponse<HistoryResponse> getByUser(String username, int page, int size);

    PagedResponse<HistoryResponse> getByUserId(Long userId, int page, int size);

    PagedResponse<HistoryResponse> getByEquipment(Long equipmentId, int page, int size);

    PagedResponse<HistoryResponse> getByResponsible(String username, int page, int size);

    PagedResponse<HistoryResponse> getByResponsibleId(Long responsibleId, int page, int size);

    PagedResponse<HistoryResponse> getByStatus(Long statusId, int page, int size);

    PagedResponse<HistoryResponse> getByDate(LocalDateTime date, int page, int size);

    PagedResponse<HistoryResponse> getByDateBetween(LocalDateTime startDate, LocalDateTime endDate, int page, int size);

    HistoryResponse create(CreateHistoryRequest createRequest, UserPrincipal currentUser);

    HistoryResponse update(UpdateHistoryRequest updateRequest, UserPrincipal currentUser);

    void delete(Long id, UserPrincipal currentUser);
}
