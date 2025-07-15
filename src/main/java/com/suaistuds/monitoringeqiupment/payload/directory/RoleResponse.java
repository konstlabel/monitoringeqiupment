package com.suaistuds.monitoringeqiupment.payload.directory;

import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для представления информации о роли пользователя.
 * Используется для передачи данных о ролях между слоями приложения.
 *
 * <p>Класс поддерживает следующие возможности благодаря Lombok аннотациям:
 * <ul>
 *   <li>{@code @Data} - автоматически генерирует геттеры, сеттеры, toString(), equals() и hashCode()</li>
 *   <li>{@code @Builder} - предоставляет builder pattern для удобного создания объектов</li>
 *   <li>{@code @AllArgsConstructor} - создает конструктор со всеми полями</li>
 *   <li>{@code @NoArgsConstructor} - создает пустой конструктор</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {

    /**
     * Уникальный идентификатор роли в системе.
     * Используется для однозначной идентификации роли.
     */
    private Long id;

    /**
     * Название роли, представленное в виде перечисления {@link RoleName}.
     * Определяет уровень доступа и разрешения пользователя.
     */
    private RoleName name;
}