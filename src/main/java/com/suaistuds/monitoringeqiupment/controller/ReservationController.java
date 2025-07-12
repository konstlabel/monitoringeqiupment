package com.suaistuds.monitoringeqiupment.controller;

import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.payload.reservation.CreateReservationRequest;
import com.suaistuds.monitoringeqiupment.payload.reservation.ReservationResponse;
import com.suaistuds.monitoringeqiupment.payload.reservation.UpdateReservationRequest;
import com.suaistuds.monitoringeqiupment.security.CurrentUser;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import com.suaistuds.monitoringeqiupment.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired private ReservationService reservationService;

    @GetMapping
    public PagedResponse<ReservationResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

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

    @GetMapping("/user/{username}")
    public PagedResponse<ReservationResponse> byUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        return reservationService.getByUser(username, page, size);
    }

    @GetMapping("/responsible/{username}")
    public PagedResponse<ReservationResponse> byResponsible(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        return reservationService.getByResponsible(username, page, size);
    }
}
