package com.suaistuds.monitoringeqiupment.service.impl;

import com.suaistuds.monitoringeqiupment.exception.*;
import com.suaistuds.monitoringeqiupment.model.directory.Role;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
import com.suaistuds.monitoringeqiupment.payload.user.*;
import com.suaistuds.monitoringeqiupment.repository.RoleRepository;
import com.suaistuds.monitoringeqiupment.repository.UserRepository;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import com.suaistuds.monitoringeqiupment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 5. UserServiceImpl

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername());
    }

    @Override
    public UserIdentityAvailability checkUsernameAvailability(String username) {
        boolean available = !userRepository.existsByUsername(username);
        return UserIdentityAvailability.builder().available(available).build();
    }

    @Override
    public UserIdentityAvailability checkEmailAvailability(String email) {
        boolean available = !userRepository.existsByEmail(email);
        return UserIdentityAvailability.builder().available(available).build();
    }

    @Override
    public UserProfile getUserProfile(String username) {
        User u = userRepository.getUserByName(username);
        return new UserProfile(u.getId(), u.getUsername(), u.getEmail(), u.getCreatedAt());
    }

    @Override
    @Transactional
    public UserSummary create(SignUpRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException("Email is already taken");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new AppException("User role not set"));
        u.setRole(role);
        User saved = userRepository.save(u);
        return new UserSummary(saved.getId(), saved.getUsername());
    }

    @Override
    @Transactional
    public UserSummary update(String username, UserUpdateRequest req, UserPrincipal currentUser) {
        User u = userRepository.getUserByName(username);
        boolean self = u.getId().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.ADMIN.name()));
        if (!self && !admin) {
            throw new UnauthorizedException("You don't have permission to update this user");
        }
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        User saved = userRepository.save(u);
        return new UserSummary(saved.getId(), saved.getUsername());
    }

    @Override
    @Transactional
    public void delete(String username, UserPrincipal currentUser) {
        User u = userRepository.getUserByName(username);
        boolean self = u.getId().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.ADMIN.name()));
        if (!self && !admin) {
            throw new UnauthorizedException("You don't have permission to delete this user");
        }
        userRepository.delete(u);
    }

    @Override
    @Transactional
    public void giveAdmin(String username) {
        User u = userRepository.getUserByName(username);
        Role admin = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new AppException("Admin role not set"));
        u.setRole(admin);
    }

    @Override
    @Transactional
    public void removeAdmin(String username) {
        User u = userRepository.getUserByName(username);
        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new AppException("User role not set"));
        u.setRole(userRole);
    }

    @Override
    @Transactional
    public void giveStudio(String username) {
        User u = userRepository.getUserByName(username);
        Role studio = roleRepository.findByName(RoleName.STUDIO)
                .orElseThrow(() -> new AppException("Studio role not set"));
        u.setRole(studio);
    }

    @Override
    @Transactional
    public void removeStudio(String username) {
        User u = userRepository.getUserByName(username);
        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new AppException("User role not set"));
        u.setRole(userRole);
    }
}
