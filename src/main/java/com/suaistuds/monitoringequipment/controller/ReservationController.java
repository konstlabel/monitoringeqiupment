package com.suaistuds.monitoringequipment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.suaistuds.monitoringequipment.payload.PagedResponse;
import com.suaistuds.monitoringequipment.payload.reservation.CreateReservationRequest;
import com.suaistuds.monitoringequipment.payload.reservation.ReservationResponse;
import com.suaistuds.monitoringequipment.payload.reservation.UpdateReservationRequest;
import com.suaistuds.monitoringequipment.security.CurrentUser;
import com.suaistuds.monitoringequipment.security.UserPrincipal;
import com.suaistuds.monitoringequipment.service.ReservationService;

import java.time.LocalDateTime;

/**
 * Контроллер для управления резервированием оборудования.
 *
 * <p>Предоставляет REST API для:
 * <ul>
 *   <li>CRUD-операций с резервациями</li>
 *   <li>Фильтрации резерваций по различным параметрам</li>
 *   <li>Проверки доступности оборудования</li>
 *   <li>Получения текущих активных резерваций</li>
 * </ul>
 *
 * <p>Основные особенности:
 * <ul>
 *   <li>Поддержка пагинации для всех методов получения списков</li>
 *   <li>Гибкая система фильтрации по датам, пользователям, оборудованию</li>
 *   <li>Интеграция с системой аутентификации</li>
 *   <li>Валидация входных параметров</li>
 * </ul>
 *
 * @see ReservationService
 * @since 2025-07-13
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * Получает список всех резерваций с пагинацией.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице (1-100)
     * @return страница с резервациями {@link PagedResponse}
     */
    @GetMapping
    public PagedResponse<ReservationResponse> list(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size) {
        return reservationService.getAll(page, size);
    }

    /**
     * Получает резервацию по ID.
     *
     * @param id идентификатор резервации
     * @return данные резервации {@link ReservationResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если резервация не найдена
     */
    @GetMapping("/{id}")
    public ReservationResponse getById(@PathVariable Long id) {
        return reservationService.getById(id);
    }

    /**
     * Создает новую резервацию оборудования.
     *
     * @param createRequest данные для создания резервации
     * @param currentUser текущий аутентифицированный пользователь
     * @return созданная резервация {@link ReservationResponse} с HTTP статусом 201 (Created)
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если у пользователя нет прав
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если связанные сущности не найдены
     * @throws com.suaistuds.monitoringequipment.exception.ResourceAlreadyExistsException если есть пересечение с существующими резервациями
     */
    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @Valid @RequestBody CreateReservationRequest createRequest,
            @CurrentUser UserPrincipal currentUser) {
        ReservationResponse dto = reservationService.create(createRequest, currentUser);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    /**
     * Обновляет существующую резервацию.
     *
     * @param updateRequest данные для обновления резервации
     * @param currentUser текущий аутентифицированный пользователь
     * @return обновленная резервация {@link ReservationResponse}
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если у пользователя нет прав
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если резервация или связанные сущности не найдены
     * @throws com.suaistuds.monitoringequipment.exception.ResourceAlreadyExistsException если есть пересечение с другими резервациями
     */
    @PutMapping("/{id}")
    public ReservationResponse update(
            @Valid @RequestBody UpdateReservationRequest updateRequest,
            @CurrentUser UserPrincipal currentUser) {
        return reservationService.update(updateRequest, currentUser);
    }

    /**
     * Удаляет резервацию.
     *
     * @param id идентификатор резервации
     * @param currentUser текущий аутентифицированный пользователь
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если у пользователя нет прав
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если резервация не найдена
     * @responseStatus 204 No Content при успешном удалении
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {
        reservationService.delete(id, currentUser);
    }

    /**
     * Получает резервации по имени пользователя с пагинацией.
     *
     * @param username имя пользователя
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     */
    @GetMapping("/user/{username}")
    public PagedResponse<ReservationResponse> byUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByUser(username, page, size);
    }

    /**
     * Получает резервации по ID пользователя с пагинацией.
     *
     * @param userId идентификатор пользователя
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     */
    @GetMapping("/user/id/{userId}")
    public PagedResponse<ReservationResponse> byUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByUserId(userId, page, size);
    }

    /**
     * Получает резервации по имени ответственного с пагинацией.
     *
     * @param username имя ответственного
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если ответственный не найден
     */
    @GetMapping("/responsible/{username}")
    public PagedResponse<ReservationResponse> byResponsible(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByResponsible(username, page, size);
    }

    /**
     * Получает резервации по ID ответственного с пагинацией.
     *
     * @param responsibleId идентификатор ответственного
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если ответственный не найден
     */
    @GetMapping("/responsible/id/{responsibleId}")
    public PagedResponse<ReservationResponse> byResponsibleId(
            @PathVariable Long responsibleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByResponsibleId(responsibleId, page, size);
    }

    /**
     * Получает резервации по оборудованию с пагинацией.
     *
     * @param equipmentId идентификатор оборудования
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если оборудование не найдено
     */
    @GetMapping("/equipment/{equipmentId}")
    public PagedResponse<ReservationResponse> byEquipment(
            @PathVariable Long equipmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByEquipment(equipmentId, page, size);
    }

    /**
     * Получает резервации по статусу с пагинацией.
     *
     * @param statusId идентификатор статуса
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если статус не найден
     */
    @GetMapping("/status/{statusId}")
    public PagedResponse<ReservationResponse> byStatus(
            @PathVariable Long statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByStatus(statusId, page, size);
    }

    /**
     * Получает резервации по дате начала с пагинацией.
     *
     * @param startDate дата начала резервации (в формате ISO 8601)
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     */
    @GetMapping("/start-date")
    public PagedResponse<ReservationResponse> byStartDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByStartDate(startDate, page, size);
    }

    /**
     * Получает резервации по дате окончания с пагинацией.
     *
     * @param endDate дата окончания резервации (в формате ISO 8601)
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     */
    @GetMapping("/end-date")
    public PagedResponse<ReservationResponse> byEndDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByEndDate(endDate, page, size);
    }

    /**
     * Получает резервации с датой начала в указанном диапазоне.
     *
     * @param from начальная дата диапазона (в формате ISO 8601)
     * @param to конечная дата диапазона (в формате ISO 8601)
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     */
    @GetMapping("/date-range")
    public PagedResponse<ReservationResponse> byDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getByStartDateBetween(from, to, page, size);
    }

    /**
     * Получает текущие активные резервации на указанную дату.
     *
     * @param date дата для проверки (в формате ISO 8601)
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}
     */
    @GetMapping("/current")
    public PagedResponse<ReservationResponse> currentReservations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getCurrentReservations(date, page, size);
    }

    /**
     * Проверяет доступность оборудования в указанный период.
     *
     * @param equipmentId идентификатор оборудования
     * @param start начальная дата периода (в формате ISO 8601)
     * @param end конечная дата периода (в формате ISO 8601)
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с резервациями {@link PagedResponse}, пересекающими указанный период
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если оборудование не найдено
     */
    @GetMapping("/availability")
    public PagedResponse<ReservationResponse> checkAvailability(
            @RequestParam Long equipmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return reservationService.getEquipmentAvailability(equipmentId, start, end, page, size);
    }
}