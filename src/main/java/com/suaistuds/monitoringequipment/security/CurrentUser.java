package com.suaistuds.monitoringequipment.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для удобного доступа к данным текущего аутентифицированного пользователя.
 *
 * <p>Используется для внедрения {@link UserPrincipal} в параметры методов контроллеров.
 * Является оберткой над стандартной аннотацией Spring Security {@link AuthenticationPrincipal}.
 *
 *
 * @see AuthenticationPrincipal
 * @see UserPrincipal
 * @since 2025-07-13
 */
@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}