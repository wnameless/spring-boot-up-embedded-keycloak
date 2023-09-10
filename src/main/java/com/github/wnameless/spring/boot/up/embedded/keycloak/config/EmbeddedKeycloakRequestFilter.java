package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import java.io.UnsupportedEncodingException;
import org.keycloak.common.ClientConnection;
import org.keycloak.services.filters.AbstractRequestFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class EmbeddedKeycloakRequestFilter extends AbstractRequestFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws UnsupportedEncodingException {
    servletRequest.setCharacterEncoding("UTF-8");
    ClientConnection clientConnection = createConnection((HttpServletRequest) servletRequest);

    filter(clientConnection, (session) -> {
      try {
        filterChain.doFilter(servletRequest, servletResponse);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private ClientConnection createConnection(HttpServletRequest request) {
    return new ClientConnection() {

      @Override
      public String getRemoteAddr() {
        return request.getRemoteAddr();
      }

      @Override
      public String getRemoteHost() {
        return request.getRemoteHost();
      }

      @Override
      public int getRemotePort() {
        return request.getRemotePort();
      }

      @Override
      public String getLocalAddr() {
        return request.getLocalAddr();
      }

      @Override
      public int getLocalPort() {
        return request.getLocalPort();
      }

    };
  }

}
