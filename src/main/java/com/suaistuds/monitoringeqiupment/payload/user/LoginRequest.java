package com.suaistuds.monitoringeqiupment.payload.user;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO (Data Transfer Object) для запроса аутентификации пользователя.
 * Содержит учетные данные, необходимые для входа в систему.
 *
 * <p>Валидация полей:
 * <ul>
 *   <li>Все поля обязательны для заполнения ({@code @NotBlank})</li>
 *   <li>Проверка на пустые строки или пробелы</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
public class LoginRequest {

    /**
     * Логин или email пользователя
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotBlank})</li>
     *   <li>Должно соответствовать существующему пользователю</li>
     * </ul>
     */
    @NotBlank
    private String usernameOrEmail;

    /**
     * Пароль пользователя
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotBlank})</li>
     *   <li>Минимальная сложность проверяется на сервере</li>
     * </ul>
     */
    @NotBlank
    private String password;
}