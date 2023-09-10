package com.github.wnameless.spring.boot.up.embedded.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.github.wnameless.spring.boot.up.embedded.keycloak.config.EnableEmbeddedKeycloak;

@EnableEmbeddedKeycloak
@SpringBootApplication
public class SpringBootUpEmbeddedKeycloakTestApp {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootUpEmbeddedKeycloakTestApp.class, args);
  }

}
