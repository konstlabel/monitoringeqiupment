package com.suaistuds.monitoringequipment.service;

import com.suaistuds.monitoringequipment.payload.Equipment.UpdateEquipmentRequest;
import com.suaistuds.monitoringequipment.security.UserPrincipal;
import com.suaistuds.monitoringequipment.payload.Equipment.CreateEquipmentRequest;
import com.suaistuds.monitoringequipment.payload.Equipment.EquipmentResponse;
import com.suaistuds.monitoringequipment.payload.PagedResponse;

/**
 * Сервис для работы с оборудованием.
 * Предоставляет методы для CRUD-операций и фильтрации оборудования.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>Получение оборудования с пагинацией</li>
 *   <li>Поиск оборудования по различным критериям</li>
 *   <li>Создание, обновление и удаление оборудования</li>
 *   <li>Фильтрация по статусу, типу и создателю</li>
 * </ul>
 *
 * @since 2025-07-13
 */
public interface EquipmentService {

    /**
     * Получает все оборудование с пагинацией.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с оборудованием в формате {@link PagedResponse}
     */
    PagedResponse<EquipmentResponse> getAll(int page, int size);

    /**
     * Получает оборудование по ID.
     *
     * @param id идентификатор оборудования
     * @return данные оборудования в формате {@link EquipmentResponse}
     */
    EquipmentResponse getById(Long id);

    /**
     * Получает оборудование по серийному номеру.
     *
     * @param serialNumber серийный номер оборудования
     * @return данные оборудования в формате {@link EquipmentResponse}
     */
    EquipmentResponse getBySerialNumber(String serialNumber);

    /**
     * Получает оборудование по статусу с пагинацией.
     *
     * @param statusId идентификатор статуса
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с оборудованием в формате {@link PagedResponse}
     */
    PagedResponse<EquipmentResponse> getByStatus(Long statusId, int page, int size);

    /**
     * Получает оборудование по типу с пагинацией.
     *
     * @param typeId идентификатор типа оборудования
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с оборудованием в формате {@link PagedResponse}
     */
    PagedResponse<EquipmentResponse> getByType(Long typeId, int page, int size);

    /**
     * Получает оборудование по имени создателя с пагинацией.
     *
     * @param username имя пользователя создателя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с оборудованием в формате {@link PagedResponse}
     */
    PagedResponse<EquipmentResponse> getByCreatedBy(String username, int page, int size);

    /**
     * Получает оборудование по ID создателя с пагинацией.
     *
     * @param userId идентификатор пользователя создателя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с оборудованием в формате {@link PagedResponse}
     */
    PagedResponse<EquipmentResponse> getByCreatedById(Long userId, int page, int size);

    /**
     * Создает новое оборудование.
     *
     * @param createRequest данные для создания оборудования
     * @param currentUser текущий аутентифицированный пользователь
     * @return созданное оборудование в формате {@link EquipmentResponse}
     */
    EquipmentResponse create(CreateEquipmentRequest createRequest, UserPrincipal currentUser);

    /**
     * Обновляет существующее оборудование.
     *
     * @param updateRequest данные для обновления оборудования
     * @param currentUser текущий аутентифицированный пользователь
     * @return обновленное оборудование в формате {@link EquipmentResponse}
     */
    EquipmentResponse update(UpdateEquipmentRequest updateRequest, UserPrincipal currentUser);

    /**
     * Удаляет оборудование.
     *
     * @param id идентификатор оборудования
     * @param currentUser текущий аутентифицированный пользователь
     */
    void delete(Long id, UserPrincipal currentUser);
}
