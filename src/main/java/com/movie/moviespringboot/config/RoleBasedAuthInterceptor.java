package com.movie.moviespringboot.config;

import com.movie.moviespringboot.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class RoleBasedAuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Get user's information from session with key is "currentUser"
        User user = (User) request.getSession().getAttribute("currentUser");

        // If currentUser not existed or equals null then announce 401 error (unauthorized)
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }

        // Only accept for ROLE_ADMIN
        String role = user.getRole().getValue();
        if (Objects.equals(role, "ROLE_ADMIN")) {
            return true;
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            return false;
        }
    }
}
