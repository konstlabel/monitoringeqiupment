package com.suaistuds.monitoringeqiupment.service;

import com.suaistuds.monitoringeqiupment.payload.PagedResponse;
import com.suaistuds.monitoringeqiupment.payload.user.*;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;

public interface UserService {

    PagedResponse<UserProfile> getAll(int page, int size);

    UserProfile getUserById(Long id);

    UserProfile getUserByUsername(String username);

    UserProfile getUserByEmail(String email);

    UserSummary getCurrentUser(UserPrincipal currentUser);

    PagedResponse<UserProfile> getUsersByRole(Long roleId, int page, int size);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    UserIdentityAvailability checkUsernameAvailability(String username);

    UserIdentityAvailability checkEmailAvailability(String email);

    UserProfile getUserProfile(String username);

    UserSummary create(SignUpRequest signUpRequest);

    UserSummary update(String username, UserUpdateRequest updateRequest, UserPrincipal currentUser);

    void delete(String username, UserPrincipal currentUser);

    void giveAdmin(String username);

    void removeAdmin(String username);

    void giveStudio(String username);

    void removeStudio(String username);
}