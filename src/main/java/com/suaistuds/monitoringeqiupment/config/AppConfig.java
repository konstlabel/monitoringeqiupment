package com.suaistuds.monitoringeqiupment.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Основной класс конфигурации приложения.
 * Содержит определения Spring-бинов для общего использования в приложении.
 *
 * <p>Используется для централизованного конфигурирования общих компонентов,
 * которые требуются в разных частях приложения.
 *
 * @since 2025-07-13
 */
@Configuration
public class AppConfig {

    /**
     * Создает и настраивает экземпляр {@link ModelMapper} для маппинга DTO и сущностей.
     *
     * <p>ModelMapper предоставляет автоматическое преобразование между объектами разных типов,
     * что упрощает конвертацию между:
     * <ul>
     *   <li>Сущностями и DTO</li>
     *   <li>Разными типами моделей</li>
     *   <li>Версиями объектов</li>
     * </ul>
     *
     * @return новый экземпляр ModelMapper с настройками по умолчанию
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
