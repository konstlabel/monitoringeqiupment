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

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HistoryServiceImpl implements HistoryService {
    private static final String CREATED_AT = "createdAt";
    private static final String HISTORY    = "History";
    private static final String NO_PERM    = "You don't have permission to make this operation";

    @Autowired private HistoryRepository historyRepository;
    @Autowired private EquipmentRepository equipmentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StatusHistoryRepository statusHistoryRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public PagedResponse<HistoryResponse> getAll(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by(CREATED_AT).descending());
        Page<History> p = historyRepository.findAll(pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();
        return new PagedResponse<>(dtos, p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages(), p.isLast());
    }

    @Override
    public HistoryResponse getById(Long id) {
        History h = historyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HISTORY, "id", id));
        return toDto(h);
    }

    @Override
    @Transactional
    public HistoryResponse create(CreateHistoryRequest createRequest, UserPrincipal currentUser) {
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
    @Transactional
    public HistoryResponse update(UpdateHistoryRequest updateRequest, UserPrincipal currentUser) {
        History h = historyRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException(HISTORY, "id", updateRequest.getId()));

        boolean owner = h.getCreatedBy().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));
        if (!owner && !admin) {
            throw new UnauthorizedException(NO_PERM);
        }

        if (updateRequest.getEquipmentId() != null) {
            Equipment eq = equipmentRepository.findById(updateRequest.getEquipmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", updateRequest.getEquipmentId()));
            h.setEquipment(eq);
        }
        if (updateRequest.getUserId() != null) {
            User u = userRepository.findById(updateRequest.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", updateRequest.getUserId()));
            h.setUser(u);
        }
        if (updateRequest.getResponsibleId() != null) {
            User r = userRepository.findById(updateRequest.getResponsibleId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", updateRequest.getResponsibleId()));
            h.setResponsible(r);
        }
        if (updateRequest.getStatusHistoryId() != null) {
            StatusHistory sh = statusHistoryRepository.findById(updateRequest.getStatusHistoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("StatusHistory", "id", updateRequest.getStatusHistoryId()));
            h.setStatusHistory(sh);
        }
        if (updateRequest.getDate() != null) {
            h.setDate(updateRequest.getDate());
        }

        History updated = historyRepository.save(h);
        return toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id, UserPrincipal currentUser) {
        History h = historyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HISTORY, "id", id));

        boolean owner = h.getCreatedBy().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));
        if (!owner && !admin) {
            throw new UnauthorizedException(NO_PERM);
        }

        historyRepository.delete(h);
    }

    @Override
    public PagedResponse<HistoryResponse> getByUser(String username, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        User u = userRepository.getUserByName(username);
        Pageable pg = PageRequest.of(page, size, Sort.by(CREATED_AT).descending());
        Page<History> p = historyRepository.findByUser(u, pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();
        return new PagedResponse<>(dtos, p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages(), p.isLast());
    }

    private HistoryResponse toDto(History h) {
        HistoryResponse dto = modelMapper.map(h, HistoryResponse.class);
        dto.setEquipmentId(h.getEquipment().getId());
        dto.setUserId(h.getUser().getId());
        dto.setResponsibleId(h.getResponsible().getId());
        dto.setStatusHistoryId(h.getStatusHistory().getId());
        return dto;
    }
}