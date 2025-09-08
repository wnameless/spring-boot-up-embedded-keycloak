package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the embedded Keycloak server.
 * These properties can be configured in application.properties or application.yml
 * with the prefix "keycloak.server".
 * 
 * @author Wei-Ming Wu
 */
@ConfigurationProperties(prefix = "keycloak.server")
public class KeycloakServerProperties {

  /**
   * The context path for the Keycloak server. Default is "/auth".
   */
  String contextPath = "/auth";
  
  /**
   * The path to the realm import file. Default is "keycloak-realm.json".
   */
  String realmImportFile = "keycloak-realm.json";
  
  /**
   * The admin user configuration for the master realm.
   */
  AdminUser adminUser = new AdminUser();

  public String getContextPath() {
    return contextPath;
  }

  public void setContextPath(String contextPath) {
    this.contextPath = contextPath;
  }

  public String getRealmImportFile() {
    return realmImportFile;
  }

  public void setRealmImportFile(String realmImportFile) {
    this.realmImportFile = realmImportFile;
  }

  public AdminUser getAdminUser() {
    return adminUser;
  }

  public void setAdminUser(AdminUser adminUser) {
    this.adminUser = adminUser;
  }

  /**
   * Configuration for the Keycloak admin user.
   * This user will be created in the master realm during server initialization.
   */
  public static class AdminUser {

    /**
     * The admin username. Default is "admin".
     */
    String username = "admin";
    
    /**
     * The admin password. Default is "admin".
     */
    String password = "admin";

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

  }

}
