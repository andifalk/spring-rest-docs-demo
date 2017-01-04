package com.example.common;

import com.example.user.boundary.UserService;
import com.example.user.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by afa on 04.01.17.
 */
public class TestAuditorWare implements AuditorAware<User> {

    private final UserService userService;

    public TestAuditorWare(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return userService.findByUsername("technical");
        }

        return ((User) authentication.getPrincipal());
    }
}
