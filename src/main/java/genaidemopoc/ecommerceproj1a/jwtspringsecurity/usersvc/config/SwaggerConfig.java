package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title(UserServiceConstants.API_TITLE)
                .version(UserServiceConstants.API_VERSION)
                .description(UserServiceConstants.API_DESCRIPTION)
            )
            .addSecurityItem(new SecurityRequirement()
                .addList(UserServiceConstants.BEARER_AUTHENTICATION))
            .components(new Components()
                .addSecuritySchemes(UserServiceConstants.BEARER_AUTHENTICATION,
                    new SecurityScheme()
                        .name(UserServiceConstants.BEARER_AUTHENTICATION)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(UserServiceConstants.BEARER1)
                        .bearerFormat(UserServiceConstants.JWT)
                        .description("JWT Authorization header using Bearer scheme. Example: 'Bearer {token}'")
                )
            );
    }
}
