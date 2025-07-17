package com.suaistuds.monitoringequipment.config;

import com.suaistuds.monitoringequipment.security.JwtAuthenticationEntryPoint;
import com.suaistuds.monitoringequipment.security.JwtAuthenticationFilter;
import com.suaistuds.monitoringequipment.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * Основная конфигурация безопасности Spring Security.
 * Настраивает аутентификацию и авторизацию для REST API.
 *
 * <p>Ключевые особенности конфигурации:
 * <ul>
 *   <li>JWT-аутентификация через {@link JwtAuthenticationFilter}</li>
 *   <li>Статусные сессии (STATELESS)</li>
 *   <li>Отключение CSRF защиты для REST API</li>
 *   <li>Настройка CORS</li>
 *   <li>Шифрование паролей с помощью BCrypt</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final CustomUserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtFilter;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param userDetailsService сервис для загрузки данных пользователя
     * @param unauthorizedHandler обработчик ошибок аутентификации
     * @param jwtFilter JWT фильтр аутентификации
     */
    @Autowired
    public SecurityConfig(
            CustomUserDetailsServiceImpl userDetailsService,
            JwtAuthenticationEntryPoint unauthorizedHandler,
            JwtAuthenticationFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler  = unauthorizedHandler;
        this.jwtFilter            = jwtFilter;
    }

    /**
     * Конфигурация цепочки фильтров безопасности.
     *
     * @param http объект конфигурации HTTP безопасности
     * @return сконфигурированная цепочка фильтров
     * @throws Exception в случае ошибки конфигурации
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // переключаемся на старый Ant‑matcher
        http.setSharedObject(PathMatcher.class, new AntPathMatcher());

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/users/check/username",
                                "/api/users/check/email"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/users/*/profile",
                                "/api/users/username/*",
                                "/api/users/email/*",
                                "/api/users/id/*"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Создает бин AuthenticationManager для обработки аутентификации.
     *
     * @param config конфигурация аутентификации
     * @return менеджер аутентификации
     * @throws Exception в случае ошибки создания
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Создает кодировщик паролей BCrypt.
     *
     * @return реализация PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}