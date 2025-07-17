package com.suaistuds.monitoringequipment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурация CORS (Cross-Origin Resource Sharing) для веб-слоя приложения.
 * Определяет политики доступа к API с других доменов.
 *
 * <p>Основные настройки:
 * <ul>
 *   <li>Разрешенные источники (allowedOrigins) - настраиваются через свойство ${cors.allowedOrigins}</li>
 *   <li>Разрешенные HTTP методы - все (*)</li>
 *   <li>Разрешенные заголовки - все (*)</li>
 *   <li>Максимальное время кеширования CORS-префлайт запросов - 3600 секунд (1 час)</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${cors.allowedOrigins:*}")
    private String[] allowedOrigins;

    /**
     * Настраивает политику CORS для всех эндпоинтов приложения.
     *
     * @param registry объект для регистрации CORS-конфигураций
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
