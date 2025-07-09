package com.suaistuds.monitoringeqiupment.service;

import com.suaistuds.monitoringeqiupment.payload.ApiResponse;
import com.suaistuds.monitoringeqiupment.payload.History.HistoryResponse;
import com.suaistuds.monitoringeqiupment.payload.History.UpdateHistoryRequest;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface HistoryService {

    PagedResponse<HistoryResponse> getAll(int page, int size);

    HistoryResponse getById(Long id);

    HistoryResponse update(UpdateHistoryRequest req, UserPrincipal currentUser);

    void delete(Long id, UserPrincipal currentUser);

    public PagedResponse<HistoryResponse> getByUser(String username, int page, int size);
}
