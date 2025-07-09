package com.suaistuds.monitoringeqiupment.config;

import com.suaistuds.monitoringeqiupment.model.directory.Role;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
import com.suaistuds.monitoringeqiupment.repository.RoleRepository;
import com.suaistuds.monitoringeqiupment.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;

@Configuration
public class DataInitializer {

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
