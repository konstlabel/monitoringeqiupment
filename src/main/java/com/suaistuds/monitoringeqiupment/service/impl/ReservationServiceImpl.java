package com.suaistuds.monitoringeqiupment.service.impl;

import com.suaistuds.monitoringeqiupment.exception.ResourceNotFoundException;
import com.suaistuds.monitoringeqiupment.exception.UnauthorizedException;
import com.suaistuds.monitoringeqiupment.model.directory.StatusHistory;
import com.suaistuds.monitoringeqiupment.model.directory.StatusReservation;
import com.suaistuds.monitoringeqiupment.model.entity.History;
import com.suaistuds.monitoringeqiupment.model.entity.Reservation;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
import com.suaistuds.monitoringeqiupment.model.enums.StatusHistoryName;
import com.suaistuds.monitoringeqiupment.model.enums.StatusReservationName;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.payload.reservation.CreateReservationRequest;
import com.suaistuds.monitoringeqiupment.payload.reservation.ReservationResponse;
import com.suaistuds.monitoringeqiupment.payload.reservation.UpdateReservationRequest;
import com.suaistuds.monitoringeqiupment.repository.*;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import com.suaistuds.monitoringeqiupment.service.ReservationService;
import com.suaistuds.monitoringeqiupment.util.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {
    private static final String CREATED_AT = "createdAt";
    private static final String RES         = "Reservation";
    private static final String NO_PERM     = "You don't have permission to make this operation";

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private EquipmentRepository equipmentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StatusReservationRepository statusReservationRepository;
    @Autowired private StatusHistoryRepository statusHistoryRepository;
    @Autowired private HistoryRepository historyRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public PagedResponse<ReservationResponse> getAll(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by(CREATED_AT).descending());
        Page<Reservation> p = reservationRepository.findAll(pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();
        return new PagedResponse<>(dtos, p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages(), p.isLast());
    }

    @Override
    public ReservationResponse getById(Long id) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RES, "id", id));
        return toDto(r);
    }

    @Override
    @Transactional
    public ReservationResponse create(CreateReservationRequest createRequest, UserPrincipal currentUser) {
        Reservation r = new Reservation();
        r.setEquipment(equipmentRepository.findById(createRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", createRequest.getEquipmentId())));
        r.setUser(userRepository.getUserByName(
                userRepository.findById(createRequest.getUserId()).orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", createRequest.getUserId())).getUsername()));
        r.setResponsible(userRepository.getUserByName(
                userRepository.findById(createRequest.getResponsibleId()).orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", createRequest.getResponsibleId())).getUsername()));
        r.setStartDate(createRequest.getStartDate());
        r.setEndDate(createRequest.getEndDate());
        r.setStatusReservation(statusReservationRepository.findById(createRequest.getStatusReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("StatusReservation", "id", createRequest.getStatusReservationId())));
        r.setCreatedBy(currentUser.getId());

        Reservation saved = reservationRepository.save(r);
        return toDto(saved);
    }

    @Override
    @Transactional
    public ReservationResponse update(UpdateReservationRequest updateRequest, UserPrincipal currentUser) {
        Reservation r = reservationRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", updateRequest.getId()));

        boolean owner = r.getCreatedBy().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));
        if (!owner && !admin) {
            throw new UnauthorizedException("You don't have permission to make this operation");
        }

        if (updateRequest.getEquipmentId() != null) {
            r.setEquipment(equipmentRepository.findById(updateRequest.getEquipmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", updateRequest.getEquipmentId())));
        }
        if (updateRequest.getStartDate() != null) r.setStartDate(updateRequest.getStartDate());
        if (updateRequest.getEndDate()   != null) r.setEndDate(updateRequest.getEndDate());

        if (updateRequest.getStatusReservationId() != null) {
            StatusReservation newStatus =
                    statusReservationRepository.findById(updateRequest.getStatusReservationId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "StatusReservation", "id", updateRequest.getStatusReservationId()));
            r.setStatusReservation(newStatus);

            EnumSet<StatusReservationName> toArchive = EnumSet.of(
                    StatusReservationName.cancelled,
                    StatusReservationName.rejected,
                    StatusReservationName.returned,
                    StatusReservationName.not_returned
            );
            StatusReservationName srName = newStatus.getName();
            if (toArchive.contains(srName)) {
                StatusHistory sh = statusHistoryRepository.findByName(
                                StatusHistoryName.valueOf(srName.name()))
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "StatusHistory", "name", srName.name()));

                History history = History.builder()
                        .equipment(r.getEquipment())
                        .user(r.getUser())
                        .responsible(r.getResponsible())
                        .statusHistory(sh)
                        .date(LocalDateTime.now())
                        .build();
                historyRepository.save(history);

                reservationRepository.delete(r);

                return null;
            }
        }

        // обычное сохранение, если статус не «архивный»
        Reservation updated = reservationRepository.save(r);
        return toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id, UserPrincipal currentUser) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RES, "id", id));

        boolean owner = r.getCreatedBy().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));
        if (!owner && !admin) {
            throw new UnauthorizedException(NO_PERM);
        }

        reservationRepository.delete(r);
    }

    @Override
    public PagedResponse<ReservationResponse> getByUser(String username, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        User u = userRepository.getUserByName(username);
        Pageable pg = PageRequest.of(page, size, Sort.by(CREATED_AT).descending());
        Page<Reservation> p = reservationRepository.findByUser(u, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();
        return new PagedResponse<>(dtos, p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages(), p.isLast());
    }

    @Override
    public PagedResponse<ReservationResponse> getByResponsible(String username, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        User u = userRepository.getUserByName(username);
        Pageable pg = PageRequest.of(page, size, Sort.by(CREATED_AT).descending());
        Page<Reservation> p = reservationRepository.findByResponsible(u, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();
        return new PagedResponse<>(dtos, p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages(), p.isLast());
    }

    private ReservationResponse toDto(Reservation reservation) {
        ReservationResponse dto = modelMapper.map(reservation, ReservationResponse.class);
        dto.setEquipmentId(reservation.getEquipment().getId());
        dto.setUserId(reservation.getUser().getId());
        dto.setResponsibleId(reservation.getResponsible().getId());
        dto.setStatusReservationId(reservation.getStatusReservation().getId());
        return dto;
    }
}
