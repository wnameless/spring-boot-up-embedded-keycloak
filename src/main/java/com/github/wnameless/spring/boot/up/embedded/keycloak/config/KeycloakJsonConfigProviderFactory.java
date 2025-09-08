package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import java.net.URL;
import java.util.Optional;
import org.keycloak.Config.ConfigProvider;
import org.keycloak.services.util.JsonConfigProviderFactory;
import org.keycloak.util.JsonSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * Factory for creating JSON-based configuration providers for Keycloak.
 * This factory attempts to load configuration from:
 * <ol>
 *   <li>keycloak-server.json file in the classpath</li>
 *   <li>Parent factory's configuration source</li>
 *   <li>Built-in configuration template as fallback</li>
 * </ol>
 * 
 * @author Wei-Ming Wu
 */
public class KeycloakJsonConfigProviderFactory extends JsonConfigProviderFactory {

  private static final Logger LOG =
      LoggerFactory.getLogger(KeycloakJsonConfigProviderFactory.class);

  /**
   * Creates a configuration provider by attempting to load JSON configuration
   * from various sources in order of preference.
   * 
   * @return an Optional containing the ConfigProvider if configuration is found
   */
  @Override
  public Optional<ConfigProvider> create() {
    JsonNode node = null;

    try {
      URL serverConfig = Resources.getResource("keycloak-server.json");
      String serverConfigJson = Resources.toString(serverConfig, Charsets.UTF_8);
      node = JsonSerialization.mapper.readTree(serverConfigJson);
      return createJsonProvider(node);
    } catch (Exception e) {
      LOG.warn("No keycloak-server.json in classpath: {}", e.getMessage());
    }

    Optional<ConfigProvider> configProviderOpt = super.create();
    if (configProviderOpt.isEmpty()) {
      try {
        node = JsonSerialization.mapper.readTree(KeycloakServerConfigTemplate.JSON_STRING);
      } catch (JsonProcessingException e) {
        LOG.warn("Failed to load JSON config template: {}", e.getMessage());
      }
    }

    return createJsonProvider(node);
  }

}
