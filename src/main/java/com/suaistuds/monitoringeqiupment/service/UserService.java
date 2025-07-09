package com.suaistuds.monitoringeqiupment.service;


import com.suaistuds.monitoringeqiupment.model.entity.User;
import com.suaistuds.monitoringeqiupment.payload.ApiResponse;
import com.suaistuds.monitoringeqiupment.payload.user.*;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;

public interface UserService {

    UserSummary getCurrentUser(UserPrincipal currentUser);

    UserIdentityAvailability checkUsernameAvailability(String username);

    UserIdentityAvailability checkEmailAvailability(String email);

    UserProfile getUserProfile(String username);

    UserSummary create(SignUpRequest req);

    UserSummary update(String username, UserUpdateRequest req, UserPrincipal currentUser);

    void delete(String username, UserPrincipal currentUser);

    void giveAdmin(String username);

    void removeAdmin(String username);

    void giveStudio(String username);

    void removeStudio(String username);
}
