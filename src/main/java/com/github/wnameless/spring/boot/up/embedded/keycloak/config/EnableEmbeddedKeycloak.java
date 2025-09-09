package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * Enables embedded Keycloak server in a Spring Boot application. This annotation imports all
 * necessary configurations and excludes Liquibase auto-configuration to prevent conflicts with
 * Keycloak's database management.
 * 
 * <p>
 * Usage example:
 * </p>
 * 
 * <pre>
 * {@code @EnableEmbeddedKeycloak}
 * {@code @SpringBootApplication}
 * public class Application {
 *     public static void main(String[] args) {
 *         SpringApplication.run(Application.class, args);
 *     }
 * }
 * </pre>
 * 
 * @author Wei-Ming Wu
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableAutoConfiguration(exclude = {LiquibaseAutoConfiguration.class})
@EnableConfigurationProperties({KeycloakConnectionsJpaProperties.class,
    KeycloakServerProperties.class})
@Import({EmbeddedKeycloakConfig.class, KeycloakConnectionJpaConfig.class,
    KeycloakServerPropertiesHolder.class})
public @interface EnableEmbeddedKeycloak {}
