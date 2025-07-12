package com.suaistuds.monitoringeqiupment.service;

import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.payload.reservation.CreateReservationRequest;
import com.suaistuds.monitoringeqiupment.payload.reservation.ReservationResponse;
import com.suaistuds.monitoringeqiupment.payload.reservation.UpdateReservationRequest;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;

public interface ReservationService {

    PagedResponse<ReservationResponse> getAll(int page, int size);

    ReservationResponse getById(Long id);

    ReservationResponse create(CreateReservationRequest createRequest, UserPrincipal currentUser);

    ReservationResponse update(UpdateReservationRequest updateRequest, UserPrincipal currentUser);

    void delete(Long id, UserPrincipal currentUser);

    PagedResponse<ReservationResponse> getByUser(String username, int page, int size);

    PagedResponse<ReservationResponse> getByResponsible(String username, int page, int size);
}
