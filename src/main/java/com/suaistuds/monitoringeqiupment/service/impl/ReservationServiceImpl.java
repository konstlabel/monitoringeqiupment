package com.suaistuds.monitoringeqiupment.service.impl;

import com.suaistuds.monitoringeqiupment.exception.ResourceAlreadyExistsException;
import com.suaistuds.monitoringeqiupment.exception.ResourceNotFoundException;
import com.suaistuds.monitoringeqiupment.exception.UnauthorizedException;
import com.suaistuds.monitoringeqiupment.model.directory.StatusEquipment;
import com.suaistuds.monitoringeqiupment.model.directory.StatusHistory;
import com.suaistuds.monitoringeqiupment.model.directory.StatusReservation;
import com.suaistuds.monitoringeqiupment.model.entity.Equipment;
import com.suaistuds.monitoringeqiupment.model.entity.History;
import com.suaistuds.monitoringeqiupment.model.entity.Reservation;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
import com.suaistuds.monitoringeqiupment.model.enums.StatusEquipmentName;
import com.suaistuds.monitoringeqiupment.model.enums.StatusHistoryName;
import com.suaistuds.monitoringeqiupment.model.enums.StatusReservationName;
import com.suaistuds.monitoringeqiupment.payload.History.CreateHistoryRequest;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.payload.reservation.CreateReservationRequest;
import com.suaistuds.monitoringeqiupment.payload.reservation.ReservationResponse;
import com.suaistuds.monitoringeqiupment.payload.reservation.UpdateReservationRequest;
import com.suaistuds.monitoringeqiupment.repository.*;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import com.suaistuds.monitoringeqiupment.service.HistoryService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {
    private static final String NO_PERM     = "You don't have permission to make this operation";

    @Autowired private HistoryService historyService;

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private EquipmentRepository equipmentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StatusEquipmentRepository statusEquipmentRepository;
    @Autowired private StatusReservationRepository statusReservationRepository;
    @Autowired private StatusHistoryRepository statusHistoryRepository;
    @Autowired private HistoryRepository historyRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public PagedResponse<ReservationResponse> getAll(int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findAll(pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public ReservationResponse getById(Long id) {

        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        return toDto(r);
    }

    @Override
    public PagedResponse<ReservationResponse> getByEquipment(Long equipmentId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", equipmentId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByEquipment(equipment, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<ReservationResponse> getByUser(String username, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByUser(user, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<ReservationResponse> getByUserId(Long userId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByUser(user, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<ReservationResponse> getByResponsible(String username, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        User responsible = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Responsible", "username", username));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByResponsible(responsible, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<ReservationResponse> getByResponsibleId(Long responsibleId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        User responsible = userRepository.findById(responsibleId)
                .orElseThrow(() -> new ResourceNotFoundException("Responsible", "id", responsibleId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByResponsible(responsible, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<ReservationResponse> getByStatus(Long statusId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        StatusReservation status = statusReservationRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Status reservation", "id", statusId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByStatusReservation(status, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<ReservationResponse> getByStartDate(LocalDateTime startDate, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByStartDate(startDate, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<ReservationResponse> getByEndDate(LocalDateTime endDate, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByEndDate(endDate, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<ReservationResponse> getByStartDateBetween(LocalDateTime from, LocalDateTime to, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByStartDateBetween(from, to, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<ReservationResponse> getByEndDateBetween(LocalDateTime from, LocalDateTime to, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByEndDateBetween(from, to, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<ReservationResponse> getCurrentReservations(LocalDateTime date, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<ReservationResponse> getEquipmentAvailability(Long equipmentId, LocalDateTime start, LocalDateTime end, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", equipmentId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByEquipmentAndStartDateLessThanEqualAndEndDateGreaterThanEqual(equipment, start, end, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public ReservationResponse create(CreateReservationRequest createRequest, UserPrincipal currentUser) {

        Reservation reservation = new Reservation();

        Equipment equipment = equipmentRepository.findById(createRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "equipmentId", createRequest.getEquipmentId()));

        if (this.checkEquipmentStatus(equipment, Optional.empty())) {
            throw new ResourceAlreadyExistsException("Reservation", "equipmentId", createRequest.getEquipmentId());
        }

        reservation.setEquipment(equipment);

        reservation.setUser(userRepository.getUserByName(userRepository.findById(createRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", createRequest.getUserId())).getUsername()));

        reservation.setResponsible(userRepository.getUserByName(userRepository.findById(createRequest.getResponsibleId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", createRequest.getResponsibleId())).getUsername()));

        reservation.setStartDate(createRequest.getStartDate());

        reservation.setEndDate(createRequest.getEndDate());

        reservation.setStatusReservation(statusReservationRepository.findById(createRequest.getStatusReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("StatusReservation", "id", createRequest.getStatusReservationId())));

        reservation.setCreatedBy(currentUser.getId());

        Reservation saved = reservationRepository.save(reservation);

        equipment.setStatus(statusEquipmentRepository.findById(2L)
                .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "id", 2L)));

        return toDto(saved);
    }

    @Override
    @Transactional
    public ReservationResponse update(UpdateReservationRequest updateRequest, UserPrincipal currentUser) {

        if (isNotStudioOrAdmin(currentUser)) {
            throw new UnauthorizedException(NO_PERM);
        }

        Reservation reservation = reservationRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", updateRequest.getId()));

        Equipment equipment = equipmentRepository.findById(updateRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "equipmentId", updateRequest.getEquipmentId()));

        if (this.checkEquipmentStatus(equipment, Optional.of(reservation.getEquipment()))) {
            throw new ResourceAlreadyExistsException("Reservation", "equipmentId", updateRequest.getEquipmentId());
        }

        reservation.getEquipment().setStatus(statusEquipmentRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "id", 1L)));

        StatusReservation newStatus = statusReservationRepository.findById(updateRequest.getStatusReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("StatusReservation", "id", updateRequest.getStatusReservationId()));

        List<StatusHistoryName> historyStatuses = Arrays.asList(
                StatusHistoryName.cancelled,
                StatusHistoryName.rejected,
                StatusHistoryName.returned,
                StatusHistoryName.not_returned
        );

        if (historyStatuses.stream()
                .map(StatusHistoryName::name)
                .collect(Collectors.toSet())
                .contains(newStatus.getName())) {

            StatusHistory statusHistory = statusHistoryRepository
                    .findByName(StatusHistoryName.valueOf(newStatus.getName().name()))
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "StatusHistory", "name", newStatus.getName()));

            CreateHistoryRequest historyReq = CreateHistoryRequest.builder()
                    .equipmentId(reservation.getEquipment().getId())
                    .userId(reservation.getUser().getId())
                    .responsibleId(currentUser.getId())
                    .statusHistoryId(statusHistory.getId())
                    .date(reservation.getEndDate())
                    .build();

            historyService.create(historyReq, currentUser);

            reservationRepository.delete(reservation);

            return null;
        }

        reservation.setEquipment(equipmentRepository.findById(updateRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", updateRequest.getEquipmentId())));
        reservation.setStartDate(updateRequest.getStartDate());
        reservation.setEndDate(updateRequest.getEndDate());
        reservation.setStatusReservation(newStatus);

        Reservation updated = reservationRepository.save(reservation);
        return toDto(updated);
    }

    @Override
    public void delete(Long id, UserPrincipal currentUser) {

        if (isNotStudioOrAdmin(currentUser)) {
            throw new UnauthorizedException(NO_PERM);
        }

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));

        reservation.getEquipment().setStatus(statusEquipmentRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "id", 1L)));

        reservationRepository.delete(reservation);
    }

    private PagedResponse<ReservationResponse> toPagedResponse(Page<Reservation> page) {

        List<ReservationResponse> dtos = page.getContent().stream()
                .map(this::toDto)
                .toList();

        return new PagedResponse<>(dtos, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    private boolean isNotStudioOrAdmin(UserPrincipal currentUser) {

        boolean studio = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.studio.name()));
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));

        return !studio && !admin;
    }

    private boolean checkEquipmentStatus(Equipment e2, Optional<Equipment> e1) {

        if (e1.isPresent() && e1.get().equals(e2)) {
                return true;
        }

        StatusEquipment status = e2.getStatus();
        return status.getName() == StatusEquipmentName.available;
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
