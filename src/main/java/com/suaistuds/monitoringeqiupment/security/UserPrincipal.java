package com.suaistuds.monitoringeqiupment.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.*;

/**
 * Реализация интерфейса {@link UserDetails} для хранения информации
 * об аутентифицированном пользователе в контексте безопасности Spring.
 *
 * <p>Содержит основные данные пользователя и его права доступа.
 * Используется системой безопасности Spring для аутентификации и авторизации.
 *
 * @since 2025-07-13
 */
@Getter
public class UserPrincipal implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Уникальный идентификатор пользователя
     */

    private Long id;

    /**
     * Логин пользователя
     */
    private String username;

    /**
     * Электронная почта пользователя (игнорируется при сериализации JSON)
     */
    @JsonIgnore
    private String email;

    /**
     * Зашифрованный пароль пользователя (игнорируется при сериализации JSON)
     */
    @JsonIgnore
    private String password;

    /**
     * Права доступа пользователя
     */
    private GrantedAuthority authority;

    /**
     * Создает новый экземпляр UserPrincipal.
     *
     * @param id идентификатор пользователя
     * @param username логин пользователя
     * @param email электронная почта
     * @param password зашифрованный пароль
     * @param authority права доступа
     */
    public UserPrincipal(Long id, String username, String email, String password, GrantedAuthority authority) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

    /**
     * Создает UserPrincipal на основе сущности User.
     *
     * @param user сущность пользователя
     * @return новый экземпляр UserPrincipal
     */
    public static UserPrincipal create(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(
                user.getRole().getName().name()
        );

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authority
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authority == null
                ? Collections.emptyList()
                : Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Сравнивает объекты UserPrincipal по идентификатору пользователя.
     *
     * @param object объект для сравнения
     * @return true если идентификаторы совпадают
     */
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        UserPrincipal that = (UserPrincipal) object;
        return Objects.equals(id, that.id);
    }

    /**
     * Возвращает хэш-код на основе идентификатора пользователя.
     *
     * @return хэш-код
     */
    public int hashCode() {
        return Objects.hash(id);
    }
}