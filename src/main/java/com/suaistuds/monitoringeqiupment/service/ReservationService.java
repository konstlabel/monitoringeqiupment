package com.suaistuds.monitoringeqiupment.service;

import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.payload.reservation.CreateReservationRequest;
import com.suaistuds.monitoringeqiupment.payload.reservation.ReservationResponse;
import com.suaistuds.monitoringeqiupment.payload.reservation.UpdateReservationRequest;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;

import java.time.LocalDateTime;

public interface ReservationService {

    PagedResponse<ReservationResponse> getAll(int page, int size);

    ReservationResponse getById(Long id);

    PagedResponse<ReservationResponse> getByEquipment(Long equipmentId, int page, int size);

    PagedResponse<ReservationResponse> getByUser(String username, int page, int size);

    PagedResponse<ReservationResponse> getByUserId(Long userId, int page, int size);

    PagedResponse<ReservationResponse> getByResponsible(String username, int page, int size);

    PagedResponse<ReservationResponse> getByResponsibleId(Long responsibleId, int page, int size);

    PagedResponse<ReservationResponse> getByStatus(Long statusId, int page, int size);

    PagedResponse<ReservationResponse> getByStartDate(LocalDateTime startDate, int page, int size);

    PagedResponse<ReservationResponse> getByEndDate(LocalDateTime endDate, int page, int size);

    PagedResponse<ReservationResponse> getByStartDateBetween(LocalDateTime from, LocalDateTime to, int page, int size);

    PagedResponse<ReservationResponse> getByEndDateBetween(LocalDateTime from, LocalDateTime to, int page, int size);

    PagedResponse<ReservationResponse> getCurrentReservations(LocalDateTime date, int page, int size);

    PagedResponse<ReservationResponse> getEquipmentAvailability(Long equipmentId, LocalDateTime start, LocalDateTime end, int page, int size);

    ReservationResponse create(CreateReservationRequest createRequest, UserPrincipal currentUser);

    ReservationResponse update(UpdateReservationRequest updateRequest, UserPrincipal currentUser);

    void delete(Long id, UserPrincipal currentUser);

}
