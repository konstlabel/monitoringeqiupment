package com.suaistuds.monitoringeqiupment.service.impl;

import com.suaistuds.monitoringeqiupment.exception.ResourceAlreadyExistsException;
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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EquipmentServiceImpl implements EquipmentService {
    private static final String NO_PERM     = "You don't have permission to make this operation";

    @Autowired private EquipmentRepository equipmentRepository;
    @Autowired private StatusEquipmentRepository statusEquipmentRepository;
    @Autowired private TypeRepository typeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public PagedResponse<EquipmentResponse> getAll(int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Equipment> p = equipmentRepository.findAll(pg);
        List<EquipmentResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public EquipmentResponse getById(Long id) {

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", id));
        return toDto(equipment);
    }

    @Override
    public EquipmentResponse getBySerialNumber(String serialNumber) {

        Equipment equipment = equipmentRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "serial number", serialNumber));
        return toDto(equipment);
    }

    @Override
    public PagedResponse<EquipmentResponse> getByStatus(Long statusId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        StatusEquipment status = statusEquipmentRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Status", "id", statusId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Equipment> p = equipmentRepository.findByStatus(status, pg);
        List<EquipmentResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<EquipmentResponse> getByType(Long typeId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Type type = typeRepository.findById(typeId)
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", typeId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Equipment> p = equipmentRepository.findByType(type, pg);
        List<EquipmentResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<EquipmentResponse> getByCreatedBy(String username, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Equipment> p = equipmentRepository.findByCreatedBy(u.getId(), pg);
        List<EquipmentResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public PagedResponse<EquipmentResponse> getByCreatedById(Long userId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Equipment> p = equipmentRepository.findByCreatedBy(user.getId(), pg);
        List<EquipmentResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public EquipmentResponse create(CreateEquipmentRequest createRequest, UserPrincipal currentUser) {

        if (isNotStudioOrAdmin(currentUser)) {
            throw new UnauthorizedException(NO_PERM);
        }

        if (this.checkSerialNumber(createRequest.getSerialNumber(), Optional.empty())) {
            throw new ResourceAlreadyExistsException("Equipment", "serialNumber", createRequest.getSerialNumber());
        }

        Equipment equipment = new Equipment();

        equipment.setName(createRequest.getName());
        equipment.setSerialNumber(createRequest.getSerialNumber());

        StatusEquipment status = statusEquipmentRepository.findById(createRequest.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "id", createRequest.getStatusId()));
        equipment.setStatus(status);

        Type type = typeRepository.findById(createRequest.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", createRequest.getTypeId()));
        equipment.setType(type);

        equipment.setCreatedBy(currentUser.getId());
        Equipment saved = equipmentRepository.save(equipment);

        return toDto(saved);
    }

    @Override
    public EquipmentResponse update(UpdateEquipmentRequest updateRequest, UserPrincipal currentUser) {

        if (isNotStudioOrAdmin(currentUser)) {
            throw new UnauthorizedException(NO_PERM);
        }

        Equipment equipment = equipmentRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", updateRequest.getId()));

        if (this.checkSerialNumber(updateRequest.getSerialNumber(), Optional.of(equipment))) {
            throw new ResourceAlreadyExistsException("Equipment", "serialNumber", updateRequest.getSerialNumber());
        }

        equipment.setName(updateRequest.getName());

        equipment.setSerialNumber(updateRequest.getSerialNumber());

        StatusEquipment status = statusEquipmentRepository.findById(updateRequest.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "id", updateRequest.getStatusId()));
        equipment.setStatus(status);

        Type type = typeRepository.findById(updateRequest.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", updateRequest.getTypeId()));
        equipment.setType(type);

        Equipment updated = equipmentRepository.save(equipment);
        return toDto(updated);
    }

    @Override
    public void delete(Long id, UserPrincipal currentUser) {

        if (isNotStudioOrAdmin(currentUser)) {
            throw new UnauthorizedException(NO_PERM);
        }

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", id));

        equipmentRepository.delete(equipment);
    }

    private PagedResponse<EquipmentResponse> toPagedResponse(Page<Equipment> page) {
        List<EquipmentResponse> dtos = page.getContent().stream()
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

    private boolean checkSerialNumber(String serialNumber, Optional<Equipment> e1) {

        Optional<Equipment> e2 = equipmentRepository.findBySerialNumber(serialNumber);

        if (e1.isPresent() && e2.isPresent() && e1.get().equals(e2.get())) {
            return false;
        }

        return e2.isPresent();
    }

    private EquipmentResponse toDto(Equipment equipment) {

        EquipmentResponse dto = modelMapper.map(equipment, EquipmentResponse.class);

        dto.setName(equipment.getName());
        dto.setSerialNumber(equipment.getSerialNumber());
        dto.setStatusId(equipment.getStatus().getId());
        dto.setTypeId(equipment.getType().getId());

        return dto;
    }
}