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
import com.suaistuds.monitoringequipment.payload.History.CreateHistoryRequest;
import com.suaistuds.monitoringequipment.payload.History.HistoryResponse;
import com.suaistuds.monitoringequipment.payload.History.UpdateHistoryRequest;
import com.suaistuds.monitoringequipment.security.CurrentUser;
import com.suaistuds.monitoringequipment.security.UserPrincipal;
import com.suaistuds.monitoringequipment.service.HistoryService;

import java.time.LocalDateTime;

/**
 * Контроллер для работы с историей изменений оборудования.
 *
 * <p>Предоставляет REST API для:
 * <ul>
 *   <li>CRUD-операций с записями истории</li>
 *   <li>Фильтрации записей по различным параметрам</li>
 *   <li>Получения пагинированных списков записей</li>
 * </ul>
 *
 * <p>Основные особенности:
 * <ul>
 *   <li>Поддержка пагинации для всех методов получения списков</li>
 *   <li>Гибкая система фильтрации по различным параметрам</li>
 *   <li>Интеграция с системой аутентификации</li>
 *   <li>Валидация входных параметров</li>
 * </ul>
 *
 * @see HistoryService
 * @since 2025-07-13
 */
@RestController
@RequestMapping("/api/histories")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    /**
     * Получает список всех записей истории с пагинацией.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице (1-100)
     * @return страница с записями истории {@link PagedResponse}
     */
    @GetMapping
    public PagedResponse<HistoryResponse> list(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size) {
        return historyService.getAll(page, size);
    }

    /**
     * Получает запись истории по ID.
     *
     * @param id идентификатор записи истории
     * @return данные записи истории {@link HistoryResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если запись не найдена
     */
    @GetMapping("/{id}")
    public HistoryResponse getById(@PathVariable Long id) {
        return historyService.getById(id);
    }

    /**
     * Создает новую запись в истории.
     *
     * @param createRequest данные для создания записи
     * @param currentUser текущий аутентифицированный пользователь
     * @return созданная запись истории {@link HistoryResponse} с HTTP статусом 201 (Created)
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если у пользователя нет прав
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если связанные сущности не найдены
     */
    @PostMapping
    public ResponseEntity<HistoryResponse> create(
            @Valid @RequestBody CreateHistoryRequest createRequest,
            @CurrentUser UserPrincipal currentUser) {
        HistoryResponse response = historyService.create(createRequest, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Обновляет существующую запись истории.
     *
     * @param updateRequest данные для обновления записи
     * @param currentUser текущий аутентифицированный пользователь
     * @return обновленная запись истории {@link HistoryResponse}
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если у пользователя нет прав
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если запись или связанные сущности не найдены
     */
    @PutMapping("/{id}")
    public HistoryResponse update(
            @Valid @RequestBody UpdateHistoryRequest updateRequest,
            @CurrentUser UserPrincipal currentUser) {
        return historyService.update(updateRequest, currentUser);
    }

    /**
     * Удаляет запись истории.
     *
     * @param id идентификатор записи истории
     * @param currentUser текущий аутентифицированный пользователь
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если у пользователя нет прав
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если запись не найдена
     * @responseStatus 204 No Content при успешном удалении
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {
        historyService.delete(id, currentUser);
    }

    /**
     * Получает записи истории по имени пользователя с пагинацией.
     *
     * @param username имя пользователя
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     */
    @GetMapping("/user/{username}")
    public PagedResponse<HistoryResponse> byUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByUser(username, page, size);
    }

    /**
     * Получает записи истории по ID пользователя с пагинацией.
     *
     * @param userId идентификатор пользователя
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     */
    @GetMapping("/user/id/{userId}")
    public PagedResponse<HistoryResponse> byUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByUserId(userId, page, size);
    }

    /**
     * Получает записи истории по оборудованию с пагинацией.
     *
     * @param equipmentId идентификатор оборудования
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если оборудование не найдено
     */
    @GetMapping("/equipment/{equipmentId}")
    public PagedResponse<HistoryResponse> byEquipment(
            @PathVariable Long equipmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByEquipment(equipmentId, page, size);
    }

    /**
     * Получает записи истории по имени ответственного с пагинацией.
     *
     * @param username имя ответственного
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если ответственный не найден
     */
    @GetMapping("/responsible/{username}")
    public PagedResponse<HistoryResponse> byResponsible(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByResponsible(username, page, size);
    }

    /**
     * Получает записи истории по ID ответственного с пагинацией.
     *
     * @param responsibleId идентификатор ответственного
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если ответственный не найден
     */
    @GetMapping("/responsible/id/{responsibleId}")
    public PagedResponse<HistoryResponse> byResponsibleId(
            @PathVariable Long responsibleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByResponsibleId(responsibleId, page, size);
    }

    /**
     * Получает записи истории по статусу с пагинацией.
     *
     * @param statusId идентификатор статуса
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если статус не найден
     */
    @GetMapping("/status/{statusId}")
    public PagedResponse<HistoryResponse> byStatus(
            @PathVariable Long statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByStatus(statusId, page, size);
    }

    /**
     * Получает записи истории по конкретной дате с пагинацией.
     *
     * @param date дата для фильтрации (в формате ISO 8601)
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     */
    @GetMapping("/date")
    public PagedResponse<HistoryResponse> byDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByDate(date, page, size);
    }

    /**
     * Получает записи истории за период времени с пагинацией.
     *
     * @param startDate начальная дата периода (в формате ISO 8601)
     * @param endDate конечная дата периода (в формате ISO 8601)
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с записями истории {@link PagedResponse}
     */
    @GetMapping("/date-range")
    public PagedResponse<HistoryResponse> byDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return historyService.getByDateBetween(startDate, endDate, page, size);
    }
}