package com.suaistuds.monitoringequipment.service;

import com.suaistuds.monitoringequipment.payload.PagedResponse;
import com.suaistuds.monitoringequipment.payload.user.*;
import com.suaistuds.monitoringequipment.security.UserPrincipal;

/**
 * Сервис для работы с пользователями системы.
 * Предоставляет методы для управления учетными записями пользователей и их ролями.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>CRUD-операции с пользователями</li>
 *   <li>Управление ролями и привилегиями</li>
 *   <li>Проверка доступности учетных данных</li>
 *   <li>Получение информации о текущем пользователе</li>
 *   <li>Фильтрация пользователей по различным критериям</li>
 * </ul>
 *
 * @since 2025-07-13
 */
public interface UserService {

    /**
     * Получает всех пользователей с пагинацией.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с профилями пользователей в формате {@link PagedResponse}
     */
    PagedResponse<UserProfile> getAll(int page, int size);

    /**
     * Получает пользователя по ID.
     *
     * @param id идентификатор пользователя
     * @return профиль пользователя в формате {@link UserProfile}
     */
    UserProfile getUserById(Long id);

    /**
     * Получает пользователя по имени пользователя.
     *
     * @param username имя пользователя (логин)
     * @return профиль пользователя в формате {@link UserProfile}
     */
    UserProfile getUserByUsername(String username);

    /**
     * Получает пользователя по email.
     *
     * @param email адрес электронной почты
     * @return профиль пользователя в формате {@link UserProfile}
     */
    UserProfile getUserByEmail(String email);

    /**
     * Получает краткую информацию о текущем аутентифицированном пользователе.
     *
     * @param currentUser текущий аутентифицированный пользователь
     * @return краткая информация о пользователе в формате {@link UserSummary}
     */
    UserSummary getCurrentUser(UserPrincipal currentUser);

    /**
     * Получает пользователей по роли с пагинацией.
     *
     * @param roleId идентификатор роли
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с профилями пользователей в формате {@link PagedResponse}
     */
    PagedResponse<UserProfile> getUsersByRole(Long roleId, int page, int size);

    /**
     * Проверяет существование пользователя с указанным именем.
     *
     * @param username имя пользователя для проверки
     * @return true если пользователь существует, false в противном случае
     */
    boolean existsByUsername(String username);

    /**
     * Проверяет существование пользователя с указанным email.
     *
     * @param email email для проверки
     * @return true если пользователь с таким email существует, false в противном случае
     */
    boolean existsByEmail(String email);

    /**
     * Проверяет доступность имени пользователя.
     *
     * @param username имя пользователя для проверки
     * @return объект с информацией о доступности в формате {@link UserIdentityAvailability}
     */
    UserIdentityAvailability checkUsernameAvailability(String username);

    /**
     * Проверяет доступность email.
     *
     * @param email email для проверки
     * @return объект с информацией о доступности в формате {@link UserIdentityAvailability}
     */
    UserIdentityAvailability checkEmailAvailability(String email);

    /**
     * Получает профиль пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return профиль пользователя в формате {@link UserProfile}
     */
    UserProfile getUserProfile(String username);

    /**
     * Создает нового пользователя.
     *
     * @param signUpRequest данные для регистрации нового пользователя
     * @return краткая информация о созданном пользователе в формате {@link UserSummary}
     */
    UserSummary create(SignUpRequest signUpRequest);

    /**
     * Обновляет данные пользователя.
     *
     * @param username имя пользователя для обновления
     * @param updateRequest данные для обновления
     * @param currentUser текущий аутентифицированный пользователь
     * @return краткая информация об обновленном пользователе в формате {@link UserSummary}
     */
    UserSummary update(String username, UserUpdateRequest updateRequest, UserPrincipal currentUser);

    /**
     * Удаляет пользователя.
     *
     * @param username имя пользователя для удаления
     * @param currentUser текущий аутентифицированный пользователь
     */
    void delete(String username, UserPrincipal currentUser);

    /**
     * Назначает пользователю права администратора.
     *
     * @param username имя пользователя
     */
    void giveAdmin(String username);

    /**
     * Отзывает права администратора у пользователя.
     *
     * @param username имя пользователя
     */
    void removeAdmin(String username);

    /**
     * Назначает пользователю права студии.
     *
     * @param username имя пользователя
     */
    void giveStudio(String username);

    /**
     * Отзывает права студии у пользователя.
     *
     * @param username имя пользователя
     */
    void removeStudio(String username);
}