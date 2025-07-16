package com.suaistuds.monitoringeqiupment.security;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Компонент обработки ошибок аутентификации JWT.
 * Реализует интерфейс {@link AuthenticationEntryPoint} для обработки неавторизованных запросов.
 *
 * <p>Отправляет HTTP-ответ 401 (UNAUTHORIZED) при неудачной аутентификации,
 * логируя при этом информацию об ошибке.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>Перехват исключений аутентификации</li>
 *   <li>Логирование ошибок аутентификации</li>
 *   <li>Отправка стандартизированного ответа клиенту</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    /**
     * Обрабатывает запрос при неудачной аутентификации.
     *
     * @param httpServletRequest HTTP-запрос
     * @param httpServletResponse HTTP-ответ
     * @param e исключение аутентификации
     * @throws IOException при ошибках ввода-вывода
     * @throws ServletException при ошибках сервлета
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @NotNull AuthenticationException e)
            throws IOException, ServletException {
        LOGGER.error("Responding with unauthorized error. Message - {}", e.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Sorry, You're not authorized to access this resource.");
    }
}