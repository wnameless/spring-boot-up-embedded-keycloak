package com.github.wnameless.spring.boot.up.embedded.keycloak.config;

import java.io.UnsupportedEncodingException;
import org.keycloak.common.ClientConnection;
import org.keycloak.services.filters.AbstractRequestFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Servlet filter for managing Keycloak requests in an embedded environment.
 * This filter handles session management and client connection setup for incoming requests
 * to the embedded Keycloak server.
 * 
 * @author Wei-Ming Wu
 */
public class EmbeddedKeycloakRequestFilter extends AbstractRequestFilter implements Filter {

  /**
   * Processes incoming requests to the embedded Keycloak server.
   * Sets up the character encoding and creates a client connection for the request.
   * 
   * @param servletRequest the servlet request
   * @param servletResponse the servlet response
   * @param filterChain the filter chain for request processing
   * @throws UnsupportedEncodingException if UTF-8 encoding is not supported
   */
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws UnsupportedEncodingException {
    servletRequest.setCharacterEncoding("UTF-8");
    ClientConnection clientConnection = createConnection((HttpServletRequest) servletRequest);

    filter(clientConnection, (session) -> {
      try {
        filterChain.doFilter(servletRequest, servletResponse);
      } catch (Exception e) {
        // Preserve the original exception context
        throw new RuntimeException("Error processing request through Keycloak filter", e);
      }
    });
  }

  /**
   * Creates a ClientConnection from the HTTP servlet request.
   * This connection provides network information about the client.
   * 
   * @param request the HTTP servlet request
   * @return a ClientConnection implementation with request network details
   */
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
