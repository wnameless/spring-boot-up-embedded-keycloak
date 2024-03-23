package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.Import;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableAutoConfiguration(exclude = {LiquibaseAutoConfiguration.class})
@Import({EmbeddedKeycloakConfig.class, KeycloakConnectionJpaConfig.class,
    KeycloakServerPropertiesHolder.class})
public @interface EnableEmbeddedKeycloak {}
