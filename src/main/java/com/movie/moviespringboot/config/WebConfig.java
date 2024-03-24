package com.movie.moviespringboot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticationInterceptor authenticationInterceptor;
    private final RoleBasedAuthInterceptor roleBasedAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Only applies to requests to the links below
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/reviews/**");

        // Only applies to requests to the links below, role ADMIN only
        registry.addInterceptor(roleBasedAuthInterceptor)
                .addPathPatterns("/admin", "/admin/**", "/api/admin/**")
                .excludePathPatterns("/admin-assets/**", "/web/js/**", "/web/css/**", "/web/image/**");
    }

    // upload file img
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image_uploads/**")
                .addResourceLocations("file:image_uploads/");
    }
}
