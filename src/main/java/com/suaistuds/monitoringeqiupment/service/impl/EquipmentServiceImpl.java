package com.suaistuds.monitoringeqiupment.service.impl;


import com.suaistuds.monitoringeqiupment.exception.ResourceNotFoundException;
import com.suaistuds.monitoringeqiupment.exception.UnauthorizedException;
import com.suaistuds.monitoringeqiupment.model.directory.StatusEquipment;
import com.suaistuds.monitoringeqiupment.model.directory.Type;
import com.suaistuds.monitoringeqiupment.model.entity.Equipment;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
import com.suaistuds.monitoringeqiupment.payload.Equipment.CreateEquipmentRequest;
import com.suaistuds.monitoringeqiupment.payload.Equipment.EquipmentResponse;
import com.suaistuds.monitoringeqiupment.payload.Equipment.UpdateEquipmentRequest;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.repository.StatusEquipmentRepository;
import com.suaistuds.monitoringeqiupment.repository.TypeRepository;
import com.suaistuds.monitoringeqiupment.repository.EquipmentRepository;
import com.suaistuds.monitoringeqiupment.repository.UserRepository;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import com.suaistuds.monitoringeqiupment.service.EquipmentService;
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
public class EquipmentServiceImpl implements EquipmentService {
    private static final String CREATED_AT = "createdAt";
    private static final String EQUIPMENT   = "Equipment";
    private static final String NO_PERM     = "You don't have permission to make this operation";

    @Autowired private EquipmentRepository equipmentRepository;
    @Autowired private StatusEquipmentRepository statusEquipmentRepository;
    @Autowired private TypeRepository typeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public PagedResponse<EquipmentResponse> getAll(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by(CREATED_AT).descending());
        Page<Equipment> p = equipmentRepository.findAll(pg);
        List<EquipmentResponse> dtos = p.getContent().stream().map(this::toDto).toList();
        return new PagedResponse<>(dtos, p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages(), p.isLast());
    }

    @Override
    public EquipmentResponse getById(Long id) {
        Equipment e = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EQUIPMENT, "id", id));
        return toDto(e);
    }

    @Override
    @Transactional
    public EquipmentResponse create(CreateEquipmentRequest createRequest, UserPrincipal currentUser) {
        Equipment e = new Equipment();
        e.setName(createRequest.getName());
        e.setSerialNumber(createRequest.getSerialNumber());

        StatusEquipment status = statusEquipmentRepository.findById(createRequest.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "id", createRequest.getStatusId()));
        e.setStatus(status);

        Type type = typeRepository.findById(createRequest.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", createRequest.getTypeId()));
        e.setType(type);

        e.setCreatedBy(currentUser.getId());
        Equipment saved = equipmentRepository.save(e);
        return toDto(saved);
    }

    @Override
    @Transactional
    public EquipmentResponse update(UpdateEquipmentRequest updateRequest, UserPrincipal currentUser) {
        Equipment e = equipmentRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException(EQUIPMENT, "id", updateRequest.getId()));

        boolean owner = e.getCreatedBy().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));
        if (!owner && !admin) {
            throw new UnauthorizedException(NO_PERM);
        }

        e.setName(updateRequest.getName());
        e.setSerialNumber(updateRequest.getSerialNumber());

        StatusEquipment status = statusEquipmentRepository.findById(updateRequest.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "id", updateRequest.getStatusId()));
        e.setStatus(status);

        Type type = typeRepository.findById(updateRequest.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", updateRequest.getTypeId()));
        e.setType(type);

        Equipment updated = equipmentRepository.save(e);
        return toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id, UserPrincipal currentUser) {
        Equipment e = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EQUIPMENT, "id", id));

        boolean owner = e.getCreatedBy().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));
        if (!owner && !admin) {
            throw new UnauthorizedException(NO_PERM);
        }

        equipmentRepository.delete(e);
    }

    @Override
    public PagedResponse<EquipmentResponse> getByUser(String username, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        User u = userRepository.getUserByName(username);
        Pageable pg = PageRequest.of(page, size, Sort.by(CREATED_AT).descending());
        Page<Equipment> p = equipmentRepository.findByCreatedBy(u.getId(), pg);
        List<EquipmentResponse> dtos = p.getContent().stream().map(this::toDto).toList();
        return new PagedResponse<>(dtos, p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages(), p.isLast());
    }

    private EquipmentResponse toDto(Equipment e) {
        EquipmentResponse dto = modelMapper.map(e, EquipmentResponse.class);
        dto.setName(e.getName());
        dto.setStatusId(e.getStatus().getId());
        dto.setTypeId(e.getType().getId());
        return dto;
    }
}

