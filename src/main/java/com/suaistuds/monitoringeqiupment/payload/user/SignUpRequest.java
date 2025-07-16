package com.suaistuds.monitoringeqiupment.payload.user;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * DTO (Data Transfer Object) для запроса регистрации нового пользователя.
 * Содержит данные, необходимые для создания учетной записи пользователя.
 * Все поля проходят валидацию перед обработкой на сервере.
 *
 * @since 2025-07-13
 */
@Data
public class SignUpRequest {

    /**
     * Имя пользователя для входа в систему.
     * Должно быть уникальным в рамках системы.
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Не может быть пустым ({@code @NotBlank})</li>
     *   <li>Длина от 3 до 30 символов ({@code @Size})</li>
     * </ul>
     */
    @NotBlank
    @Size(min = 3, max = 30)
    private String username;

    /**
     * Электронная почта пользователя.
     * Используется для связи и восстановления доступа.
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Не может быть пустым ({@code @NotBlank})</li>
     *   <li>Максимальная длина 50 символов ({@code @Size})</li>
     *   <li>Должен соответствовать формату email ({@code @Email})</li>
     * </ul>
     */
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    /**
     * Пароль пользователя.
     * Хранится в системе в зашифрованном виде.
     * <p><b>Валидация:</b>
     * <ul>
     *   <li>Не может быть пустым ({@code @NotBlank})</li>
     *   <li>Длина от 6 до 31 символа ({@code @Size})</li>
     * </ul>
     * <p><b>Безопасность:</b>
     * <ul>
     *   <li>Передается только через защищенные соединения</li>
     *   <li>Не должен сохраняться в открытом виде</li>
     * </ul>
     */
    @NotBlank
    @Size(min = 6, max = 31)
    private String password;
}