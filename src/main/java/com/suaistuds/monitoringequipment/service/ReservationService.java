package com.suaistuds.monitoringequipment.service;

import com.suaistuds.monitoringequipment.payload.PagedResponse;
import com.suaistuds.monitoringequipment.payload.reservation.CreateReservationRequest;
import com.suaistuds.monitoringequipment.payload.reservation.ReservationResponse;
import com.suaistuds.monitoringequipment.payload.reservation.UpdateReservationRequest;
import com.suaistuds.monitoringequipment.security.UserPrincipal;

import java.time.LocalDateTime;

/**
 * Сервис для работы с резервированием оборудования.
 * Предоставляет методы для управления бронированиями и их фильтрации.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>CRUD-операции с бронированиями</li>
 *   <li>Фильтрация бронирований по различным параметрам с пагинацией</li>
 *   <li>Проверка доступности оборудования</li>
 *   <li>Получение текущих и будущих бронирований</li>
 *   <li>Работа с периодами бронирования</li>
 * </ul>
 *
 * @since 2025-07-13
 */
public interface ReservationService {

    /**
     * Получает все бронирования с пагинацией.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с бронированиями в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getAll(int page, int size);

    /**
     * Получает бронирование по ID.
     *
     * @param id идентификатор бронирования
     * @return данные бронирования в формате {@link ReservationResponse}
     */
    ReservationResponse getById(Long id);

    /**
     * Получает бронирования для конкретного оборудования с пагинацией.
     *
     * @param equipmentId идентификатор оборудования
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с бронированиями в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getByEquipment(Long equipmentId, int page, int size);

    /**
     * Получает бронирования по имени пользователя с пагинацией.
     *
     * @param username имя пользователя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с бронированиями в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getByUser(String username, int page, int size);

    /**
     * Получает бронирования по ID пользователя с пагинацией.
     *
     * @param userId идентификатор пользователя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с бронированиями в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getByUserId(Long userId, int page, int size);

    /**
     * Получает бронирования по имени ответственного лица с пагинацией.
     *
     * @param username имя ответственного лица
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с бронированиями в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getByResponsible(String username, int page, int size);

    /**
     * Получает бронирования по ID ответственного лица с пагинацией.
     *
     * @param responsibleId идентификатор ответственного лица
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с бронированиями в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getByResponsibleId(Long responsibleId, int page, int size);

    /**
     * Получает бронирования по статусу с пагинацией.
     *
     * @param statusId идентификатор статуса
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с бронированиями в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getByStatus(Long statusId, int page, int size);

    /**
     * Получает бронирования по дате начала с пагинацией.
     *
     * @param startDate дата начала бронирования
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с бронированиями в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getByStartDate(LocalDateTime startDate, int page, int size);

    /**
     * Получает бронирования по дате окончания с пагинацией.
     *
     * @param endDate дата окончания бронирования
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с бронированиями в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getByEndDate(LocalDateTime endDate, int page, int size);

    /**
     * Получает бронирования, у которых дата начала попадает в указанный период, с пагинацией.
     *
     * @param from начальная дата периода
     * @param to конечная дата периода
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с бронированиями в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getByStartDateBetween(LocalDateTime from, LocalDateTime to, int page, int size);

    /**
     * Получает бронирования, у которых дата окончания попадает в указанный период, с пагинацией.
     *
     * @param from начальная дата периода
     * @param to конечная дата периода
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с бронированиями в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getByEndDateBetween(LocalDateTime from, LocalDateTime to, int page, int size);

    /**
     * Получает текущие активные бронирования на указанную дату с пагинацией.
     *
     * @param date дата для проверки активности бронирований
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с активными бронированиями в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getCurrentReservations(LocalDateTime date, int page, int size);

    /**
     * Проверяет доступность оборудования в указанный период с пагинацией.
     *
     * @param equipmentId идентификатор оборудования
     * @param start начальная дата периода
     * @param end конечная дата периода
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с информацией о доступности в формате {@link PagedResponse}
     */
    PagedResponse<ReservationResponse> getEquipmentAvailability(Long equipmentId, LocalDateTime start, LocalDateTime end, int page, int size);

    /**
     * Создает новое бронирование оборудования.
     *
     * @param createRequest данные для создания бронирования
     * @param currentUser текущий аутентифицированный пользователь
     * @return созданное бронирование в формате {@link ReservationResponse}
     */
    ReservationResponse create(CreateReservationRequest createRequest, UserPrincipal currentUser);

    /**
     * Обновляет существующее бронирование.
     *
     * @param updateRequest данные для обновления бронирования
     * @param currentUser текущий аутентифицированный пользователь
     * @return обновленное бронирование в формате {@link ReservationResponse}
     */
    ReservationResponse update(UpdateReservationRequest updateRequest, UserPrincipal currentUser);

    /**
     * Удаляет бронирование.
     *
     * @param id идентификатор бронирования
     * @param currentUser текущий аутентифицированный пользователь
     */
    void delete(Long id, UserPrincipal currentUser);

}
