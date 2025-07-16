package com.suaistuds.monitoringeqiupment.payload;

import lombok.Data;

/**
 * DTO (Data Transfer Object) для ответа с JWT токеном аутентификации.
 * Содержит токен доступа и информацию о его типе для использования в защищенных запросах.
 *
 * <p>Особенности:
 * <ul>
 *   <li>По умолчанию использует тип токена "Bearer"</li>
 *   <li>Предоставляет токен для авторизации последующих запросов</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
public class JwtAuthenticationResponse {

    /**
     * JWT токен доступа
     * <p><b>Характеристики:</b>
     * <ul>
     *   <li>Подписанный токен в формате JWT</li>
     *   <li>Содержит claims с информацией о пользователе</li>
     *   <li>Имеет ограниченное время действия</li>
     * </ul>
     */
    private String accessToken;

    /**
     * Тип токена
     * <p><b>Значение по умолчанию:</b> "Bearer"
     * <p><b>Использование:</b> Указывается в заголовке Authorization
     */
    private String tokenType = "Bearer";

    /**
     * Конструктор для создания ответа с JWT токеном
     * @param accessToken JWT токен доступа
     */
    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}