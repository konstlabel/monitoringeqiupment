package com.suaistuds.monitoringeqiupment.service.impl;

import com.suaistuds.monitoringeqiupment.exception.*;
import com.suaistuds.monitoringeqiupment.model.directory.Role;
import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.model.enums.RoleName;
import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.payload.user.*;
import com.suaistuds.monitoringeqiupment.repository.RoleRepository;
import com.suaistuds.monitoringeqiupment.repository.UserRepository;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import com.suaistuds.monitoringeqiupment.service.UserService;
import com.suaistuds.monitoringeqiupment.util.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ModelMapper modelMapper;

    @Override
    public PagedResponse<UserProfile> getAll(int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> p = userRepository.findAll(pg);
        List<UserProfile> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public UserProfile getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return toDto(user);
    }

    @Override
    public UserProfile getUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return toDto(user);

    }

    @Override
    public UserProfile getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return toDto(user);
    }

    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {

        if (currentUser == null) {
            throw new ResourceNotFoundException("User", "currentUser", null);
        }
        return new UserSummary(currentUser.getId(), currentUser.getUsername());

    }

    @Override
    public PagedResponse<UserProfile> getUsersByRole(Long roleId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));
        Pageable pg = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> p = userRepository.findAll(pg);
        List<UserProfile> dtos = p.getContent().stream().map(this::toDto).toList();

        return toPagedResponse(p);
    }

    @Override
    public boolean existsByUsername(String username) {

        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {

        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();

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

        User user = userRepository.getUserByName(username);
        return toDto(user);
    }

    @Override
    public UserSummary create(SignUpRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email is already taken");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        Role role = roleRepository.findByName(RoleName.user)
                .orElseThrow(() -> new AppException("User role not set"));
        user.setRole(role);
        User saved = userRepository.save(user);
        return new UserSummary(saved.getId(), saved.getUsername());
    }

    @Override
    public UserSummary update(String username, UserUpdateRequest updateRequest, UserPrincipal currentUser) {

        User user = userRepository.getUserByName(username);

        boolean self = user.getId().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));

        if (!self && !admin) {
            throw new UnauthorizedException("You don't have permission to update this user");
        }

        if (userRepository.existsByUsername(updateRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(updateRequest.getEmail())) {
            throw new BadRequestException("Email is already taken");
        }

        user.setUsername(updateRequest.getUsername());
        user.setEmail(updateRequest.getEmail());
        user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        User saved = userRepository.save(user);
        return new UserSummary(saved.getId(), saved.getUsername());
    }

    @Override
    public void delete(String username, UserPrincipal currentUser) {

        User user = userRepository.getUserByName(username);

        boolean self = user.getId().equals(currentUser.getId());
        boolean admin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleName.admin.name()));

        if (!self && !admin) {
            throw new UnauthorizedException("You don't have permission to delete this user");
        }

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void giveAdmin(String username) {

        User u = userRepository.getUserByName(username);
        Role admin = roleRepository.findByName(RoleName.admin)
                .orElseThrow(() -> new AppException("Admin role not set"));
        u.setRole(admin);
    }

    @Override
    @Transactional
    public void removeAdmin(String username) {

        User u = userRepository.getUserByName(username);
        Role userRole = roleRepository.findByName(RoleName.user)
                .orElseThrow(() -> new AppException("User role not set"));
        u.setRole(userRole);
    }

    @Override
    @Transactional
    public void giveStudio(String username) {

        User u = userRepository.getUserByName(username);
        Role studio = roleRepository.findByName(RoleName.studio)
                .orElseThrow(() -> new AppException("Studio role not set"));
        u.setRole(studio);
    }

    @Override
    @Transactional
    public void removeStudio(String username) {

        User u = userRepository.getUserByName(username);
        Role userRole = roleRepository.findByName(RoleName.user)
                .orElseThrow(() -> new AppException("User role not set"));
        u.setRole(userRole);
    }

    private PagedResponse<UserProfile> toPagedResponse(Page<User> page) {
        List<UserProfile> dtos = page.getContent().stream()
                .map(this::toDto)
                .toList();
        return new PagedResponse<>(dtos, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());
    }
    
    private UserProfile toDto(User user) {
        UserProfile dto = modelMapper.map(user, UserProfile.class);
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoleId(user.getRole().getId());
        dto.setId(user.getId());

        return dto;
    }
}
