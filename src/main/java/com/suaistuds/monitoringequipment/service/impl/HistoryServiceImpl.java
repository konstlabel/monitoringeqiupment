package com.suaistuds.monitoringequipment.service.impl;

import com.suaistuds.monitoringequipment.exception.ResourceNotFoundException;
import com.suaistuds.monitoringequipment.exception.UnauthorizedException;
import com.suaistuds.monitoringequipment.model.directory.StatusHistory;
import com.suaistuds.monitoringequipment.model.entity.Equipment;
import com.suaistuds.monitoringequipment.model.entity.History;
import com.suaistuds.monitoringequipment.model.entity.User;
import com.suaistuds.monitoringequipment.model.enums.RoleName;
import com.suaistuds.monitoringequipment.model.enums.StatusEquipmentName;
import com.suaistuds.monitoringequipment.model.enums.StatusHistoryName;
import com.suaistuds.monitoringequipment.payload.History.CreateHistoryRequest;
import com.suaistuds.monitoringequipment.payload.History.HistoryResponse;
import com.suaistuds.monitoringequipment.payload.History.UpdateHistoryRequest;
import com.suaistuds.monitoringequipment.payload.PagedResponse;
import com.suaistuds.monitoringequipment.repository.*;
import com.suaistuds.monitoringequipment.security.UserPrincipal;
import com.suaistuds.monitoringequipment.service.HistoryService;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация сервиса для работы с историей изменений оборудования.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>CRUD-операции с записями истории</li>
 *   <li>Фильтрация записей по различным параметрам</li>
 *   <li>Автоматическое обновление статусов оборудования при создании/изменении записей</li>
 *   <li>Ограничение доступа для определенных ролей</li>
 * </ul>
 *
 * <p>Особенности реализации:
 * <ul>
 *   <li>Интеграция с репозиториями Equipment, User и StatusHistory</li>
 *   <li>Автоматическое обновление статуса оборудования при изменении истории</li>
 *   <li>Поддержка пагинации для всех методов поиска</li>
 *   <li>Использование ModelMapper для преобразования сущностей в DTO</li>
 * </ul>
 *
 * <p>Обрабатываемые исключения:
 * <ul>
 *   <li>{@link ResourceNotFoundException} - если ресурс не найден</li>
 *   <li>{@link UnauthorizedException} - при отсутствии прав доступа</li>
 * </ul>
 *
 * @see HistoryService
 * @since 2025-07-13
 */
@Service
@Transactional(readOnly = true)
public class HistoryServiceImpl implements HistoryService {

    /**
     * Сообщение об отсутствии прав доступа.
     */
    private static final String NO_PERM    = "You don't have permission to make this operation";

    @Autowired private HistoryRepository historyRepository;
    @Autowired private EquipmentRepository equipmentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StatusEquipmentRepository statusEquipmentRepository;
    @Autowired private StatusHistoryRepository statusHistoryRepository;
    @Autowired private ModelMapper modelMapper;

