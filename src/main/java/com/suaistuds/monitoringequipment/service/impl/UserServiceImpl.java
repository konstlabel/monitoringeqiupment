package com.suaistuds.monitoringequipment.service.impl;

import com.suaistuds.monitoringequipment.exception.*;
import com.suaistuds.monitoringequipment.model.directory.Role;
import com.suaistuds.monitoringequipment.model.entity.User;
import com.suaistuds.monitoringequipment.model.enums.RoleName;
import com.suaistuds.monitoringequipment.payload.PagedResponse;
import com.suaistuds.monitoringequipment.payload.user.*;
import com.suaistuds.monitoringequipment.repository.RoleRepository;
import com.suaistuds.monitoringequipment.repository.UserRepository;
import com.suaistuds.monitoringequipment.security.UserPrincipal;
import com.suaistuds.monitoringequipment.service.UserService;
import com.suaistuds.monitoringequipment.util.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для управления пользователями системы.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>CRUD-операции с пользователями</li>
 *   <li>Управление ролями пользователей (администратор, студия, пользователь)</li>
 *   <li>Проверка доступности учетных данных</li>
 *   <li>Получение информации о текущем пользователе</li>
 *   <li>Пагинированный поиск пользователей</li>
 * </ul>
 *
 * <p>Особенности реализации:
 * <ul>
 *   <li>Использует Spring Data JPA репозитории ({@link UserRepository}, {@link RoleRepository})</li>
 *   <li>Шифрование паролей через {@link PasswordEncoder}</li>
 *   <li>Автоматическое преобразование сущностей в DTO через {@link ModelMapper}</li>
 *   <li>Транзакционность операций изменения данных ({@code @Transactional})</li>
 * </ul>
 *
 * <p>Обрабатываемые исключения:
 * <ul>
 *   <li>{@link ResourceNotFoundException} - если ресурс не найден</li>
 *   <li>{@link BadRequestException} - при попытке использовать занятые email/username</li>
 *   <li>{@link UnauthorizedException} - при отсутствии прав на операцию</li>
 *   <li>{@link AppException} - при проблемах с настройками ролей</li>
 * </ul>
 *
 * @see UserService
 * @since 2025-07-13
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    /**
     * Репозиторий для работы с пользователями.
     */
    @Autowired private UserRepository userRepository;

    /**
     * Репозиторий для работы с ролями пользователей.
     */
    @Autowired private RoleRepository roleRepository;

    /**
     * Кодировщик паролей.
     */
    @Autowired private PasswordEncoder passwordEncoder;

    /**
     * Маппер для преобразования сущностей в DTO.
     */
    @Autowired private ModelMapper modelMapper;

    /**
     * Получает всех пользователей с пагинацией.
     *
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @return страница с профилями пользователей {@link PagedResponse}
     */
    @Override
    public PagedResponse<UserProfile> getAll(int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> p = userRepository.findAll(pg);
        List<UserProfile> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    /**
     * Получает пользователя по ID.
     *
     * @param id идентификатор пользователя
     * @return профиль пользователя {@link UserProfile}
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Override
    public UserProfile getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return toDto(user);
    }

    /**
     * Получает пользователя по имени пользователя.
     *
     * @param username имя пользователя (логин)
     * @return профиль пользователя {@link UserProfile}
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Override
    public UserProfile getUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return toDto(user);

    }

    /**
     * Получает пользователя по email.
     *
     * @param email адрес электронной почты
     * @return профиль пользователя {@link UserProfile}
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Override
    public UserProfile getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return toDto(user);
    }

    /**
     * Получает краткую информацию о текущем аутентифицированном пользователе.
     *
     * @param currentUser текущий аутентифицированный пользователь
     * @return краткая информация {@link UserSummary}
     * @throws ResourceNotFoundException если currentUser равен null
     */
    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {

        if (currentUser == null) {
            throw new ResourceNotFoundException("User", "currentUser", null);
        }
        return new UserSummary(currentUser.getId(), currentUser.getUsername());

    }

    /**
     * Получает пользователей по роли с пагинацией.
     *
     * @param roleId идентификатор роли
     * @param page номер страницы
     * @param size количество элементов на странице
     * @return страница с профилями пользователей {@link PagedResponse}
     * @throws ResourceNotFoundException если роль не найдена
     */
    @Override
    public PagedResponse<UserProfile> getUsersByRole(Long roleId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> p = userRepository.findByRole(role, pg);
        List<UserProfile> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    /**
     * Проверяет существование пользователя с указанным именем.
     *
     * @param username имя пользователя для проверки
     * @return true если пользователь существует, иначе false
     */
    @Override
    public boolean existsByUsername(String username) {

        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    /**
     * Проверяет существование пользователя с указанным email.
     *
     * @param email email для проверки
     * @return true если пользователь существует, иначе false
     */
    @Override
    public boolean existsByEmail(String email) {

        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();

    }

    /**
     * Проверяет доступность имени пользователя.
     *
     * @param username имя пользователя для проверки
     * @return объект с информацией о доступности {@link UserIdentityAvailability}
     */
    @Override
    public UserIdentityAvailability checkUsernameAvailability(String username) {

        boolean available = !userRepository.existsByUsername(username);
        return UserIdentityAvailability.builder().available(available).build();
    }

    /**
     * Проверяет доступность email.
     *
     * @param email email для проверки
     * @return объект с информацией о доступности {@link UserIdentityAvailability}
     */
    @Override
    public UserIdentityAvailability checkEmailAvailability(String email) {

        boolean available = !userRepository.existsByEmail(email);
        return UserIdentityAvailability.builder().available(available).build();
    }

    /**
     * Получает профиль пользователя по имени.
     *
     * @param username имя пользователя
     * @return профиль пользователя {@link UserProfile}
     */
    @Override
    public UserProfile getUserProfile(String username) {

        User user = userRepository.getUserByName(username);
        return toDto(user);
    }

    /**
     * Создает нового пользователя.
     *
     * @param signUpRequest данные для регистрации
     * @return краткая информация о созданном пользователе {@link UserSummary}
     * @throws BadRequestException если имя пользователя или email уже заняты
     * @throws AppException если роль пользователя не настроена в системе
     */
    @Override
    @Transactional
    public UserSummary create(SignUpRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email is already taken");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        Role role = roleRepository.findByName(RoleName.user)
                .orElseThrow(() -> new AppException("User role not set"));
        user.setRole(role);
        User saved = userRepository.save(user);
        return new UserSummary(saved.getId(), saved.getUsername());
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param username имя пользователя для обновления
     * @param updateRequest новые данные пользователя
     * @param currentUser текущий аутентифицированный пользователь
     * @return обновленная краткая информация {@link UserSummary}
     * @throws UnauthorizedException если нет прав на обновление
     * @throws BadRequestException если новые имя пользователя или email уже заняты
     */
    @Override
    @Transactional
    public UserSummary update(String username, UserUpdateRequest updateRequest, UserPrincipal currentUser) {

        User user = userRepository.getUserByName(username);

        boolean self = user.getId().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));

        if (!self && !admin) {
            throw new UnauthorizedException("You don't have permission to update this user");
        }

        if (userRepository.existsByUsername(updateRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(updateRequest.getEmail())) {
            throw new BadRequestException("Email is already taken");
        }

        user.setUsername(updateRequest.getUsername());
        user.setEmail(updateRequest.getEmail());
        user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        User saved = userRepository.save(user);
        return new UserSummary(saved.getId(), saved.getUsername());
    }

    /**
     * Удаляет пользователя.
     *
     * @param username имя пользователя для удаления
     * @param currentUser текущий аутентифицированный пользователь
     * @throws UnauthorizedException если нет прав на удаление
     */
    @Override
    @Transactional
    public void delete(String username, UserPrincipal currentUser) {

        User user = userRepository.getUserByName(username);

        boolean self = user.getId().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));

        if (!self && !admin) {
            throw new UnauthorizedException("You don't have permission to delete this user");
        }

        userRepository.delete(user);
    }

    /**
     * Назначает пользователю права администратора.
     *
     * @param username имя пользователя
     * @throws AppException если роль администратора не настроена в системе
     */
    @Override
    @PreAuthorize("hasAnyRole('admin')")
    @Transactional
    public void giveAdmin(String username) {

        User u = userRepository.getUserByName(username);
        Role admin = roleRepository.findByName(RoleName.admin)
                .orElseThrow(() -> new AppException("Admin role not set"));
        u.setRole(admin);
    }

    /**
     * Отзывает права администратора у пользователя.
     *
     * @param username имя пользователя
     * @throws AppException если роль пользователя не настроена в системе
     */
    @Override
    @PreAuthorize("hasAnyRole('admin')")
    @Transactional
    public void removeAdmin(String username) {

        User u = userRepository.getUserByName(username);
        Role userRole = roleRepository.findByName(RoleName.user)
                .orElseThrow(() -> new AppException("User role not set"));
        u.setRole(userRole);
    }

    /**
     * Назначает пользователю права студии.
     *
     * @param username имя пользователя
     * @throws AppException если роль студии не настроена в системе
     */
    @Override
    @PreAuthorize("hasAnyRole('admin')")
    @Transactional
    public void giveStudio(String username) {

        User u = userRepository.getUserByName(username);
        Role studio = roleRepository.findByName(RoleName.studio)
                .orElseThrow(() -> new AppException("Studio role not set"));
        u.setRole(studio);
    }

    /**
     * Отзывает права студии у пользователя.
     *
     * @param username имя пользователя
     * @throws AppException если роль пользователя не настроена в системе
     */
    @Override
    @PreAuthorize("hasAnyRole('admin')")
    @Transactional
    public void removeStudio(String username) {

        User u = userRepository.getUserByName(username);
        Role userRole = roleRepository.findByName(RoleName.user)
                .orElseThrow(() -> new AppException("User role not set"));
        u.setRole(userRole);
    }

    /**
     * Преобразует страницу пользователей в PagedResponse.
     *
     * @param page страница с пользователями
     * @return страница с DTO {@link PagedResponse}
     */
    private PagedResponse<UserProfile> toPagedResponse(Page<User> page) {
        List<UserProfile> dtos = page.getContent().stream()
                .map(this::toDto)
                .toList();
        return new PagedResponse<>(dtos, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    /**
     * Преобразует сущность User в DTO профиля.
     *
     * @param user сущность пользователя
     * @return DTO профиля {@link UserProfile}
     */
    private UserProfile toDto(User user) {
        UserProfile dto = modelMapper.map(user, UserProfile.class);
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoleId(user.getRole().getId());
        dto.setId(user.getId());

        return dto;
    }
}
