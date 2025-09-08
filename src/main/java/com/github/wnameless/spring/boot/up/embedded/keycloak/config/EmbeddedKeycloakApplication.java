package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import java.util.NoSuchElementException;
import org.keycloak.Config;
import org.keycloak.config.ConfigProviderFactory;
import org.keycloak.exportimport.ExportImportManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.services.managers.ApplianceBootstrap;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resources.KeycloakApplication;
import org.keycloak.util.JsonSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import com.github.wnameless.spring.boot.up.embedded.keycloak.config.KeycloakServerProperties.AdminUser;

/**
 * Keycloak application class for embedded deployment.
 * This class extends Keycloak's base application to provide custom initialization
 * including configuration loading, admin user creation, and realm import.
 * 
 * @author Wei-Ming Wu
 */
public class EmbeddedKeycloakApplication extends KeycloakApplication {

  private static final Logger LOG = LoggerFactory.getLogger(EmbeddedKeycloakApplication.class);

  /**
   * Loads the Keycloak configuration using the custom JSON config provider.
   * 
   * @throws NoSuchElementException if no configuration is present
   */
  protected void loadConfig() {
    ConfigProviderFactory factory = new KeycloakJsonConfigProviderFactory();
    Config.init(factory.create()
        .orElseThrow(() -> new NoSuchElementException("No Keycloak server config present")));
  }

  /**
   * Bootstraps the Keycloak server with initial configuration.
   * Creates the master realm admin user and imports realm configurations.
   * 
   * @return the ExportImportManager instance
   */
  @Override
  protected ExportImportManager bootstrap() {
    ExportImportManager exportImportManager = super.bootstrap();
    createMasterRealmAdminUser();
    createKeycloakRealm();
    return exportImportManager;
  }

  /**
   * Creates the admin user in the master realm.
   * The admin credentials are retrieved from the server properties.
   */
  private void createMasterRealmAdminUser() {
    KeycloakSession session = getSessionFactory().create();

    try {
      session.getTransactionManager().begin();

      AdminUser admin = KeycloakServerPropertiesHolder.getKeycloakServerProperties().getAdminUser();
      ApplianceBootstrap applianceBootstrap = new ApplianceBootstrap(session);
      applianceBootstrap.createMasterRealmUser(admin.getUsername(), admin.getPassword());

      session.getTransactionManager().commit();
    } catch (Exception ex) {
      LOG.warn("Can NOT create Keycloak master admin user: {}", ex.getMessage());
      session.getTransactionManager().rollback();
    }

    session.close();
  }

  /**
   * Imports realm configuration from a JSON file.
   * The realm file path is configured in the server properties.
   */
  private void createKeycloakRealm() {
    KeycloakSession session = getSessionFactory().create();

    try {
      session.getTransactionManager().begin();

      RealmManager manager = new RealmManager(session);
      Resource realmImportFile = new ClassPathResource(
          KeycloakServerPropertiesHolder.getKeycloakServerProperties().getRealmImportFile());
      manager.importRealm(
          JsonSerialization.readValue(realmImportFile.getInputStream(), RealmRepresentation.class));

      session.getTransactionManager().commit();
    } catch (Exception ex) {
      LOG.warn("Failed to import Realm json file: {}", ex.getMessage());
      session.getTransactionManager().rollback();
    }

    session.close();
  }

}
