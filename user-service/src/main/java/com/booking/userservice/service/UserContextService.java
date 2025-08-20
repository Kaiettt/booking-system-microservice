package com.booking.userservice.service;
import com.booking.userservice.common.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



@Service
public class UserContextService {

    public UserContext getCurrentUser() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return getCurrentUser(request);
        }
        return null;
    }

    public UserContext getCurrentUser(HttpServletRequest request) {
        String userId = request.getHeader("X-User-Id");
        String username = request.getHeader("X-User-Name");

        return new UserContext(userId, username);
    }

    public String getCurrentUserId() {
        UserContext user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }

    public String getCurrentUserName() {
        UserContext user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }


}


