package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@ConditionalOnBean(annotation = {Configuration.class, EnableEmbeddedKeycloak.class})
@Component
public class KeycloakConnectionJpaConfig {

  public KeycloakConnectionJpaConfig(Environment environment,
      KeycloakConnectionsJpaProperties keycloakConnectionsJpaProperties) {
    System.getProperties().setProperty("keycloak.connectionsJpa.url",
        keycloakConnectionsJpaProperties.getUrl());
    System.getProperties().setProperty("keycloak.connectionsJpa.driver",
        keycloakConnectionsJpaProperties.getDriver());
    System.getProperties().setProperty("keycloak.connectionsJpa.driverDialect",
        keycloakConnectionsJpaProperties.getDriverDialect());
    System.getProperties().setProperty("keycloak.connectionsJpa.user",
        keycloakConnectionsJpaProperties.getUser());
    System.getProperties().setProperty("keycloak.connectionsJpa.password",
        keycloakConnectionsJpaProperties.getPassword());
    System.getProperties().setProperty("keycloak.connectionsJpa.showSql",
        String.valueOf(keycloakConnectionsJpaProperties.isShowSql()));
    System.getProperties().setProperty("keycloak.connectionsJpa.formatSql",
        String.valueOf(keycloakConnectionsJpaProperties.isFormatSql()));
    System.getProperties().setProperty("keycloak.connectionsJpa.globalStatsInterval",
        String.valueOf(keycloakConnectionsJpaProperties.getGlobalStatsInterval()));
  }

}
