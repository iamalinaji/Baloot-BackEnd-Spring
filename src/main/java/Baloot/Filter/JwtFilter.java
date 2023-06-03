package Baloot.Filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Value("${secretKey}")
    private String secretKey;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/auth") || requestURI.startsWith("/auth/");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        // Extract the JWT token from the request
        String jwt = extractJwtToken(request);

        // Validate the JWT token
        if (isValidJwtToken(jwt,secretKey)) {
            // Proceed with the request
            filterChain.doFilter(request, response);
        } else {
            // Invalid token, return 401 Unauthorized status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String extractJwtToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private boolean isValidJwtToken(String jwt,String secretKey) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt);

            // The JWT signature is valid
            return true;
        } catch (Exception e) {
            // The JWT signature is invalid or other validation error occurred
            return false;
        }
    }
}