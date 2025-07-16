package com.suaistuds.monitoringeqiupment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Конфигурационный класс для включения аудита JPA-сущностей.
 * Активирует автоматическое заполнение полей аудита (@CreatedBy, @LastModifiedBy и др.)
 * с использованием Spring Data JPA.
 *
 * <p>Настраивает интеграцию с Spring Security через auditorAwareRef,
 * что позволяет автоматически фиксировать пользователя, вносящего изменения.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>Включает автоматическое аудирование сущностей</li>
 *   <li>Связывает аудит с текущим аутентифицированным пользователем через springSecurityAuditAwareImpl</li>
 *   <li>Обеспечивает автоматическое заполнение полей:
 *     <ul>
 *       <li>@CreatedDate</li>
 *       <li>@LastModifiedDate</li>
 *       <li>@CreatedBy</li>
 *       <li>@LastModifiedBy</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditAwareImpl")
public class AuditingConfig {}
