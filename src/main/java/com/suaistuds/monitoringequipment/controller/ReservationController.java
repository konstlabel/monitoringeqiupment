package com.suaistuds.monitoringequipment.controller;

import com.suaistuds.monitoringequipment.payload.PagedResponse;
import com.suaistuds.monitoringequipment.payload.reservation.CreateReservationRequest;
import com.suaistuds.monitoringequipment.payload.reservation.ReservationResponse;
import com.suaistuds.monitoringequipment.payload.reservation.UpdateReservationRequest;
import com.suaistuds.monitoringequipment.security.CurrentUser;
import com.suaistuds.monitoringequipment.security.UserPrincipal;
import com.suaistuds.monitoringequipment.service.ReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // Основные CRUD операции
    @GetMapping
    public PagedResponse<ReservationResponse> list(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size) {
        return reservationService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public ReservationResponse getById(@PathVariable Long id) {
        return reservationService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @Valid @RequestBody CreateReservationRequest createRequest,
            @CurrentUser UserPrincipal currentUser) {
        ReservationResponse dto = reservationService.create(createRequest, currentUser);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ReservationResponse update(
            @Valid @RequestBody UpdateReservationRequest updateRequest,
            @CurrentUser UserPrincipal currentUser) {
        return reservationService.update(updateRequest, currentUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {
        reservationService.delete(id, currentUser);
    }

    // Фильтрация по пользователям
    @GetMapping("/user/{username}")
    public PagedResponse<ReservationResponse> byUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByUser(username, page, size);
    }

    @GetMapping("/user/id/{userId}")
    public PagedResponse<ReservationResponse> byUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByUserId(userId, page, size);
    }

    // Фильтрация по ответственным
    @GetMapping("/responsible/{username}")
    public PagedResponse<ReservationResponse> byResponsible(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByResponsible(username, page, size);
    }

    @GetMapping("/responsible/id/{responsibleId}")
    public PagedResponse<ReservationResponse> byResponsibleId(
            @PathVariable Long responsibleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByResponsibleId(responsibleId, page, size);
    }

    // Фильтрация по оборудованию
    @GetMapping("/equipment/{equipmentId}")
    public PagedResponse<ReservationResponse> byEquipment(
            @PathVariable Long equipmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByEquipment(equipmentId, page, size);
    }

    // Фильтрация по статусу
    @GetMapping("/status/{statusId}")
    public PagedResponse<ReservationResponse> byStatus(
            @PathVariable Long statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByStatus(statusId, page, size);
    }

    // Фильтрация по датам
    @GetMapping("/start-date")
    public PagedResponse<ReservationResponse> byStartDate(
            @RequestParam LocalDateTime startDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByStartDate(startDate, page, size);
    }

    @GetMapping("/end-date")
    public PagedResponse<ReservationResponse> byEndDate(
            @RequestParam LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByEndDate(endDate, page, size);
    }

    @GetMapping("/date-range")
    public PagedResponse<ReservationResponse> byDateRange(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByStartDateBetween(from, to, page, size);
    }

    // Специальные запросы
    @GetMapping("/current")
    public PagedResponse<ReservationResponse> currentReservations(
            @RequestParam LocalDateTime date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getCurrentReservations(date, page, size);
    }

    @GetMapping("/availability")
    public PagedResponse<ReservationResponse> checkAvailability(
            @RequestParam Long equipmentId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getEquipmentAvailability(equipmentId, start, end, page, size);
    }
}