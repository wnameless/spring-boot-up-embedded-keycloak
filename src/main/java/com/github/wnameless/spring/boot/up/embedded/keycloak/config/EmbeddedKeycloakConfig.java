package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyContextParameters;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Spring configuration class for the embedded Keycloak server.
 * This configuration is activated when the @EnableEmbeddedKeycloak annotation is present.
 * It configures the RESTEasy servlet, request filters, and connection properties.
 * 
 * @author Wei-Ming Wu
 */
@ConditionalOnBean(annotation = {Configuration.class, EnableEmbeddedKeycloak.class})
@Configuration
public class EmbeddedKeycloakConfig {

  /**
   * Creates the JPA connection properties bean for Keycloak database configuration.
   * 
   * @return the KeycloakConnectionsJpaProperties instance
   */
  @Bean
  KeycloakConnectionsJpaProperties KeycloakConnectionsJpaProperties() {
    return new KeycloakConnectionsJpaProperties();
  }

  /**
   * Creates the Keycloak server properties bean.
   * 
   * @return the KeycloakServerProperties instance
   */
  @Bean
  KeycloakServerProperties keycloakServerProperties() {
    return new KeycloakServerProperties();
  }

  /**
   * Registers the RESTEasy servlet for handling Keycloak JAX-RS endpoints.
   * 
   * @param keycloakServerProperties the server properties
   * @return the servlet registration bean for RESTEasy
   * @throws Exception if servlet registration fails
   */
  @DependsOn(KeycloakServerPropertiesHolder.BEAN_NAME)
  @Bean
  ServletRegistrationBean<HttpServlet30Dispatcher> keycloakJaxRsApplication(
      KeycloakServerProperties keycloakServerProperties) throws Exception {
    ServletRegistrationBean<HttpServlet30Dispatcher> servlet =
        new ServletRegistrationBean<>(new HttpServlet30Dispatcher());
    servlet.addInitParameter("jakarta.ws.rs.Application",
        EmbeddedKeycloakApplication.class.getName());
    servlet.addInitParameter(ResteasyContextParameters.RESTEASY_SERVLET_MAPPING_PREFIX,
        keycloakServerProperties.getContextPath());
    servlet.addInitParameter(ResteasyContextParameters.RESTEASY_USE_CONTAINER_FORM_PARAMS, "true");
    servlet.addUrlMappings(keycloakServerProperties.getContextPath() + "/*");
    servlet.setLoadOnStartup(1);
    servlet.setAsyncSupported(true);

    return servlet;
  }

  /**
   * Registers the request filter for Keycloak session management.
   * 
   * @param keycloakServerProperties the server properties
   * @return the filter registration bean for session management
   */
  @Bean
  FilterRegistrationBean<EmbeddedKeycloakRequestFilter> keycloakSessionManagement(
      KeycloakServerProperties keycloakServerProperties) {
    FilterRegistrationBean<EmbeddedKeycloakRequestFilter> filter = new FilterRegistrationBean<>();
    filter.setName("Keycloak Session Management");
    filter.setFilter(new EmbeddedKeycloakRequestFilter());
    filter.addUrlPatterns(keycloakServerProperties.getContextPath() + "/*");

    return filter;
  }

}
