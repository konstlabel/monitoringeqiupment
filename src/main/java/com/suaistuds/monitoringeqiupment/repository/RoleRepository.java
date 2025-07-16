package com.suaistuds.monitoringeqiupment.repository;

import com.suaistuds.monitoringeqiupment.model.directory.Role;
import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Role}.
 * Предоставляет методы для доступа к данным о ролях пользователей.
 *
 * <p>Наследует стандартные CRUD-операции от {@link JpaRepository}
 * и добавляет специализированные методы для поиска ролей.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Находит роль по её наименованию.
     *
     * @param name наименование роли из перечисления {@link RoleName}
     * @return {@link Optional}, содержащий роль, если найдена
     */
    Optional<Role> findByName(RoleName name);
}