    /**
     * Получает все записи истории с пагинацией.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     */
    @Override
    public PagedResponse<HistoryResponse> getAll(int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<History> p = historyRepository.findAll(pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    /**
     * Получает запись истории по ID.
     *
     * @param id идентификатор записи истории
     * @return данные записи истории {@link HistoryResponse}
     * @throws ResourceNotFoundException если запись не найдена
     */
    @Override
    public HistoryResponse getById(Long id) {

        History history = historyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("History", "id", id));
        return toDto(history);
    }

    /**
     * Получает записи истории по пользователю (по имени) с пагинацией.
     *
     * @param username имя пользователя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     * @throws ResourceNotFoundException если пользователь не найден
     */
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

    /**
     * Получает записи истории по пользователю (по ID) с пагинацией.
     *
     * @param userId идентификатор пользователя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     * @throws ResourceNotFoundException если пользователь не найден
     */
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

    /**
     * Получает записи истории по оборудованию с пагинацией.
     *
     * @param equipmentId идентификатор оборудования
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     * @throws ResourceNotFoundException если оборудование не найдено
     */
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

    /**
     * Получает записи истории по ответственному лицу (по имени) с пагинацией.
     *
     * @param username имя ответственного
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     * @throws ResourceNotFoundException если ответственный не найден
     */
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

    /**
     * Получает записи истории по ответственному лицу (по ID) с пагинацией.
     *
     * @param responsibleId идентификатор ответственного
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     * @throws ResourceNotFoundException если ответственный не найден
     */
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

    /**
     * Получает записи истории по статусу с пагинацией.
     *
     * @param statusId идентификатор статуса истории
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     * @throws ResourceNotFoundException если статус не найден
     */
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

    /**
     * Получает записи истории по конкретной дате с пагинацией.
     *
     * @param date дата для фильтрации
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     */
    @Override
    public PagedResponse<HistoryResponse> getByDate(LocalDateTime date, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<History> p = historyRepository.findByDate(date, pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    /**
     * Получает записи истории за период времени с пагинацией.
     *
     * @param startDate начальная дата периода
     * @param endDate конечная дата периода
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     */
    @Override
    public PagedResponse<HistoryResponse> getByDateBetween(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<History> p = historyRepository.findByDateBetween(startDate, endDate, pg);
        List<HistoryResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    /**
     * Создает новую запись в истории.
     *
     * @param createRequest данные для создания записи
     * @param currentUser текущий аутентифицированный пользователь
     * @return созданная запись истории {@link HistoryResponse}
     * @throws UnauthorizedException если у пользователя нет прав на создание
     * @throws ResourceNotFoundException если оборудование, пользователи или статус не найдены
     */
    @Override
    @Transactional
    public HistoryResponse create(CreateHistoryRequest createRequest, UserPrincipal currentUser) {

        if (isNotStudioOrAdmin(currentUser)) {
            throw new UnauthorizedException(NO_PERM);
        }

        if (isNotStudioOrAdmin(currentUser)) {
            throw new UnauthorizedException(NO_PERM);
        }

        Equipment equipment = equipmentRepository.findById(createRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", createRequest.getEquipmentId()));

        User user = userRepository.findById(createRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", createRequest.getUserId()));

        User responsible = userRepository.findById(createRequest.getResponsibleId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", createRequest.getResponsibleId()));

        StatusHistory status = statusHistoryRepository.findById(createRequest.getStatusHistoryId())
                .orElseThrow(() -> new ResourceNotFoundException("StatusHistory", "id", createRequest.getStatusHistoryId()));

        History history = History.builder()
                .equipment(equipment)
                .user(user)
                .responsible(responsible)
                .statusHistory(status)
                .date(createRequest.getDate())
                .build();

        if (status.getName() == StatusHistoryName.not_returned) {
            equipment.setStatus(statusEquipmentRepository.findByName(StatusEquipmentName.issued)
                    .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "name", StatusEquipmentName.issued)));
            equipmentRepository.save(equipment);
        }
        else if (status.getName() == StatusHistoryName.returned) {
            equipment.setStatus(statusEquipmentRepository.findByName(StatusEquipmentName.available)
                    .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "name", StatusEquipmentName.available)));
            equipmentRepository.save(equipment);
        }

        History saved = historyRepository.save(history);
        return toDto(saved);
    }

    /**
     * Обновляет существующую запись истории.
     *
     * @param updateRequest данные для обновления записи
     * @param currentUser текущий аутентифицированный пользователь
     * @return обновленная запись истории {@link HistoryResponse}
     * @throws UnauthorizedException если у пользователя нет прав на обновление
     * @throws ResourceNotFoundException если запись, оборудование, пользователи или статус не найдены
     */
    @Override
    @Transactional
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

        if (status.getName() == StatusHistoryName.not_returned) {
            equipment.setStatus(statusEquipmentRepository.findByName(StatusEquipmentName.issued)
                    .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "name", StatusEquipmentName.issued)));
            equipmentRepository.save(equipment);
        }
        else if (status.getName() == StatusHistoryName.returned) {
            equipment.setStatus(statusEquipmentRepository.findByName(StatusEquipmentName.available)
                    .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "name", StatusEquipmentName.available)));
            equipmentRepository.save(equipment);
        }

        History updated = historyRepository.save(history);
        return toDto(updated);
    }

    /**
     * Удаляет запись истории.
     *
     * @param id идентификатор записи истории
     * @param currentUser текущий аутентифицированный пользователь
     * @throws UnauthorizedException если у пользователя нет прав на удаление
     * @throws ResourceNotFoundException если запись не найдена
     */
    @Override
    @Transactional
    public void delete(Long id, UserPrincipal currentUser) {

        if (isNotStudioOrAdmin(currentUser)) {
            throw new UnauthorizedException(NO_PERM);
        }

        History h = historyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("History", "id", id));

        historyRepository.delete(h);
    }

    /**
     * Преобразует страницу записей истории в PagedResponse.
     *
     * @param page страница с сущностями History
     * @return страница с DTO {@link PagedResponse}
     */
    private PagedResponse<HistoryResponse> toPagedResponse(Page<History> page) {

        List<HistoryResponse> dtos = page.getContent().stream()
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
     * Преобразует сущность History в DTO.
     *
     * @param history сущность истории
     * @return DTO {@link HistoryResponse}
     */
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