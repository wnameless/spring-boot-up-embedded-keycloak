package com.github.wnameless.spring.boot.up.embedded.keycloak.autoservice;

import java.io.File;
import org.keycloak.Config.Scope;
import org.keycloak.common.Profile;
import org.keycloak.common.profile.PropertiesFileProfileConfigResolver;
import org.keycloak.common.profile.PropertiesProfileConfigResolver;
import org.keycloak.platform.PlatformProvider;
import org.keycloak.services.ServicesLogger;
import com.google.auto.service.AutoService;

@AutoService(PlatformProvider.class)
public class KeycloakPlatformProvider implements PlatformProvider {

  public KeycloakPlatformProvider() {
    Profile.configure(new PropertiesProfileConfigResolver(System.getProperties()),
        new PropertiesFileProfileConfigResolver());
  }

  @Override
  public void onStartup(Runnable startupHook) {
    if (startupHook != null) startupHook.run();
  }

  @Override
  public void onShutdown(Runnable shutdownHook) {
    if (shutdownHook != null) {
      Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));
    }
  }

  @Override
  public void exit(Throwable cause) {
    ServicesLogger.LOGGER.fatal(cause.getMessage(), cause);
    System.exit(1);
  }

  @Override
  public File getTmpDirectory() {
    return new File(System.getProperty("java.io.tmpdir"));
  }

  @Override
  public ClassLoader getScriptEngineClassLoader(Scope scriptProviderConfig) {
    return null;
  }

  @Override
  public String name() {
    return "spring-boot-up-embedded-keycloak";
  }

}
