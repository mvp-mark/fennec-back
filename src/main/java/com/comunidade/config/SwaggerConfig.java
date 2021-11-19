package com.comunidade.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.google.gson.Gson;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Value("${build.version}")		
	private String version;
	
	@Bean
	public Gson getGson() {
		return new Gson();
	}
	
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("com.comunica.portal.resources"))
          .paths(PathSelectors.any())
          .build();
    }
    
    private ApiKey apiKey() {
  		return new ApiKey("Bearer", "Authorization", "header");
  	}
      
      private ApiInfo apiInfo() {
          return new ApiInfo(
            "Duplicatas", 
            "Processamento de Duplicatas", 
            version, 
            "", 
            null, 
            "", 
            "", 
            Collections.emptyList());
     }
      
      private SecurityContext securityContext() {
  		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
  	}

  	private List<SecurityReference> defaultAuth() {
  		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
  		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
  		authorizationScopes[0] = authorizationScope;
  		return Arrays.asList(new SecurityReference("Bearer", authorizationScopes));
  	}
}
