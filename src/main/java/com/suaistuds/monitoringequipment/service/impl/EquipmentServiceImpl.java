package com.suaistuds.monitoringequipment.service.impl;

import com.suaistuds.monitoringequipment.exception.ResourceAlreadyExistsException;
import com.suaistuds.monitoringequipment.exception.ResourceNotFoundException;
import com.suaistuds.monitoringequipment.exception.UnauthorizedException;
import com.suaistuds.monitoringequipment.model.directory.StatusEquipment;
import com.suaistuds.monitoringequipment.model.directory.Type;
import com.suaistuds.monitoringequipment.model.entity.Equipment;
import com.suaistuds.monitoringequipment.model.entity.User;
import com.suaistuds.monitoringequipment.model.enums.RoleName;
import com.suaistuds.monitoringequipment.payload.Equipment.CreateEquipmentRequest;
import com.suaistuds.monitoringequipment.payload.Equipment.EquipmentResponse;
import com.suaistuds.monitoringequipment.payload.Equipment.UpdateEquipmentRequest;
import com.suaistuds.monitoringequipment.payload.PagedResponse;
import com.suaistuds.monitoringequipment.repository.StatusEquipmentRepository;
import com.suaistuds.monitoringequipment.repository.TypeRepository;
import com.suaistuds.monitoringequipment.repository.EquipmentRepository;
import com.suaistuds.monitoringequipment.repository.UserRepository;
import com.suaistuds.monitoringequipment.security.UserPrincipal;
import com.suaistuds.monitoringequipment.service.EquipmentService;
import com.suaistuds.monitoringequipment.util.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с оборудованием.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>CRUD-операции с оборудованием</li>
 *   <li>Фильтрация оборудования по различным параметрам</li>
 *   <li>Проверка прав доступа для операций</li>
 *   <li>Валидация входных данных</li>
 * </ul>
 *
 * <p>Особенности реализации:
 * <ul>
 *   <li>Использует Spring Data JPA репозитории для доступа к данным</li>
 *   <li>Все методы по умолчанию выполняются в режиме только для чтения</li>
 *   <li>Методы изменения данных помечены {@code @Transactional}</li>
 *   <li>Автоматическое преобразование сущностей в DTO через ModelMapper</li>
 * </ul>
 *
 * <p>Обрабатываемые исключения:
 * <ul>
 *   <li>{@link ResourceNotFoundException} - если ресурс не найден</li>
 *   <li>{@link ResourceAlreadyExistsException} - если оборудование с таким серийным номером уже существует</li>
 *   <li>{@link UnauthorizedException} - если у пользователя нет прав на операцию</li>
 * </ul>
 *
 * @see EquipmentService
 * @since 2025-07-13
 */
@Service
@Transactional(readOnly = true)
public class EquipmentServiceImpl implements EquipmentService {

    /**
     * Сообщение об отсутствии прав доступа.
     */
    private static final String NO_PERM     = "You don't have permission to make this operation";

    @Autowired private EquipmentRepository equipmentRepository;
    @Autowired private StatusEquipmentRepository statusEquipmentRepository;
    @Autowired private TypeRepository typeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ModelMapper modelMapper;

