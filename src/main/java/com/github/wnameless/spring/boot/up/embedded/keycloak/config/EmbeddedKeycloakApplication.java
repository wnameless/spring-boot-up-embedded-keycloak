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
 * Keycloak application class for embedded deployment. This class extends Keycloak's base
 * application to provide custom initialization including configuration loading, admin user
 * creation, and realm import.
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
   * Bootstraps the Keycloak server with initial configuration. Creates the master realm admin user
   * and imports realm configurations.
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
   * Creates the admin user in the master realm. The admin credentials are retrieved from the server
   * properties.
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
      LOG.warn("Cannot create Keycloak master admin user: {}", ex.getMessage(), ex);
      try {
        session.getTransactionManager().rollback();
      } catch (Exception rollbackEx) {
        LOG.error("Failed to rollback transaction after admin user creation failure", rollbackEx);
      }
    } finally {
      session.close();
    }
  }

  /**
   * Imports realm configuration from a JSON file. The realm file path is configured in the server
   * properties. Only imports if the realm does not already exist in the database.
   */
  private void createKeycloakRealm() {
    KeycloakSession session = getSessionFactory().create();

    try {
      session.getTransactionManager().begin();

      String realmImportPath =
          KeycloakServerPropertiesHolder.getKeycloakServerProperties().getRealmImportFile();
      Resource realmImportFile = new ClassPathResource(realmImportPath);

      if (!realmImportFile.exists()) {
        LOG.info("Realm import file not found at: {}. Skipping realm import.", realmImportPath);
        session.getTransactionManager().commit();
        return;
      }

      RealmRepresentation realmRep =
          JsonSerialization.readValue(realmImportFile.getInputStream(), RealmRepresentation.class);

      String realmId = realmRep.getId() != null ? realmRep.getId() : realmRep.getRealm();

      if (realmId == null) {
        LOG.warn("Realm import file does not contain a valid realm ID or name. Skipping import.");
        session.getTransactionManager().commit();
        return;
      }

      if (session.realms().getRealm(realmId) != null) {
        LOG.debug("Realm '{}' already exists. Skipping import.", realmId);
        session.getTransactionManager().commit();
        return;
      }

      RealmManager manager = new RealmManager(session);
      manager.importRealm(realmRep);
      LOG.info("Successfully imported realm '{}' from: {}", realmId, realmImportPath);

      session.getTransactionManager().commit();
    } catch (Exception ex) {
      LOG.warn("Could not import realm from file '{}': {}",
          KeycloakServerPropertiesHolder.getKeycloakServerProperties().getRealmImportFile(),
          ex.getMessage());
      LOG.debug("Realm import error details", ex);
      try {
        session.getTransactionManager().rollback();
      } catch (Exception rollbackEx) {
        LOG.error("Failed to rollback transaction after realm import failure", rollbackEx);
      }
    } finally {
      session.close();
    }
  }

}
