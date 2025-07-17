package com.suaistuds.monitoringequipment.service.impl;

import com.suaistuds.monitoringequipment.exception.ResourceAlreadyExistsException;
import com.suaistuds.monitoringequipment.exception.ResourceNotFoundException;
import com.suaistuds.monitoringequipment.model.directory.StatusHistory;
import com.suaistuds.monitoringequipment.model.directory.StatusReservation;
import com.suaistuds.monitoringequipment.model.entity.Equipment;
import com.suaistuds.monitoringequipment.model.entity.Reservation;
import com.suaistuds.monitoringequipment.model.entity.User;
import com.suaistuds.monitoringequipment.model.enums.StatusEquipmentName;
import com.suaistuds.monitoringequipment.model.enums.StatusHistoryName;
import com.suaistuds.monitoringequipment.model.enums.StatusReservationName;
import com.suaistuds.monitoringequipment.payload.History.CreateHistoryRequest;
import com.suaistuds.monitoringequipment.payload.PagedResponse;
import com.suaistuds.monitoringequipment.payload.reservation.CreateReservationRequest;
import com.suaistuds.monitoringequipment.payload.reservation.ReservationResponse;
import com.suaistuds.monitoringequipment.payload.reservation.UpdateReservationRequest;
import com.suaistuds.monitoringequipment.repository.*;
import com.suaistuds.monitoringequipment.security.UserPrincipal;
import com.suaistuds.monitoringequipment.service.HistoryService;
import com.suaistuds.monitoringequipment.service.ReservationService;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления резервированием оборудования.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>CRUD-операции с резервациями оборудования</li>
 *   <li>Проверка доступности оборудования в указанные периоды</li>
 *   <li>Управление статусами резерваций и связанного оборудования</li>
 *   <li>Автоматическое создание записей истории при изменении статусов</li>
 *   <li>Фильтрация резерваций по различным параметрам</li>
 * </ul>
 *
 * <p>Особенности реализации:
 * <ul>
 *   <li>Интеграция с сервисом истории ({@link HistoryService})</li>
 *   <li>Автоматическое обновление статусов оборудования при изменении резерваций</li>
 *   <li>Проверка пересечений временных интервалов резерваций</li>
 *   <li>Ограничение доступа через {@code @PreAuthorize}</li>
 * </ul>
 *
 * <p>Обрабатываемые исключения:
 * <ul>
 *   <li>{@link ResourceNotFoundException} - если ресурс не найден</li>
 *   <li>{@link ResourceAlreadyExistsException} - при конфликте резерваций</li>
 *   <li>{@link UnauthorizedException} - при отсутствии прав доступа</li>
 * </ul>
 *
 * @see ReservationService
 * @since 2025-07-13
 */
