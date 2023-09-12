package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import com.github.wnameless.spring.boot.up.SpringBootUpApplicationContextProvider;

@DependsOn(SpringBootUpApplicationContextProvider.BEAN_NAME)
@ConditionalOnBean(annotation = {Configuration.class, EnableEmbeddedKeycloak.class})
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
