package com.suaistuds.monitoringeqiupment.controller;

import com.suaistuds.monitoringeqiupment.payload.History.HistoryResponse;
import com.suaistuds.monitoringeqiupment.payload.History.UpdateHistoryRequest;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.security.CurrentUser;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import com.suaistuds.monitoringeqiupment.service.HistoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/histories")
public class HistoryController {

    @Autowired private HistoryService historyService;

    @GetMapping
    public PagedResponse<HistoryResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public HistoryResponse getById(@PathVariable Long id) {
        return historyService.getById(id);
    }

    @PutMapping("/{id}")
    public HistoryResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateHistoryRequest req,
            @CurrentUser UserPrincipal currentUser) {
        req.setId(id);
        return historyService.update(req, currentUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {
        historyService.delete(id, currentUser);
    }

    @GetMapping("/user/{username}")
    public PagedResponse<HistoryResponse> byUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByUser(username, page, size);
    }
}