@Service
@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {

    /**
     * Сообщение об отсутствии прав доступа.
     */
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

    /**
     * Получает все резервации с пагинацией.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     */
    @Override
    public PagedResponse<ReservationResponse> getAll(int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findAll(pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    /**
     * Получает резервацию по ID.
     *
     * @param id идентификатор резервации
     * @return данные резервации {@link ReservationResponse}
     * @throws ResourceNotFoundException если резервация не найдена
     */
    @Override
    public ReservationResponse getById(Long id) {

        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        return toDto(r);
    }

    /**
     * Получает резервации по оборудованию с пагинацией.
     *
     * @param equipmentId идентификатор оборудования
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws ResourceNotFoundException если оборудование не найдено
     */
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

    /**
     * Получает резервации по пользователю (по имени) с пагинацией.
     *
     * @param username имя пользователя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws ResourceNotFoundException если пользователь не найден
     */
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

    /**
     * Получает резервации по пользователю (по ID) с пагинацией.
     *
     * @param userId идентификатор пользователя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws ResourceNotFoundException если пользователь не найден
     */
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

    /**
     * Получает резервации по ответственному лицу (по имени) с пагинацией.
     *
     * @param username имя ответственного
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws ResourceNotFoundException если ответственный не найден
     */
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

    /**
     * Получает резервации по ответственному лицу (по ID) с пагинацией.
     *
     * @param responsibleId идентификатор ответственного
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws ResourceNotFoundException если ответственный не найден
     */
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

    /**
     * Получает резервации по статусу с пагинацией.
     *
     * @param statusId идентификатор статуса резервации
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws ResourceNotFoundException если статус не найден
     */
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

    /**
     * Получает резервации по дате начала с пагинацией.
     *
     * @param startDate дата начала резервации
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     */
    @Override
    public PagedResponse<ReservationResponse> getByStartDate(LocalDateTime startDate, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByStartDate(startDate, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    /**
     * Получает резервации по дате окончания с пагинацией.
     *
     * @param endDate дата окончания резервации
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     */
    @Override
    public PagedResponse<ReservationResponse> getByEndDate(LocalDateTime endDate, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByEndDate(endDate, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    /**
     * Получает резервации с датой начала в указанном диапазоне.
     *
     * @param from начальная дата диапазона
     * @param to конечная дата диапазона
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     */
    @Override
    public PagedResponse<ReservationResponse> getByStartDateBetween(LocalDateTime from, LocalDateTime to, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByStartDateBetween(from, to, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    /**
     * Получает резервации с датой окончания в указанном диапазоне.
     *
     * @param from начальная дата диапазона
     * @param to конечная дата диапазона
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     */
    @Override
    public PagedResponse<ReservationResponse> getByEndDateBetween(LocalDateTime from, LocalDateTime to, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByEndDateBetween(from, to, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    /**
     * Получает текущие активные резервации на указанную дату.
     *
     * @param date дата для проверки активности
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     */
    @Override
    public PagedResponse<ReservationResponse> getCurrentReservations(LocalDateTime date, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reservation> p = reservationRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date, pg);
        List<ReservationResponse> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    /**
     * Проверяет доступность оборудования в указанный период.
     *
     * @param equipmentId идентификатор оборудования
     * @param start начальная дата периода
     * @param end конечная дата периода
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws ResourceNotFoundException если оборудование не найдено
     */
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

    /**
     * Создает новую резервацию оборудования.
     *
     * @param createRequest данные для создания резервации
     * @param currentUser текущий аутентифицированный пользователь
     * @return созданная резервация {@link ReservationResponse}
     * @throws ResourceNotFoundException если оборудование, пользователи или статус не найдены
     * @throws ResourceAlreadyExistsException если есть пересечение с существующими резервациями
     */
    @Override
    @Transactional
    public ReservationResponse create(CreateReservationRequest createRequest, UserPrincipal currentUser) {

        Equipment equipment = equipmentRepository.findById(createRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", createRequest.getEquipmentId()));

        // 1) проверяем пересечения
        List<Reservation> overlaps = reservationRepository.findOverlapping(
                equipment,
                createRequest.getStartDate(),
                createRequest.getEndDate(),
                null
        );
        if (!overlaps.isEmpty()) {
            throw new ResourceAlreadyExistsException("Reservation", "equipmentId",
                    equipment.getId());
        }

        Reservation reservation = new Reservation();
        // установка значений
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

        // Сохранение
        Reservation saved = reservationRepository.save(reservation);

        // Изменение статуса equipment
        equipment.setStatus(statusEquipmentRepository.findByName(StatusEquipmentName.reserved)
                .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "name", StatusEquipmentName.reserved)));
        equipmentRepository.save(equipment);


        return toDto(saved);
    }

    /**
     * Обновляет существующую резервацию.
     *
     * @param updateRequest данные для обновления
     * @param currentUser текущий аутентифицированный пользователь
     * @return обновленная резервация {@link ReservationResponse}
     * @throws ResourceNotFoundException если резервация, оборудование или статус не найдены
     * @throws ResourceAlreadyExistsException если есть пересечение с другими резервациями
     */
    @Override
    @PreAuthorize("hasAnyRole('studio','admin')")
    @Transactional
    public ReservationResponse update(UpdateReservationRequest updateRequest, UserPrincipal currentUser) {

        Reservation reservation = reservationRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", updateRequest.getId()));

        Equipment equipment = equipmentRepository.findById(updateRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment", "id", updateRequest.getEquipmentId()));

        // 1) проверяем пересечения, исключая саму себя
        List<Reservation> overlaps = reservationRepository.findOverlapping(
                equipment,
                updateRequest.getStartDate(),
                updateRequest.getEndDate(),
                reservation.getId()
        );
        if (!overlaps.isEmpty()) {
            throw new ResourceAlreadyExistsException("Reservation", "equipmentId",
                    equipment.getId());
        }

        // 2) сбрасываем статус у старого equipment, если он изменился
        Equipment oldEquip = reservation.getEquipment();
        if (!oldEquip.equals(equipment)) {
            oldEquip.setStatus(statusEquipmentRepository.findByName(StatusEquipmentName.available)
                    .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "name", StatusEquipmentName.available)));
            equipmentRepository.save(oldEquip);
        }

        StatusReservation newStatus = statusReservationRepository.findById(updateRequest.getStatusReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("StatusReservation", "id", updateRequest.getStatusReservationId()));

        reservation.setEquipment(equipment);
        reservation.setStartDate(updateRequest.getStartDate());
        reservation.setEndDate(updateRequest.getEndDate());
        reservation.setStatusReservation(newStatus);


        // Список historyStatuses
        List<StatusHistoryName> historyStatuses = Arrays.asList(
                StatusHistoryName.cancelled,
                StatusHistoryName.rejected,
                StatusHistoryName.returned,
                StatusHistoryName.not_returned
        );

        // Проверка, что статус резервации один из списка historyStatuses
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

            // Создание истории
            historyService.create(historyReq, currentUser);

            // Удаление резервации
            reservationRepository.delete(reservation);

            return toDto(reservation);
        }

        if(newStatus.getName() == StatusReservationName.issued) {
            equipment.setStatus(statusEquipmentRepository.findByName(StatusEquipmentName.issued)
                    .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "name", StatusEquipmentName.issued)));
        }
        else if(newStatus.getName() != StatusReservationName.rejected) {
            equipment.setStatus(statusEquipmentRepository.findByName(StatusEquipmentName.reserved)
                    .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "name", StatusEquipmentName.reserved)));
        }
        equipmentRepository.save(equipment);

        // Сохранение резервации
        Reservation updated = reservationRepository.save(reservation);
        return toDto(updated);
    }

    /**
     * Удаляет резервацию.
     *
     * @param id идентификатор резервации
     * @param currentUser текущий аутентифицированный пользователь
     * @throws ResourceNotFoundException если резервация не найдена
     */
    @Override
    @PreAuthorize("hasAnyRole('studio','admin')")
    @Transactional
    public void delete(Long id, UserPrincipal currentUser) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));

        reservation.getEquipment().setStatus(statusEquipmentRepository.findByName(StatusEquipmentName.available)
                .orElseThrow(() -> new ResourceNotFoundException("StatusEquipment", "name", StatusEquipmentName.available)));
        equipmentRepository.save(reservation.getEquipment());
        reservationRepository.delete(reservation);
    }

    /**
     * Преобразует страницу резерваций в PagedResponse.
     *
     * @param page страница с сущностями Reservation
     * @return страница с DTO {@link PagedResponse}
     */
    private PagedResponse<ReservationResponse> toPagedResponse(Page<Reservation> page) {

        List<ReservationResponse> dtos = page.getContent().stream()
                .map(this::toDto)
                .toList();

        return new PagedResponse<>(dtos, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    /**
     * Преобразует сущность Reservation в DTO.
     *
     * @param reservation сущность резервации
     * @return DTO {@link ReservationResponse}
     */
    private ReservationResponse toDto(Reservation reservation) {

        ReservationResponse dto = modelMapper.map(reservation, ReservationResponse.class);
        dto.setEquipmentId(reservation.getEquipment().getId());
        dto.setUserId(reservation.getUser().getId());
        dto.setResponsibleId(reservation.getResponsible().getId());
        dto.setStatusReservationId(reservation.getStatusReservation().getId());

        return dto;
    }
}
