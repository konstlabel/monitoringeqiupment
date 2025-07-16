package com.suaistuds.monitoringeqiupment.payload.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO (Data Transfer Object) для представления профиля пользователя.
 * Содержит основные данные о пользователе системы.
 *
 * @since 2025-07-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {

    /**
     * Уникальный идентификатор пользователя
     */
    private Long id;

    /**
     * Логин пользователя
     * <p><b>Характеристики:</b>
     * <ul>
     *   <li>Уникален в рамках системы</li>
     *   <li>Используется для входа в систему</li>
     * </ul>
     */
    private String username;

    /**
     * Электронная почта пользователя
     * <p><b>Характеристики:</b>
     * <ul>
     *   <li>Уникальна в рамках системы</li>
     *   <li>Используется для уведомлений</li>
     * </ul>
     */
    private String email;

    /**
     * Идентификатор роли пользователя
     * <p><b>Характеристики:</b>
     * <ul>
     *   <li>Определяет уровень доступа</li>
     *   <li>Соответствует записи в справочнике ролей</li>
     * </ul>
     */
    private Long roleId;

    /**
     * Дата и время регистрации пользователя
     * <p><b>Формат:</b> Instant (UTC)
     */
    private Instant joinedAt;
}