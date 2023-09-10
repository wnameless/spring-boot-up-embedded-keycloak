package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak.connections-jpa")
public class KeycloakConnectionsJpaProperties {

  String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

  String driver = "org.h2.Driver";

  String driverDialect = "org.hibernate.dialect.H2Dialect";

  String user = "sa";

  String password = "";

  boolean showSql = false;

  boolean formatSql = true;

  int globalStatsInterval = -1;

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

}
