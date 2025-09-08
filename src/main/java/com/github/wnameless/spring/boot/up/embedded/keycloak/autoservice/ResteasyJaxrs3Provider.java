package com.github.wnameless.spring.boot.up.embedded.keycloak.autoservice;

import org.jboss.resteasy.core.ResteasyContext;
import org.jboss.resteasy.spi.Dispatcher;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.common.util.ResteasyProvider;
import com.google.auto.service.AutoService;

/**
 * RESTEasy provider implementation for JAX-RS 3.0 compatibility.
 * This class bridges Keycloak's RESTEasy integration with RESTEasy 6.x,
 * which implements Jakarta EE JAX-RS 3.0 specification.
 * 
 * @author Wei-Ming Wu
 */
@AutoService(ResteasyProvider.class)
public class ResteasyJaxrs3Provider implements ResteasyProvider {

  @Override
  public <R> R getContextData(Class<R> type) {
    return ResteasyProviderFactory.getInstance().getContextData(type);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void pushDefaultContextObject(Class type, Object instance) {
    ResteasyProviderFactory.getInstance().getContextData(Dispatcher.class)
        .getDefaultContextObjects().put(type, instance);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public void pushContext(Class type, Object instance) {
    ResteasyContext.pushContext(type, instance);
  }

  @Override
  public void clearContextData() {
    ResteasyContext.clearContextData();
  }

}
