package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Keycloak JPA database connections.
 * These properties can be configured in application.properties or application.yml
 * with the prefix "keycloak.connections-jpa".
 * 
 * @author Wei-Ming Wu
 */
@ConfigurationProperties(prefix = "keycloak.connections-jpa")
public class KeycloakConnectionsJpaProperties {

  /**
   * JDBC URL for the database connection. Default is in-memory H2 database.
   */
  String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

  /**
   * JDBC driver class name. Default is H2 driver.
   */
  String driver = "org.h2.Driver";

  /**
   * Hibernate dialect for the database. Default is H2 dialect.
   */
  String driverDialect = "org.hibernate.dialect.H2Dialect";

  /**
   * Database username. Default is "sa".
   */
  String user = "sa";

  /**
   * Database password. Default is empty.
   */
  String password = "";

  /**
   * Whether to show SQL statements in logs. Default is false.
   */
  boolean showSql = false;

  /**
   * Whether to format SQL statements in logs. Default is true.
   */
  boolean formatSql = true;

  /**
   * Global statistics interval in seconds. Default is -1 (disabled).
   */
  int globalStatsInterval = -1;

  // boolean liquibaseShouldRun = false;

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDriver() {
    return this.driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public String getDriverDialect() {
    return this.driverDialect;
  }

  public void setDriverDialect(String driverDialect) {
    this.driverDialect = driverDialect;
  }

  public String getUser() {
    return this.user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isShowSql() {
    return this.showSql;
  }

  public boolean getShowSql() {
    return this.showSql;
  }

  public void setShowSql(boolean showSql) {
    this.showSql = showSql;
  }

  public boolean isFormatSql() {
    return this.formatSql;
  }

  public boolean getFormatSql() {
    return this.formatSql;
  }

  public void setFormatSql(boolean formatSql) {
    this.formatSql = formatSql;
  }

  public int getGlobalStatsInterval() {
    return this.globalStatsInterval;
  }

  public void setGlobalStatsInterval(int globalStatsInterval) {
    this.globalStatsInterval = globalStatsInterval;
  }

  // public boolean getLiquibaseShouldRun() {
  // return liquibaseShouldRun;
  // }

  // public void setLiquibaseShouldRun(boolean liquibaseShouldRun) {
  // this.liquibaseShouldRun = liquibaseShouldRun;
  // }

}
