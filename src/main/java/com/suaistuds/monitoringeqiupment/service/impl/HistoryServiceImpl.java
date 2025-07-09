package com.suaistuds.monitoringeqiupment.service.impl;

import com.suaistuds.monitoringeqiupment.exception.ResourceNotFoundException;
import com.suaistuds.monitoringeqiupment.exception.UnauthorizedException;
import com.suaistuds.monitoringeqiupment.model.directory.StatusHistory;
import com.suaistuds.monitoringeqiupment.model.entity.Equipment;
import com.suaistuds.monitoringeqiupment.model.entity.History;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
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
    public HistoryResponse update(UpdateHistoryRequest req, UserPrincipal currentUser) {
        History h = historyRepository.findById(req.getId())
                .orElseThrow(() -> new ResourceNotFoundException(HISTORY, "id", req.getId()));

        boolean owner = h.getCreatedBy().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));
        if (!owner && !admin) {
            throw new UnauthorizedException(NO_PERM);
        }

        if (req.getEquipmentId() != null) {
            Equipment eq = equipmentRepository.findById(req.getEquipmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", req.getEquipmentId()));
            h.setEquipment(eq);
        }
        if (req.getUserId() != null) {
            User u = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", req.getUserId()));
            h.setUser(u);
        }
        if (req.getResponsibleId() != null) {
            User r = userRepository.findById(req.getResponsibleId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", req.getResponsibleId()));
            h.setResponsible(r);
        }
        if (req.getStatusHistoryId() != null) {
            StatusHistory sh = statusHistoryRepository.findById(req.getStatusHistoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("StatusHistory", "id", req.getStatusHistoryId()));
            h.setStatusHistory(sh);
        }
        if (req.getDate() != null) {
            h.setDate(req.getDate());
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