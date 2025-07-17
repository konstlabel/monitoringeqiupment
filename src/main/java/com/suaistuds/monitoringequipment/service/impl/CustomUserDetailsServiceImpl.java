package com.suaistuds.monitoringequipment.service.impl;

import com.suaistuds.monitoringequipment.model.entity.User;
import com.suaistuds.monitoringequipment.repository.UserRepository;
import com.suaistuds.monitoringequipment.security.UserPrincipal;
import com.suaistuds.monitoringequipment.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса для загрузки данных пользователей Spring Security.
 *
 * <p>Совмещает функциональность:
 * <ul>
 *   <li>Стандартного {@link UserDetailsService} Spring Security</li>
 *   <li>Кастомного {@link CustomUserDetailsService} для загрузки по ID</li>
 * </ul>
 *
 * <p>Особенности реализации:
 * <ul>
 *   <li>Использует {@link UserRepository} для доступа к данным пользователей</li>
 *   <li>Все методы работают в режиме только для чтения ({@code @Transactional(readOnly = true)})</li>
 *   <li>Преобразует сущности {@link User} в {@link UserPrincipal} для Spring Security</li>
 * </ul>
 *
 * <p>Аннотации класса:
 * <ul>
 *   <li>{@code @Service} - помечает как Spring-сервис</li>
 *   <li>{@code @Transactional} - методы выполняются в транзакционном контексте</li>
 * </ul>
 *
 * @see UserDetailsService
 * @see CustomUserDetailsService
 * @since 2025-07-13
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {

    /**
     * Репозиторий для работы с пользователями.
     * Инжектируется Spring'ом через конструктор (не показан) или поле.
     */
    @Autowired private UserRepository userRepository;

    /**
     * Загружает данные пользователя по имени пользователя или email.
     *
     * @param usernameOrEmail имя пользователя или email для поиска
     * @return объект {@link UserDetails} (реализация {@link UserPrincipal})
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) {

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User not found with username or email: %s", usernameOrEmail)));

        return UserPrincipal.create(user);
    }

    /**
     * Загружает данные пользователя по ID.
     *
     * @param id идентификатор пользователя
     * @return объект {@link UserDetails} (реализация {@link UserPrincipal})
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User not found with id: %s", id)));

        return UserPrincipal.create(user);
    }
}
