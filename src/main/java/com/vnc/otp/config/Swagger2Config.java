package com.vnc.otp.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Swagger config class
 *
 * @author aarti
 *
 */
@Configuration
@EnableSwagger2
@ConditionalOnClass(Docket.class)
@ConditionalOnMissingBean(Docket.class)
public class Swagger2Config {

    @Value("${swagger.api.title}")
    private String apiTitle;

    @Value("${swagger.api.contact.info}")
    private String apiContactInfo;

    @Value("${swagger.api.license.url}")
    private String licenseUrl;

    @Value("${swagger.terms.of.service.url}")
    private String termsOfServiceUrl;

    @Value("${swagger.api.description}")
    private String apiDescription;

    @Value("${swagger.api.groupName}")
    private String groupName;

    @Value("${swagger.api.version}")
    private String apiVersion;

    @Value("${swagger.api.path.regex}")
    private String pathRegex;

    @Value("${swagger.enabled}")
    private boolean enabled;

    @Value("${swagger.contact.url}")
    private String contactUrl;

    @Value("${swagger.contact.email}")
    private String contactEmail;


    @Bean
    public Docket microServiceApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(groupName)
                .apiInfo(apiInfo())
                .enable(enabled)
                .directModelSubstitute(LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
                .select()
                .paths(apiPaths())
                .build();
    }

    private Predicate<String> apiPaths() {
        return regex(pathRegex);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(apiTitle)
                .description(apiDescription)
                .termsOfServiceUrl(termsOfServiceUrl)
                .contact(new Contact(apiContactInfo, contactUrl, contactEmail))
                .license("Apache 2.0")
                .licenseUrl(licenseUrl)
                .version(apiVersion)
                .build();
    }

}