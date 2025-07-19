package com.suaistuds.monitoringequipment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.suaistuds.monitoringequipment.payload.PagedResponse;
import com.suaistuds.monitoringequipment.payload.Equipment.CreateEquipmentRequest;
import com.suaistuds.monitoringequipment.payload.Equipment.EquipmentResponse;
import com.suaistuds.monitoringequipment.payload.Equipment.UpdateEquipmentRequest;
import com.suaistuds.monitoringequipment.security.CurrentUser;
import com.suaistuds.monitoringequipment.security.UserPrincipal;
import com.suaistuds.monitoringequipment.service.EquipmentService;

/**
 * Контроллер для управления оборудованием.
 *
 * <p>Предоставляет REST API для выполнения операций CRUD с оборудованием:
 * <ul>
 *   <li>Получение списка оборудования с пагинацией</li>
 *   <li>Получение оборудования по различным критериям</li>
 *   <li>Создание, обновление и удаление оборудования</li>
 * </ul>
 *
 * <p>Основные особенности:
 * <ul>
 *   <li>Поддержка пагинации для методов получения списков</li>
 *   <li>Валидация входных параметров</li>
 *   <li>Интеграция с системой аутентификации</li>
 *   <li>Стандартные HTTP статусы ответов</li>
 * </ul>
 *
 * <p>Требования к доступу:
 * <ul>
 *   <li>Для операций создания/изменения/удаления требуется аутентификация</li>
 *   <li>Права доступа проверяются на уровне сервиса</li>
 * </ul>
 *
 * @see EquipmentService
 * @since 2025-07-13
 */
@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {

    @Autowired private EquipmentService equipmentService;

    /**
     * Получает список оборудования с пагинацией.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице (1-100)
     * @return страница с оборудованием {@link PagedResponse}
     */
    @GetMapping
    public PagedResponse<EquipmentResponse> list(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size) {
        return equipmentService.getAll(page, size);
    }

    /**
     * Получает оборудование по ID.
     *
     * @param id идентификатор оборудования
     * @return данные оборудования {@link EquipmentResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если оборудование не найдено
     */
    @GetMapping("/{id}")
    public EquipmentResponse getById(@PathVariable Long id) {
        return equipmentService.getById(id);
    }

    /**
     * Получает оборудование по серийному номеру.
     *
     * @param serialNumber серийный номер оборудования
     * @return данные оборудования {@link EquipmentResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если оборудование не найдено
     */
    @GetMapping("/serial/{serialNumber}")
    public EquipmentResponse getBySerialNumber(@PathVariable String serialNumber) {
        return equipmentService.getBySerialNumber(serialNumber);
    }

    /**
     * Получает оборудование по статусу с пагинацией.
     *
     * @param statusId идентификатор статуса
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с оборудованием {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если статус не найден
     */
    @GetMapping("/status/{statusId}")
    public PagedResponse<EquipmentResponse> byStatus(
            @PathVariable Long statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return equipmentService.getByStatus(statusId, page, size);
    }

    /**
     * Получает оборудование по типу с пагинацией.
     *
     * @param typeId идентификатор типа оборудования
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с оборудованием {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если тип не найден
     */
    @GetMapping("/type/{typeId}")
    public PagedResponse<EquipmentResponse> byType(
            @PathVariable Long typeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return equipmentService.getByType(typeId, page, size);
    }

    /**
     * Получает оборудование по имени пользователя (создателя) с пагинацией.
     *
     * @param username имя пользователя
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с оборудованием {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     */
    @GetMapping("/user/{username}")
    public PagedResponse<EquipmentResponse> byUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return equipmentService.getByCreatedBy(username, page, size);
    }

    /**
     * Получает оборудование по ID пользователя (создателя) с пагинацией.
     *
     * @param userId идентификатор пользователя
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с оборудованием {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     */
    @GetMapping("/user/id/{userId}")
    public PagedResponse<EquipmentResponse> byUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return equipmentService.getByCreatedById(userId, page, size);
    }

    /**
     * Создает новое оборудование.
     *
     * @param createRequest данные для создания оборудования
     * @param currentUser текущий аутентифицированный пользователь
     * @return созданное оборудование {@link EquipmentResponse} с HTTP статусом 201 (Created)
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если у пользователя нет прав
     * @throws com.suaistuds.monitoringequipment.exception.ResourceAlreadyExistsException если оборудование с таким серийным номером уже существует
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если связанные сущности не найдены
     */
    @PostMapping
    public ResponseEntity<EquipmentResponse> create(
            @Valid @RequestBody CreateEquipmentRequest createRequest,
            @CurrentUser UserPrincipal currentUser) {
        EquipmentResponse dto = equipmentService.create(createRequest, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Обновляет существующее оборудование.
     *
     * @param updateRequest данные для обновления оборудования
     * @param currentUser текущий аутентифицированный пользователь
     * @return обновленное оборудование {@link EquipmentResponse}
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если у пользователя нет прав
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если оборудование или связанные сущности не найдены
     * @throws com.suaistuds.monitoringequipment.exception.ResourceAlreadyExistsException если новое значение серийного номера уже занято
     */
    @PutMapping("/{id}")
    public EquipmentResponse update(
            @Valid @RequestBody UpdateEquipmentRequest updateRequest,
            @CurrentUser UserPrincipal currentUser) {
        return equipmentService.update(updateRequest, currentUser);
    }

    /**
     * Удаляет оборудование.
     *
     * @param id идентификатор оборудования
     * @param currentUser текущий аутентифицированный пользователь
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если у пользователя нет прав
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если оборудование не найдено
     * @responseStatus 204 No Content при успешном удалении
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {
        equipmentService.delete(id, currentUser);
    }
}