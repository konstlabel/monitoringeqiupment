package com.suaistuds.monitoringeqiupment.service.impl;

import com.suaistuds.monitoringeqiupment.exception.ResourceNotFoundException;
import com.suaistuds.monitoringeqiupment.exception.UnauthorizedException;
import com.suaistuds.monitoringeqiupment.model.directory.StatusHistory;
import com.suaistuds.monitoringeqiupment.model.entity.Equipment;
import com.suaistuds.monitoringeqiupment.model.entity.History;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
import com.suaistuds.monitoringeqiupment.payload.History.CreateHistoryRequest;
import com.suaistuds.monitoringeqiupment.payload.History.HistoryResponse;
import com.suaistuds.monitoringeqiupment.payload.History.UpdateHistoryRequest;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.repository.StatusHistoryRepository;
import com.suaistuds.monitoringeqiupment.repository.EquipmentRepository;
import com.suaistuds.monitoringeqiupment.repository.HistoryRepository;
import com.suaistuds.monitoringeqiupment.repository.UserRepository;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import com.suaistuds.monitoringeqiupment.service.HistoryService;
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
import java.util.List;

@Service
@Transactional(readOnly = true)
public class HistoryServiceImpl implements HistoryService {
    private static final String NO_PERM    = "You don't have permission to make this operation";

    @Autowired private HistoryRepository historyRepository;
    @Autowired private EquipmentRepository equipmentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StatusHistoryRepository statusHistoryRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public PagedResponse<HistoryResponse> getAll(int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<History> p = historyRepository.findAll(pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public HistoryResponse getById(Long id) {

        History history = historyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("History", "id", id));
        return toDto(history);
    }

    @Override
    public PagedResponse<HistoryResponse> getByUser(String username, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<History> p = historyRepository.findByUser(user, pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<HistoryResponse> getByUserId(Long userId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<History> p = historyRepository.findByUser(user, pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<HistoryResponse> getByEquipment(Long equipmentId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", equipmentId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<History> p = historyRepository.findByEquipment(equipment, pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<HistoryResponse> getByResponsible(String username, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        User responsible = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Responsible", "username", username));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<History> p = historyRepository.findByUser(responsible, pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<HistoryResponse> getByResponsibleId(Long responsibleId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        User responsible = userRepository.findById(responsibleId)
                .orElseThrow(() -> new ResourceNotFoundException("Responsible", "id", responsibleId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<History> p = historyRepository.findByUser(responsible, pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<HistoryResponse> getByStatus(Long statusId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        StatusHistory status = statusHistoryRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Status history", "id", statusId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<History> p = historyRepository.findByStatusHistory(status, pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<HistoryResponse> getByDate(LocalDateTime date, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<History> p = historyRepository.findByDate(date, pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<HistoryResponse> getByDateBetween(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<History> p = historyRepository.findByDateBetween(startDate, endDate, pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public HistoryResponse create(CreateHistoryRequest createRequest, UserPrincipal currentUser) {

        if (isNotStudioOrAdmin(currentUser)) {
            throw new UnauthorizedException(NO_PERM);
        }

        Equipment equipment = equipmentRepository.findById(createRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", createRequest.getEquipmentId()));

        User user = userRepository.findById(createRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", createRequest.getUserId()));

        User responsible = userRepository.findById(createRequest.getResponsibleId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", createRequest.getResponsibleId()));

        StatusHistory statusHistory = statusHistoryRepository.findById(createRequest.getStatusHistoryId())
                .orElseThrow(() -> new ResourceNotFoundException("StatusHistory", "id", createRequest.getStatusHistoryId()));

        History history = History.builder()
                .equipment(equipment)
                .user(user)
                .responsible(responsible)
                .statusHistory(statusHistory)
                .date(createRequest.getDate())
                .build();

        History saved = historyRepository.save(history);
        return toDto(saved);
    }

    @Override
    public HistoryResponse update(UpdateHistoryRequest updateRequest, UserPrincipal currentUser) {

        if (isNotStudioOrAdmin(currentUser)) {
            throw new UnauthorizedException(NO_PERM);
        }

        History history = historyRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("History", "id", updateRequest.getId()));

            Equipment equipment = equipmentRepository.findById(updateRequest.getEquipmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", updateRequest.getEquipmentId()));
            history.setEquipment(equipment);

            User user = userRepository.findById(updateRequest.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", updateRequest.getUserId()));
            history.setUser(user);

            User responsible = userRepository.findById(updateRequest.getResponsibleId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", updateRequest.getResponsibleId()));
            history.setResponsible(responsible);

            StatusHistory status = statusHistoryRepository.findById(updateRequest.getStatusHistoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("StatusHistory", "id", updateRequest.getStatusHistoryId()));
            history.setStatusHistory(status);

            history.setDate(updateRequest.getDate());

        History updated = historyRepository.save(history);
        return toDto(updated);
    }

    @Override
    public void delete(Long id, UserPrincipal currentUser) {

        if (isNotStudioOrAdmin(currentUser)) {
            throw new UnauthorizedException(NO_PERM);
        }

        History h = historyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("History", "id", id));

        historyRepository.delete(h);
    }

    private PagedResponse<HistoryResponse> toPagedResponse(Page<History> page) {

        List<HistoryResponse> dtos = page.getContent().stream()
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
    
    private HistoryResponse toDto(History history) {

        HistoryResponse dto = modelMapper.map(history, HistoryResponse.class);

        dto.setEquipmentId(history.getEquipment().getId());
        dto.setDate(history.getDate());
        dto.setStatusHistoryId(history.getStatusHistory().getId());
        dto.setUserId(history.getUser().getId());
        dto.setResponsibleId(history.getResponsible().getId());

        return dto;
    }
}