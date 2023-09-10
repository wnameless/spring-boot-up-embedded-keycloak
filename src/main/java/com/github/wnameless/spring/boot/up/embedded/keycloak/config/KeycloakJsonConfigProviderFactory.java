package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Optional;
import org.keycloak.Config.ConfigProvider;
import org.keycloak.services.util.JsonConfigProviderFactory;
import org.keycloak.util.JsonSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Resources;

public class KeycloakJsonConfigProviderFactory extends JsonConfigProviderFactory {

  private static final Logger LOG =
      LoggerFactory.getLogger(KeycloakJsonConfigProviderFactory.class);

  @Override
  public Optional<ConfigProvider> create() {
    JsonNode node = null;

    try {
      URL serverConfig = Resources.getResource("keycloak-server.json");
      String serverConfigJson = Resources.toString(serverConfig, Charset.forName("UTF-8"));
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
