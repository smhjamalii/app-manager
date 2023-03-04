package ir.welldone.appmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component @Slf4j
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${api-key}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().startsWith("/swagger-ui") ||
                request.getRequestURI().startsWith("/v3/api-docs")){
            filterChain.doFilter(request, response);
        } else {
            String requestApiKey = request.getHeader("api-key");
            if (apiKey.equals(requestApiKey)) {
                filterChain.doFilter(request, response);
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid API Key");
            }
        }
    }
}
