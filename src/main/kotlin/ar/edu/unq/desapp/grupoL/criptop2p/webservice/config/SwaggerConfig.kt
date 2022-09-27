package ar.edu.unq.desapp.grupoL.criptop2p.webservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors

import springfox.documentation.builders.RequestHandlerSelectors

import springfox.documentation.spi.DocumentationType

import springfox.documentation.spring.web.plugins.Docket


@Configuration
class SpringFoxConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
    }
}
/*
//import ar.edu.unq.desapp.grupoL.criptop2p.webservice.controller.ServiceREST
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*


@Configuration
@EnableSwagger2
class SwaggerConfig {
    
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            //.securityContexts(listOf(securityContext()))
            //.securitySchemes(listOf(apiKey(), bearerToken()))
            .select()
            .apis(RequestHandlerSelectors.any())
            //.apis(RequestHandlerSelectors.withClassAnnotation(ServiceREST::class.java))
            .paths(PathSelectors.any())
            .build()
            //.apiInfo(metaInfo())
    }

    private fun apiKey(): ApiKey {
        return ApiKey("Api key", "Authorization", "header")
    }

    private fun bearerToken(): ApiKey {
        return ApiKey("JWT", "Authentication", "header")
    }

    private fun securityContext(): SecurityContext? {
        return SecurityContext.builder().securityReferences(defaultAuth()).build()
    }

    private fun defaultAuth(): List<SecurityReference?> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes: Array<AuthorizationScope?> = arrayOfNulls(1)
        authorizationScopes[0] = authorizationScope
        return listOf(
            SecurityReference("Api key", authorizationScopes),
            SecurityReference("JWT", authorizationScopes)
        )
    }

    private fun metaInfo(): ApiInfo {
        return ApiInfo(
            "Criptop2p API REST",
            "",
            "1.0",
            "",
            Contact(
                "L-Team", "https://github.com/AlexEnigmaCode/DappsGrupoL",
                "algoritmosale@gmail.com"
            ),
            "Apache License Version 2.0",
            "https://www.apache.org/licenses/LICENSE-2.0", ArrayList()
        )
    }
}*/