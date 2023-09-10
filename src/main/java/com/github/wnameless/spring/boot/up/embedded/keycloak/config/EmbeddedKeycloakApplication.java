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

public class EmbeddedKeycloakApplication extends KeycloakApplication {

  private static final Logger LOG = LoggerFactory.getLogger(EmbeddedKeycloakApplication.class);

  protected void loadConfig() {
    ConfigProviderFactory factory = new KeycloakJsonConfigProviderFactory();
    Config.init(factory.create().orElseThrow(() -> new NoSuchElementException("No value present")));
  }

  @Override
  protected ExportImportManager bootstrap() {
    ExportImportManager exportImportManager = super.bootstrap();
    createMasterRealmAdminUser();
    createKeycloakRealm();
    return exportImportManager;
  }

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

  private void createKeycloakRealm() {
    KeycloakSession session = getSessionFactory().create();

    try {
      session.getTransactionManager().begin();

      RealmManager manager = new RealmManager(session);
      Resource lessonRealmImportFile = new ClassPathResource(
          KeycloakServerPropertiesHolder.getKeycloakServerProperties().getRealmImportFile());
      manager.importRealm(JsonSerialization.readValue(lessonRealmImportFile.getInputStream(),
          RealmRepresentation.class));

      session.getTransactionManager().commit();
    } catch (Exception ex) {
      LOG.warn("Failed to import Realm json file: {}", ex.getMessage());
      session.getTransactionManager().rollback();
    }

    session.close();
  }

}
