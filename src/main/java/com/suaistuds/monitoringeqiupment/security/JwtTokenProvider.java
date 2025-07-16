package com.suaistuds.monitoringeqiupment.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Компонент для работы с JWT токенами.
 * Обеспечивает генерацию, валидацию и парсинг JWT токенов.
 *
 * <p>Использует библиотеку JJWT для работы с токенами.
 * Конфигурируется через параметры приложения:
 * <ul>
 *   <li>app.jwtSecret - секретный ключ в Base64</li>
 *   <li>app.jwtExpirationInMs - срок действия токена в миллисекундах</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Component
public class JwtTokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecretBase64;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    private Key key;

    /**
     * Инициализирует компонент после создания бина.
     * Декодирует секретный ключ из Base64 и создает Key для подписи.
     */
    @PostConstruct
    public void init() {
        byte[] secretBytes = Decoders.BASE64.decode(jwtSecretBase64);
        this.key = Keys.hmacShaKeyFor(secretBytes);
    }

    /**
     * Генерирует JWT токен на основе аутентификации пользователя.
     *
     * @param authentication данные аутентификации пользователя
     * @return сгенерированный JWT токен
     * @throws ClassCastException если principal не является UserPrincipal
     */
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    /**
     * Извлекает идентификатор пользователя из JWT токена.
     *
     * @param token JWT токен
     * @return идентификатор пользователя
     * @throws JwtException если токен невалиден
     */
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());
    }

    /**
     * Проверяет валидность JWT токена.
     *
     * @param token JWT токен для проверки
     * @return true если токен валиден, false в противном случае
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            LOGGER.error("JWT validation error: {}", ex.getMessage(), ex);
            return false;
        }
    }
}
