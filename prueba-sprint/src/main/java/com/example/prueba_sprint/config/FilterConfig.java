package com.example.prueba_sprint.config;

import com.example.prueba_sprint.filter.RequestSecurityDebugFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RequestSecurityDebugFilter> requestSecurityDebugFilterRegistration(RequestSecurityDebugFilter filter) {
        FilterRegistrationBean<RequestSecurityDebugFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE - 10); // run before other filters
        registration.addUrlPatterns("/api/test/*");
        registration.setName("RequestSecurityDebugFilter");
        return registration;
    }
}
