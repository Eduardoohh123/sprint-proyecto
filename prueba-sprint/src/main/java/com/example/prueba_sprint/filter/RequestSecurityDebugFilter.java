package com.example.prueba_sprint.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collections;
import java.util.Enumeration;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestSecurityDebugFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestSecurityDebugFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            String path = request.getRequestURI();
            if (path != null && path.startsWith("/api/test")) {
                log.debug("[SEC-DBG] Incoming request: method={} uri={} remote={} headers=[{}]",
                        request.getMethod(), path, request.getRemoteAddr(), getRelevantHeaders(request));

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null) {
                    log.debug("[SEC-DBG] SecurityContext authentication = null (unauthenticated)");
                } else {
                    log.debug("[SEC-DBG] Authentication present: name={} authenticated={} authorities={}",
                            auth.getName(), auth.isAuthenticated(), auth.getAuthorities());
                }
            }
        } catch (Exception e) {
            log.error("[SEC-DBG] Error in RequestSecurityDebugFilter: {}", e.getMessage(), e);
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // Ensure exceptions bubble up but log for diagnosis
            log.error("[SEC-DBG] Exception during filterChain: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private String getRelevantHeaders(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> names = request.getHeaderNames();
        if (names == null) return "";
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if ("cookie".equalsIgnoreCase(name) || "authorization".equalsIgnoreCase(name) || name.toLowerCase().startsWith("x-")) {
                sb.append(name).append("=").append(request.getHeader(name)).append("; ");
            }
        }
        return sb.toString();
    }
}
