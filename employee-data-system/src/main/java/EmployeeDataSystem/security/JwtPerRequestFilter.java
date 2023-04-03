package EmployeeDataSystem.security;

import java.io.IOException;
import java.util.Optional;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class JwtPerRequestFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = Token.parseToken(request);
        if (Optional.ofNullable(token).isPresent()) {
            TokenAuthentication details = new TokenAuthentication(this.jwtService.parseToken(token));
            SecurityContextHolder.getContext().setAuthentication(details);
        }

        filterChain.doFilter(request, response);
    }

    public JwtPerRequestFilter(final JwtService jwtService) {
        this.jwtService = jwtService;
    }
}
