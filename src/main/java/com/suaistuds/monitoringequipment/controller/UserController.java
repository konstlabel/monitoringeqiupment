package com.suaistuds.monitoringequipment.controller;

import com.suaistuds.monitoringequipment.payload.PagedResponse;
import com.suaistuds.monitoringequipment.payload.user.*;
import com.suaistuds.monitoringequipment.service.UserService;
import com.suaistuds.monitoringequipment.security.CurrentUser;
import com.suaistuds.monitoringequipment.security.UserPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления пользователями системы.
 *
 * <p>Предоставляет REST API для:
 * <ul>
 *   <li>CRUD-операций с пользователями</li>
 *   <li>Управления ролями пользователей</li>
 *   <li>Проверки доступности учетных данных</li>
 *   <li>Получения информации о текущем пользователе</li>
 *   <li>Фильтрации пользователей по различным параметрам</li>
 * </ul>
 *
 * <p>Основные особенности:
 * <ul>
 *   <li>Интеграция с системой аутентификации и авторизации</li>
 *   <li>Поддержка пагинации для методов получения списков</li>
 *   <li>Валидация входных параметров</li>
 *   <li>Разграничение прав доступа для операций</li>
 * </ul>
 *
 * @see UserService
 * @since 2025-07-13
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Получает список всех пользователей с пагинацией.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице (1-100)
     * @return страница с профилями пользователей {@link PagedResponse}
     */
    @GetMapping
    public PagedResponse<UserProfile> getAllUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size) {
        return userService.getAll(page, size);
    }

    /**
     * Получает краткую информацию о текущем аутентифицированном пользователе.
     *
     * @param currentUser текущий аутентифицированный пользователь
     * @return краткая информация о пользователе {@link UserSummary}
     */
    @GetMapping("/me")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return userService.getCurrentUser(currentUser);
    }

    /**
     * Получает профиль пользователя по ID.
     *
     * @param userId идентификатор пользователя
     * @return профиль пользователя {@link UserProfile}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     */
    @GetMapping("/id/{userId}")
    public UserProfile getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    /**
     * Получает профиль пользователя по имени пользователя.
     *
     * @param username имя пользователя (логин)
     * @return профиль пользователя {@link UserProfile}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     */
    @GetMapping("/username/{username}")
    public UserProfile getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    /**
     * Получает профиль пользователя по email.
     *
     * @param email адрес электронной почты
     * @return профиль пользователя {@link UserProfile}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     */
    @GetMapping("/email/{email}")
    public UserProfile getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    /**
     * Получает публичный профиль пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return профиль пользователя {@link UserProfile}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     */
    @GetMapping("/{username}/profile")
    public UserProfile getUserProfile(@PathVariable String username) {
        return userService.getUserProfile(username);
    }

    /**
     * Проверяет доступность имени пользователя.
     *
     * @param username имя пользователя для проверки
     * @return информация о доступности {@link UserIdentityAvailability}
     */
    @GetMapping("/check/username")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam String username) {
        return userService.checkUsernameAvailability(username);
    }

    /**
     * Проверяет доступность email.
     *
     * @param email email для проверки
     * @return информация о доступности {@link UserIdentityAvailability}
     */
    @GetMapping("/check/email")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam String email) {
        return userService.checkEmailAvailability(email);
    }

    /**
     * Создает нового пользователя.
     *
     * @param signUpRequest данные для регистрации
     * @return краткая информация о созданном пользователе {@link UserSummary} с HTTP статусом 201 (Created)
     * @throws com.suaistuds.monitoringequipment.exception.BadRequestException если имя пользователя или email уже заняты
     */
    @PostMapping
    public ResponseEntity<UserSummary> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        UserSummary userSummary = userService.create(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSummary);
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param username имя пользователя для обновления
     * @param updateRequest новые данные пользователя
     * @param currentUser текущий аутентифицированный пользователь
     * @return обновленная краткая информация о пользователе {@link UserSummary}
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если нет прав на обновление
     * @throws com.suaistuds.monitoringequipment.exception.BadRequestException если новые имя пользователя или email уже заняты
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     */
    @PutMapping("/{username}")
    public UserSummary updateUser(
            @PathVariable String username,
            @Valid @RequestBody UserUpdateRequest updateRequest,
            @CurrentUser UserPrincipal currentUser) {
        return userService.update(username, updateRequest, currentUser);
    }

    /**
     * Удаляет пользователя.
     *
     * @param username имя пользователя для удаления
     * @param currentUser текущий аутентифицированный пользователь
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если нет прав на удаление
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     * @responseStatus 204 No Content при успешном удалении
     */
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable String username,
            @CurrentUser UserPrincipal currentUser) {
        userService.delete(username, currentUser);
    }

    /**
     * Назначает пользователю роль администратора.
     *
     * @param username имя пользователя
     * @param currentUser текущий аутентифицированный пользователь
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если нет прав на изменение ролей
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     * @responseStatus 204 No Content при успешном выполнении
     */
    @PutMapping("/{username}/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void grantAdminRole(@PathVariable String username,
                               @CurrentUser UserPrincipal currentUser) {
        userService.giveAdmin(username, currentUser);
    }

    /**
     * Отзывает у пользователя роль администратора.
     *
     * @param username имя пользователя
     * @param currentUser текущий аутентифицированный пользователь
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если нет прав на изменение ролей
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     * @responseStatus 204 No Content при успешном выполнении
     */
    @DeleteMapping("/{username}/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeAdminRole(@PathVariable String username,
                                @CurrentUser UserPrincipal currentUser) {
        userService.removeAdmin(username, currentUser);
    }

    /**
     * Назначает пользователю роль студии.
     *
     * @param username имя пользователя
     * @param currentUser текущий аутентифицированный пользователь
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если нет прав на изменение ролей
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     * @responseStatus 204 No Content при успешном выполнении
     */
    @PutMapping("/{username}/studio")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void grantStudioRole(@PathVariable String username,
                                @CurrentUser UserPrincipal currentUser) {
        userService.giveStudio(username, currentUser);
    }

    /**
     * Отзывает у пользователя роль студии.
     *
     * @param username имя пользователя
     * @param currentUser текущий аутентифицированный пользователь
     * @throws com.suaistuds.monitoringequipment.exception.UnauthorizedException если нет прав на изменение ролей
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если пользователь не найден
     * @responseStatus 204 No Content при успешном выполнении
     */
    @DeleteMapping("/{username}/studio")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeStudioRole(@PathVariable String username,
                                 @CurrentUser UserPrincipal currentUser) {
        userService.removeStudio(username, currentUser);
    }

    /**
     * Получает пользователей по роли с пагинацией.
     *
     * @param roleId идентификатор роли
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @param currentUser текущий аутентифицированный пользователь
     * @return страница с профилями пользователей {@link PagedResponse}
     * @throws com.suaistuds.monitoringequipment.exception.ResourceNotFoundException если роль не найдена
     */
    @GetMapping("/role/{roleId}")
    public PagedResponse<UserProfile> getUsersByRole(
            @PathVariable Long roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @CurrentUser UserPrincipal currentUser) {
        return userService.getUsersByRole(roleId, page, size, currentUser);
    }
}