    /**
     * Получает все записи оборудования с пагинацией.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с оборудованием в формате {@link PagedResponse}
     */
    @Override
    public PagedResponse<EquipmentResponse> getAll(int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Equipment> p = equipmentRepository.findAll(pg);
        List<EquipmentResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    /**
     * Получает оборудование по ID.
     *
     * @param id идентификатор оборудования
     * @return данные оборудования в формате {@link EquipmentResponse}
     * @throws ResourceNotFoundException если оборудование не найдено
     */
    @Override
    public EquipmentResponse getById(Long id) {

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", id));
        return toDto(equipment);
    }

    /**
     * Получает оборудование по серийному номеру.
     *
     * @param serialNumber серийный номер оборудования
     * @return данные оборудования в формате {@link EquipmentResponse}
     * @throws ResourceNotFoundException если оборудование не найдено
     */
    @Override
    public EquipmentResponse getBySerialNumber(String serialNumber) {

        Equipment equipment = equipmentRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "serial number", serialNumber));
        return toDto(equipment);
    }

    /**
     * Получает оборудование по статусу с пагинацией.
     *
     * @param statusId идентификатор статуса
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с оборудованием в формате {@link PagedResponse}
     * @throws ResourceNotFoundException если статус не найден
     */
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

    /**
     * Получает оборудование по типу с пагинацией.
     *
     * @param typeId идентификатор типа оборудования
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с оборудованием в формате {@link PagedResponse}
     * @throws ResourceNotFoundException если тип не найден
     */
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

    /**
     * Получает оборудование по имени создателя с пагинацией.
     *
     * @param username имя пользователя создателя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с оборудованием в формате {@link PagedResponse}
     * @throws ResourceNotFoundException если пользователь не найден
     */
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

    /**
     * Получает оборудование по ID создателя с пагинацией.
     *
     * @param userId идентификатор пользователя создателя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с оборудованием в формате {@link PagedResponse}
     * @throws ResourceNotFoundException если пользователь не найден
     */
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

    /**
     * Создает новое оборудование.
     *
     * @param createRequest данные для создания оборудования
     * @param currentUser текущий аутентифицированный пользователь
     * @return созданное оборудование в формате {@link EquipmentResponse}
     * @throws UnauthorizedException если у пользователя нет прав на создание
     * @throws ResourceAlreadyExistsException если оборудование с таким серийным номером уже существует
     * @throws ResourceNotFoundException если статус или тип не найдены
     */
    @Override
    @Transactional
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

    /**
     * Обновляет существующее оборудование.
     *
     * @param updateRequest данные для обновления оборудования
     * @param currentUser текущий аутентифицированный пользователь
     * @return обновленное оборудование в формате {@link EquipmentResponse}
     * @throws UnauthorizedException если у пользователя нет прав на обновление
     * @throws ResourceNotFoundException если оборудование, статус или тип не найдены
     * @throws ResourceAlreadyExistsException если оборудование с таким серийным номером уже существует
     */
    @Override
    @Transactional
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

    /**
     * Удаляет оборудование.
     *
     * @param id идентификатор оборудования
     * @param currentUser текущий аутентифицированный пользователь
     * @throws UnauthorizedException если у пользователя нет прав на удаление
     * @throws ResourceNotFoundException если оборудование не найдено
     */
    @Override
    @Transactional
    public void delete(Long id, UserPrincipal currentUser) {

        if (isNotStudioOrAdmin(currentUser)) {
            throw new UnauthorizedException(NO_PERM);
        }

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", id));

        equipmentRepository.delete(equipment);
    }

    /**
     * Преобразует страницу сущностей Equipment в PagedResponse.
     *
     * @param page страница с сущностями Equipment
     * @return страница с DTO в формате {@link PagedResponse}
     */
    private PagedResponse<EquipmentResponse> toPagedResponse(Page<Equipment> page) {
        List<EquipmentResponse> dtos = page.getContent().stream()
                .map(this::toDto)
                .toList();
        return new PagedResponse<>(dtos, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    /**
     * Проверяет, имеет ли пользователь права Studio или Admin.
     *
     * @param currentUser текущий аутентифицированный пользователь
     * @return true если пользователь не имеет прав Studio или Admin, иначе false
     */
    private boolean isNotStudioOrAdmin(UserPrincipal currentUser) {
        boolean studio = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.studio.name()));
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));

        return !studio && !admin;
    }

    /**
     * Проверяет уникальность серийного номера оборудования.
     *
     * @param serialNumber серийный номер для проверки
     * @param e1 опциональное оборудование для исключения из проверки (при обновлении)
     * @return true если серийный номер уже существует, иначе false
     */
    private boolean checkSerialNumber(String serialNumber, Optional<Equipment> e1) {

        Optional<Equipment> e2 = equipmentRepository.findBySerialNumber(serialNumber);

        if (e1.isPresent() && e2.isPresent() && e1.get().equals(e2.get())) {
            return false;
        }

        return e2.isPresent();
    }

    /**
     * Преобразует сущность Equipment в DTO.
     *
     * @param equipment сущность оборудования
     * @return DTO в формате {@link EquipmentResponse}
     */
    private EquipmentResponse toDto(Equipment equipment) {

        EquipmentResponse dto = modelMapper.map(equipment, EquipmentResponse.class);

        dto.setName(equipment.getName());
        dto.setSerialNumber(equipment.getSerialNumber());
        dto.setStatusId(equipment.getStatus().getId());
        dto.setTypeId(equipment.getType().getId());

        return dto;
    }
}