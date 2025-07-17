package com.suaistuds.monitoringequipment.payload.user;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO (Data Transfer Object) для краткого представления информации о пользователе.
 * Используется в случаях, когда необходимо передать только базовые данные о пользователе.
 *
 * @since 2025-07-13
 */
@Data
@AllArgsConstructor
public class UserSummary {

    /**
     * Уникальный идентификатор пользователя в системе
     */
    private Long id;

    /**
     * Логин пользователя
     * <p><b>Характеристики:</b>
     * <ul>
     *   <li>Уникальный в рамках системы</li>
     *   <li>Используется для идентификации пользователя</li>
     * </ul>
     */
    private String username;
}