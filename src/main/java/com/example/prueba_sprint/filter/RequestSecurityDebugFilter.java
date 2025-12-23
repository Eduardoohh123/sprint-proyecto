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

import java.time.Instant;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestSecurityDebugFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestSecurityDebugFilter.class);

    // In-memory bounded buffer of recent debug events
    private static final int MAX_EVENTS = 200;
    private static final LinkedList<String> EVENTS = new LinkedList<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            String path = request.getRequestURI();
            if (path != null && path.startsWith("/api/test")) {
                String headers = getRelevantHeaders(request);
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String authInfo;
                if (auth == null) {
                    authInfo = "unauthenticated";
                } else {
                    authInfo = String.format("name=%s authenticated=%s authorities=%s", auth.getName(), auth.isAuthenticated(), auth.getAuthorities());
                }

                String evt = String.format("%s | method=%s uri=%s remote=%s auth=[%s] headers=[%s]",
                        Instant.now().toString(), request.getMethod(), path, request.getRemoteAddr(), authInfo, headers);

                // append to buffer (thread-safe-ish since LinkedList used only under synchronized)
                synchronized (EVENTS) {
                    EVENTS.addFirst(evt);
                    if (EVENTS.size() > MAX_EVENTS) EVENTS.removeLast();
                }

                log.debug("[SEC-DBG] {}", evt);
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

    // Expose recent events for diagnostics
    public static List<String> recentEvents() {
        synchronized (EVENTS) {
            return List.copyOf(EVENTS);
        }
    }
}
