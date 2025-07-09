package com.suaistuds.monitoringeqiupment.service.impl;

import com.suaistuds.monitoringeqiupment.exception.ResourceNotFoundException;
import com.suaistuds.monitoringeqiupment.exception.UnauthorizedException;
import com.suaistuds.monitoringeqiupment.model.entity.Reservation;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.payload.reservation.CreateReservationRequest;
import com.suaistuds.monitoringeqiupment.payload.reservation.ReservationResponse;
import com.suaistuds.monitoringeqiupment.payload.reservation.UpdateReservationRequest;
import com.suaistuds.monitoringeqiupment.repository.EquipmentRepository;
import com.suaistuds.monitoringeqiupment.repository.ReservationRepository;
import com.suaistuds.monitoringeqiupment.repository.StatusReservationRepository;
import com.suaistuds.monitoringeqiupment.repository.UserRepository;
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
    public ReservationResponse create(CreateReservationRequest req, UserPrincipal currentUser) {
        Reservation r = new Reservation();
        r.setEquipment(equipmentRepository.findById(req.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", req.getEquipmentId())));
        r.setUser(userRepository.getUserByName(
                userRepository.findById(req.getUserId()).orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", req.getUserId())).getUsername()));
        r.setResponsible(userRepository.getUserByName(
                userRepository.findById(req.getResponsibleId()).orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", req.getResponsibleId())).getUsername()));
        r.setStartDate(req.getStartDate());
        r.setEndDate(req.getEndDate());
        r.setStatusReservation(statusReservationRepository.findById(req.getStatusReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("StatusReservation", "id", req.getStatusReservationId())));
        r.setCreatedBy(currentUser.getId());

        Reservation saved = reservationRepository.save(r);
        return toDto(saved);
    }

    @Override
    @Transactional
    public ReservationResponse update(UpdateReservationRequest req, UserPrincipal currentUser) {
        Reservation r = reservationRepository.findById(req.getId())
                .orElseThrow(() -> new ResourceNotFoundException(RES, "id", req.getId()));

        boolean owner = r.getCreatedBy().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));
        if (!owner && !admin) {
            throw new UnauthorizedException(NO_PERM);
        }

        if (req.getEquipmentId() != null) {
            r.setEquipment(equipmentRepository.findById(req.getEquipmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", req.getEquipmentId())));
        }
        if (req.getStartDate() != null) r.setStartDate(req.getStartDate());
        if (req.getEndDate() != null)   r.setEndDate(req.getEndDate());
        if (req.getStatusReservationId() != null) {
            r.setStatusReservation(statusReservationRepository.findById(req.getStatusReservationId())
                    .orElseThrow(() -> new ResourceNotFoundException("StatusReservation", "id", req.getStatusReservationId())));
        }

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

    private ReservationResponse toDto(Reservation r) {
        ReservationResponse dto = modelMapper.map(r, ReservationResponse.class);
        dto.setEquipmentId(r.getEquipment().getId());
        dto.setUserId(r.getUser().getId());
        dto.setResponsibleId(r.getResponsible().getId());
        dto.setStatusReservationId(r.getStatusReservation().getId());
        return dto;
    }
}
