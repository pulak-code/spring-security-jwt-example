package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * Configuration for Spring MVC to handle static resources and routes correctly
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Configure content negotiation to prioritize JSON for API endpoints
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(false)
                .ignoreAcceptHeader(false)
                .defaultContentType(org.springframework.http.MediaType.APPLICATION_JSON);
    }
    
    /**
     * Configure path matching to use trailing slash match
     * Updated to use non-deprecated API
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Using PathPatternParser instead of the deprecated AntPathMatcher
        configurer.setPatternParser(new PathPatternParser())
                .setUseTrailingSlashMatch(true);
    }

    /**
     * Configure static resource handling to ensure controller mappings take precedence
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Explicitly map static resources
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
                
        // Add Swagger UI resources
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
                
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }
    
    /**
     * Configure CORS for all endpoints
     * Updated to use a more concise pattern for CORS configuration
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // Using pattern matching instead of allowedOrigins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "accept", "Origin", 
                               "Access-Control-Request-Method", "Access-Control-Request-Headers")
                .exposedHeaders("Authorization")
                .maxAge(3600);
    }
} 