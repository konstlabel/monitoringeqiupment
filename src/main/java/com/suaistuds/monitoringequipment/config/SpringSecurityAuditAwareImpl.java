package com.suaistuds.monitoringequipment.config;

import com.suaistuds.monitoringequipment.security.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Реализация интерфейса {@link AuditorAware} для интеграции Spring Security с JPA Auditing.
 * Определяет текущего пользователя для автоматического заполнения полей аудита (@CreatedBy, @LastModifiedBy).
 *
 * <p>Основные функции:
 * <ul>
 *   <li>Получает текущего аутентифицированного пользователя из контекста безопасности</li>
 *   <li>Игнорирует анонимных пользователей</li>
 *   <li>Возвращает идентификатор пользователя для JPA Auditing</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Component
public class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {

    /**
     * Возвращает идентификатор текущего аутентифицированного пользователя.
     *
     * @return {@link Optional} с идентификатором пользователя, если пользователь аутентифицирован,
     *         или пустой Optional для анонимных пользователей
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.of(((UserPrincipal) auth.getPrincipal()).getId());
    }
}
