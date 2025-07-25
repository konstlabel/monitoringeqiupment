package com.suaistuds.monitoringequipment.security;

import com.suaistuds.monitoringequipment.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр аутентификации JWT, обрабатывающий каждый входящий HTTP-запрос.
 * Наследует {@link OncePerRequestFilter} для однократного выполнения на запрос.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>Извлечение JWT токена из заголовка Authorization</li>
 *   <li>Валидация токена с помощью {@link JwtTokenProvider}</li>
 *   <li>Загрузка данных пользователя через {@link CustomUserDetailsService}</li>
 *   <li>Установка аутентификации в контексте безопасности Spring</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * Основной метод фильтрации, выполняемый для каждого запроса.
     *
     * @param request HTTP-запрос
     * @param response HTTP-ответ
     * @param filterChain цепочка фильтров
     * @throws ServletException при ошибках сервлета
     * @throws IOException при ошибках ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception ex) {
            LOGGER.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Проверяет, следует ли пропустить фильтрацию для данного запроса.
     *
     * @param request HTTP-запрос
     * @return true, если фильтр не должен применяться, false в противном случае
     * @throws ServletException при ошибках сервлета
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Пропускаем публичные эндпоинты
        return pathMatcher.match("/api/auth/**", path) ||
                (pathMatcher.match("/api/users/check/username", path) && "GET".equalsIgnoreCase(method)) ||
                (pathMatcher.match("/api/users/check/email", path) && "GET".equalsIgnoreCase(method)) ||
                pathMatcher.match("/api/users/*/profile", path) ||
                pathMatcher.match("/api/users/username/*", path) ||
                pathMatcher.match("/api/users/email/*", path) ||
                pathMatcher.match("/api/users/id/*", path);
    }

    /**
     * Извлекает JWT токен из заголовка Authorization запроса.
     *
     * @param request HTTP-запрос
     * @return JWT токен или null, если токен отсутствует или невалиден
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}