package com.suaistuds.monitoringeqiupment.service;

import com.suaistuds.monitoringeqiupment.payload.History.CreateHistoryRequest;
import com.suaistuds.monitoringeqiupment.payload.History.HistoryResponse;
import com.suaistuds.monitoringeqiupment.payload.History.UpdateHistoryRequest;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;

public interface HistoryService {

    PagedResponse<HistoryResponse> getAll(int page, int size);

    HistoryResponse getById(Long id);

    HistoryResponse create(CreateHistoryRequest createRequest, UserPrincipal currentUser);

    HistoryResponse update(UpdateHistoryRequest updateRequest, UserPrincipal currentUser);

    void delete(Long id, UserPrincipal currentUser);

    PagedResponse<HistoryResponse> getByUser(String username, int page, int size);
}
