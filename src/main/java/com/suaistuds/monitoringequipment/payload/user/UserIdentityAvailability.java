package com.suaistuds.monitoringequipment.payload.user;

import lombok.Builder;
import lombok.Data;

/**
 * DTO (Data Transfer Object) для проверки доступности идентификатора пользователя.
 * Используется для проверки уникальности имени пользователя или email при регистрации.
 *
 * @since 2025-07-13
 */
@Data
@Builder
public class UserIdentityAvailability {

    /**
     * Флаг доступности идентификатора
     * <p><b>Значения:</b>
     * <ul>
     *   <li>true - идентификатор доступен для использования</li>
     *   <li>false - идентификатор уже занят</li>
     * </ul>
     */
    private Boolean available;
}