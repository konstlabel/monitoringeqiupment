package com.suaistuds.monitoringequipment.service;

import com.suaistuds.monitoringequipment.payload.History.CreateHistoryRequest;
import com.suaistuds.monitoringequipment.payload.History.HistoryResponse;
import com.suaistuds.monitoringequipment.payload.History.UpdateHistoryRequest;
import com.suaistuds.monitoringequipment.payload.PagedResponse;
import com.suaistuds.monitoringequipment.security.UserPrincipal;

import java.time.LocalDateTime;

/**
 * Сервис для работы с историей изменений оборудования.
 * Предоставляет методы для управления записями истории и их фильтрации.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>CRUD-операции с записями истории</li>
 *   <li>Фильтрация по различным параметрам с пагинацией</li>
 *   <li>Поиск записей по временным периодам</li>
 *   <li>Работа с ответственными лицами и пользователями</li>
 * </ul>
 *
 * @since 2025-07-13
 */
public interface HistoryService {

    /**
     * Получает все записи истории с пагинацией.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с записями истории в формате {@link PagedResponse}
     */
    PagedResponse<HistoryResponse> getAll(int page, int size);

    /**
     * Получает запись истории по ID.
     *
     * @param id идентификатор записи истории
     * @return данные записи истории в формате {@link HistoryResponse}
     */
    HistoryResponse getById(Long id);

    /**
     * Получает записи истории по имени пользователя с пагинацией.
     *
     * @param username имя пользователя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории в формате {@link PagedResponse}
     */
    PagedResponse<HistoryResponse> getByUser(String username, int page, int size);

    /**
     * Получает записи истории по ID пользователя с пагинацией.
     *
     * @param userId идентификатор пользователя
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории в формате {@link PagedResponse}
     */
    PagedResponse<HistoryResponse> getByUserId(Long userId, int page, int size);

    /**
     * Получает записи истории по оборудованию с пагинацией.
     *
     * @param equipmentId идентификатор оборудования
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории в формате {@link PagedResponse}
     */
    PagedResponse<HistoryResponse> getByEquipment(Long equipmentId, int page, int size);

    /**
     * Получает записи истории по имени ответственного лица с пагинацией.
     *
     * @param username имя ответственного лица
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории в формате {@link PagedResponse}
     */
    PagedResponse<HistoryResponse> getByResponsible(String username, int page, int size);

    /**
     * Получает записи истории по ID ответственного лица с пагинацией.
     *
     * @param responsibleId идентификатор ответственного лица
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории в формате {@link PagedResponse}
     */
    PagedResponse<HistoryResponse> getByResponsibleId(Long responsibleId, int page, int size);

    /**
     * Получает записи истории по статусу с пагинацией.
     *
     * @param statusId идентификатор статуса
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории в формате {@link PagedResponse}
     */
    PagedResponse<HistoryResponse> getByStatus(Long statusId, int page, int size);

    /**
     * Получает записи истории по конкретной дате с пагинацией.
     *
     * @param date дата для фильтрации
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории в формате {@link PagedResponse}
     */
    PagedResponse<HistoryResponse> getByDate(LocalDateTime date, int page, int size);

    /**
     * Получает записи истории за период времени с пагинацией.
     *
     * @param startDate начальная дата периода
     * @param endDate конечная дата периода
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с записями истории в формате {@link PagedResponse}
     */
    PagedResponse<HistoryResponse> getByDateBetween(LocalDateTime startDate, LocalDateTime endDate, int page, int size);

    /**
     * Создает новую запись в истории.
     *
     * @param createRequest данные для создания записи
     * @param currentUser текущий аутентифицированный пользователь
     * @return созданная запись истории в формате {@link HistoryResponse}
     */
    HistoryResponse create(CreateHistoryRequest createRequest, UserPrincipal currentUser);

    /**
     * Обновляет существующую запись истории.
     *
     * @param updateRequest данные для обновления записи
     * @param currentUser текущий аутентифицированный пользователь
     * @return обновленная запись истории в формате {@link HistoryResponse}
     */
    HistoryResponse update(UpdateHistoryRequest updateRequest, UserPrincipal currentUser);

    /**
     * Удаляет запись истории.
     *
     * @param id идентификатор записи истории
     * @param currentUser текущий аутентифицированный пользователь
     */
    void delete(Long id, UserPrincipal currentUser);
}
