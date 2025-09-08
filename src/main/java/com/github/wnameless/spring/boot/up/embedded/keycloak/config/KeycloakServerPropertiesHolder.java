package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Static holder for Keycloak server properties.
 * This class provides static access to server properties for components
 * that cannot use dependency injection.
 * 
 * @author Wei-Ming Wu
 */
@ConditionalOnBean(annotation = {Configuration.class, EnableEmbeddedKeycloak.class})
@Component(KeycloakServerPropertiesHolder.BEAN_NAME)
public class KeycloakServerPropertiesHolder {

  /**
   * Bean name constant for this component.
   */
  public static final String BEAN_NAME = "keycloakServerPropertiesHolder";

  /**
   * Static reference to the server properties.
   */
  private static KeycloakServerProperties keycloakServerProperties;

  /**
   * Constructs the holder and sets the static properties reference.
   * 
   * @param keycloakServerProperties the server properties to hold
   */
  KeycloakServerPropertiesHolder(KeycloakServerProperties keycloakServerProperties) {
    KeycloakServerPropertiesHolder.keycloakServerProperties = keycloakServerProperties;
  }

  /**
   * Gets the Keycloak server properties.
   * 
   * @return the server properties
   */
  public static KeycloakServerProperties getKeycloakServerProperties() {
    return keycloakServerProperties;
  }

  /**
   * Sets the Keycloak server properties.
   * 
   * @param keycloakServerProperties the server properties to set
   */
  public static void setKeycloakServerProperties(
      KeycloakServerProperties keycloakServerProperties) {
    KeycloakServerPropertiesHolder.keycloakServerProperties = keycloakServerProperties;
  }

}
