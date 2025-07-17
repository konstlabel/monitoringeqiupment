package com.suaistuds.monitoringequipment.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Сервис для загрузки данных пользователей в контекст безопасности Spring.
 * Расширяет стандартный функционал UserDetailsService для поддержки:
 * <ul>
 *   <li>Аутентификации по username/email</li>
 *   <li>Загрузки пользователя по ID</li>
 * </ul>
 *
 * <p>Используется механизмами безопасности Spring для аутентификации и авторизации.
 *
 * @since 2025-07-13
 */
public interface CustomUserDetailsService {

    /**
     * Загружает пользователя по username или email.
     *
     * @param usernameOrEmail имя пользователя или email для поиска
     * @return UserDetails с данными пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException;

    /**
     * Загружает пользователя по ID.
     *
     * @param id идентификатор пользователя
     * @return UserDetails с данными пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    UserDetails loadUserById(Long id);
}