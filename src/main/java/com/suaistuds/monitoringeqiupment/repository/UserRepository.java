package com.suaistuds.monitoringeqiupment.repository;

import com.suaistuds.monitoringeqiupment.exception.ResourceNotFoundException;
import com.suaistuds.monitoringeqiupment.model.directory.Role;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link User}.
 * Предоставляет методы для доступа и управления данными пользователей системы.
 *
 * <p>Наследует стандартные CRUD-операции от {@link JpaRepository<User, Long>}
 * и добавляет специализированные методы для работы с пользователями.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя (не может быть пустым)
     * @return {@link Optional}, содержащий пользователя если найден
     */
    Optional<User> findByUsername(@NotBlank String username);

    /**
     * Находит пользователя по email.
     *
     * @param email электронная почта пользователя (не может быть пустой)
     * @return {@link Optional}, содержащий пользователя если найден
     */
    Optional<User> findByEmail(@NotBlank String email);

    /**
     * Проверяет существование пользователя с указанным именем.
     *
     * @param username имя пользователя для проверки (не может быть пустым)
     * @return true если пользователь существует, false в противном случае
     */
    Boolean existsByUsername(@NotBlank String username);

    /**
     * Проверяет существование пользователя с указанным email.
     *
     * @param email электронная почта для проверки (не может быть пустой)
     * @return true если пользователь существует, false в противном случае
     */
    Boolean existsByEmail(@NotBlank String email);

    /**
     * Находит пользователя по имени пользователя или email.
     *
     * @param username имя пользователя для поиска
     * @param email электронная почта для поиска
     * @return {@link Optional}, содержащий пользователя если найден
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * Находит страницу пользователей по роли.
     *
     * @param role роль пользователей
     * @param pageable параметры пагинации
     * @return страница с пользователями ({@link Page})
     */
    Page<User> findByRole(Role role, Pageable pageable);

    /**
     * Получает пользователя по данным аутентифицированного пользователя.
     *
     * @param currentUser аутентифицированный пользователь ({@link UserPrincipal})
     * @return найденный пользователь
     * @throws ResourceNotFoundException если пользователь не найден
     */
    default User getUser(UserPrincipal currentUser) {
        return getUserByName(currentUser.getUsername());
    }

    /**
     * Получает пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return найденный пользователь
     * @throws ResourceNotFoundException если пользователь не найден
     */
    default User getUserByName(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}