package EmployeeDataSystem.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class Token {
    public Token() {
    }

    public static String parseToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        return StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ") ? headerAuth.substring(7) : null;
    }
}
