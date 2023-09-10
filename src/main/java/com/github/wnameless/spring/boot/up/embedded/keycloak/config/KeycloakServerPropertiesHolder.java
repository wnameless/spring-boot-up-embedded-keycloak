package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(KeycloakServerPropertiesHolder.BEAN_NAME)
public class KeycloakServerPropertiesHolder {

  public static final String BEAN_NAME = "keycloakServerPropertiesHolder";

  private static KeycloakServerProperties keycloakServerProperties;

  @Autowired
  KeycloakServerPropertiesHolder(KeycloakServerProperties keycloakServerProperties) {
    KeycloakServerPropertiesHolder.keycloakServerProperties = keycloakServerProperties;
  }

  public static KeycloakServerProperties getKeycloakServerProperties() {
    return keycloakServerProperties;
  }

  public static void setKeycloakServerProperties(
      KeycloakServerProperties keycloakServerProperties) {
    KeycloakServerPropertiesHolder.keycloakServerProperties = keycloakServerProperties;
  }

}
