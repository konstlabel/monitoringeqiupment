package com.suaistuds.monitoringequipment.config;

import com.suaistuds.monitoringequipment.model.directory.Role;
import com.suaistuds.monitoringequipment.model.entity.User;
import com.suaistuds.monitoringequipment.model.enums.RoleName;
import com.suaistuds.monitoringequipment.repository.RoleRepository;
import com.suaistuds.monitoringequipment.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;

/**
 * Конфигурационный класс для инициализации базовых данных при запуске приложения.
 * Создает системные роли и администратора по умолчанию, если они отсутствуют.
 *
 * @since 2025-07-13
 */
@Configuration
public class DataInitializer {

    /**
     * Инициализирует базовые данные при запуске приложения:
     * <ol>
     *   <li>Создает все системные роли из перечисления {@link RoleName}, если они отсутствуют</li>
     *   <li>Создает администратора по умолчанию (логин: admin, пароль: admin123), если он отсутствует</li>
     * </ol>
     *
     * @param roleRepo репозиторий для работы с ролями
     * @param userRepo репозиторий для работы с пользователями
     * @param encoder кодировщик паролей
     * @return CommandLineRunner для выполнения инициализации
     *
     * @implNote При первом запуске выводит в консоль учетные данные администратора.
     *           В продакшн-среде следует изменить пароль по умолчанию!
     */
    @Bean
    public CommandLineRunner initDefaultAdmin(
            RoleRepository roleRepo,
            UserRepository userRepo,
            PasswordEncoder encoder
    ) {
        return args -> {
            // 1) Создаём все справочные роли, если их ещё нет
            for (RoleName rn : EnumSet.allOf(RoleName.class)) {
                roleRepo.findByName(rn)
                        .orElseGet(() -> {
                            Role role = new Role();
                            role.setName(rn);
                            return roleRepo.save(role);
                        });
            }

            // 2) Если админа нет — создаём
            String adminUsername = "admin";
            if (userRepo.findByUsername(adminUsername).isEmpty()) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setEmail("admin@local");              // можно любой e‑mail
                admin.setPassword(encoder.encode("admin123"));// выберите свой пароль
                // вытаскиваем роль ADMIN и присваиваем пользователю
                Role adminRole = roleRepo.findByName(RoleName.admin)
                        .orElseThrow();
                admin.setRole(adminRole);
                userRepo.save(admin);
                System.out.println(
                        ">>> Default ADMIN created: username='admin', password='admin123'"
                );
            }
        };
    }
}
