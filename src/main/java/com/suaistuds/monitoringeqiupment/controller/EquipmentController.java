package com.suaistuds.monitoringeqiupment.controller;

import com.suaistuds.monitoringeqiupment.payload.Equipment.CreateEquipmentRequest;
import com.suaistuds.monitoringeqiupment.payload.Equipment.EquipmentResponse;
import com.suaistuds.monitoringeqiupment.payload.Equipment.UpdateEquipmentRequest;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.security.CurrentUser;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import com.suaistuds.monitoringeqiupment.service.EquipmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {

    @Autowired private EquipmentService equipmentService;

    @GetMapping
    public PagedResponse<EquipmentResponse> list(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size) {
        return equipmentService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public EquipmentResponse getById(@PathVariable Long id) {
        return equipmentService.getById(id);
    }

    @GetMapping("/serial/{serialNumber}")
    public EquipmentResponse getBySerialNumber(@PathVariable String serialNumber) {
        return equipmentService.getBySerialNumber(serialNumber);
    }

    @GetMapping("/status/{statusId}")
    public PagedResponse<EquipmentResponse> byStatus(
            @PathVariable Long statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return equipmentService.getByStatus(statusId, page, size);
    }

    @GetMapping("/type/{typeId}")
    public PagedResponse<EquipmentResponse> byType(
            @PathVariable Long typeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return equipmentService.getByType(typeId, page, size);
    }

    @GetMapping("/user/{username}")
    public PagedResponse<EquipmentResponse> byUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return equipmentService.getByCreatedBy(username, page, size);
    }

    @GetMapping("/user/id/{userId}")
    public PagedResponse<EquipmentResponse> byUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return equipmentService.getByCreatedById(userId, page, size);
    }

    @PostMapping
    public ResponseEntity<EquipmentResponse> create(
            @Valid @RequestBody CreateEquipmentRequest createRequest,
            @CurrentUser UserPrincipal currentUser) {
        EquipmentResponse dto = equipmentService.create(createRequest, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public EquipmentResponse update(
            @Valid @RequestBody UpdateEquipmentRequest updateRequest,
            @CurrentUser UserPrincipal currentUser) {
        return equipmentService.update(updateRequest, currentUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {
        equipmentService.delete(id, currentUser);
    }
}