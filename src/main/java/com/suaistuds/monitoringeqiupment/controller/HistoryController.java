package com.suaistuds.monitoringeqiupment.controller;

import com.suaistuds.monitoringeqiupment.payload.History.HistoryResponse;
import com.suaistuds.monitoringeqiupment.payload.History.CreateHistoryRequest;
import com.suaistuds.monitoringeqiupment.payload.History.UpdateHistoryRequest;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.security.CurrentUser;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import com.suaistuds.monitoringeqiupment.service.HistoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/histories")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping
    public PagedResponse<HistoryResponse> list(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size) {
        return historyService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public HistoryResponse getById(@PathVariable Long id) {
        return historyService.getById(id);
    }

    @PostMapping
    public ResponseEntity<HistoryResponse> create(
            @Valid @RequestBody CreateHistoryRequest createRequest,
            @CurrentUser UserPrincipal currentUser) {
        HistoryResponse response = historyService.create(createRequest, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public HistoryResponse update(
            @Valid @RequestBody UpdateHistoryRequest updateRequest,
            @CurrentUser UserPrincipal currentUser) {
        return historyService.update(updateRequest, currentUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {
        historyService.delete(id, currentUser);
    }

    // Фильтрация по пользователям
    @GetMapping("/user/{username}")
    public PagedResponse<HistoryResponse> byUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByUser(username, page, size);
    }

    @GetMapping("/user/id/{userId}")
    public PagedResponse<HistoryResponse> byUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByUserId(userId, page, size);
    }

    // Фильтрация по оборудованию
    @GetMapping("/equipment/{equipmentId}")
    public PagedResponse<HistoryResponse> byEquipment(
            @PathVariable Long equipmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByEquipment(equipmentId, page, size);
    }

    // Фильтрация по ответственным
    @GetMapping("/responsible/{username}")
    public PagedResponse<HistoryResponse> byResponsible(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByResponsible(username, page, size);
    }

    @GetMapping("/responsible/id/{responsibleId}")
    public PagedResponse<HistoryResponse> byResponsibleId(
            @PathVariable Long responsibleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByResponsibleId(responsibleId, page, size);
    }

    // Фильтрация по статусу
    @GetMapping("/status/{statusId}")
    public PagedResponse<HistoryResponse> byStatus(
            @PathVariable Long statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByStatus(statusId, page, size);
    }

    // Фильтрация по датам
    @GetMapping("/date")
    public PagedResponse<HistoryResponse> byDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByDate(date, page, size);
    }

    @GetMapping("/date-range")
    public PagedResponse<HistoryResponse> byDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByDateBetween(startDate, endDate, page, size);
    }
}