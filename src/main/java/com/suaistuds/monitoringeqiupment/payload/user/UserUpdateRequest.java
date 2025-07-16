package com.suaistuds.monitoringeqiupment.payload.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) для запроса обновления данных пользователя.
 * Содержит поля, которые могут быть изменены в профиле пользователя.
 *
 * <p>Валидация полей:
 * <ul>
 *   <li>Все поля обязательны для заполнения</li>
 *   <li>Применяются проверки на длину и формат данных</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
public class UserUpdateRequest {

    /**
     * Новое имя пользователя
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotBlank})</li>
     *   <li>Длина от 3 до 30 символов ({@code @Size})</li>
     *   <li>Должно быть уникальным в системе</li>
     * </ul>
     */
    @NotBlank
    @Size(min = 3, max = 30)
    private String username;

    /**
     * Новая электронная почта пользователя
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotBlank})</li>
     *   <li>Максимальная длина 50 символов ({@code @Size})</li>
     *   <li>Должен соответствовать формату email ({@code @Email})</li>
     *   <li>Должен быть уникальным в системе</li>
     * </ul>
     */
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    /**
     * Новый пароль пользователя
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Обязательное поле ({@code @NotBlank})</li>
     *   <li>Длина от 6 до 31 символа ({@code @Size})</li>
     *   <li>Должен соответствовать требованиям сложности</li>
     * </ul>
     */
    @NotBlank
    @Size(min = 6, max = 31)
    private String password;
}
