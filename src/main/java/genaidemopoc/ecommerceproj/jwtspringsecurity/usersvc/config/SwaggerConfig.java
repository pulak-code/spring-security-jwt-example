package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.SecurityConstants;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = SecurityConstants.BEARER;

        return new OpenAPI()
            .info(new Info()
                .title(AppConstants.API_TITLE)
                .version(AppConstants.API_VERSION)
                .description(AppConstants.API_DESCRIPTION)
            )
            .addSecurityItem(new SecurityRequirement()
                .addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(SecurityConstants.BEARER.trim())
                        .bearerFormat(SecurityConstants.JWT_TOKEN)
                        .description("JWT Authorization header using Bearer scheme. Example: 'Bearer {token}'")
                )
            );
    }
}
