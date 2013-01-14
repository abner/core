package org.jboss.forge.container.impl;

import java.util.Collections;
import java.util.Set;

import javax.enterprise.inject.Typed;

import org.jboss.forge.container.services.RemoteInstance;
import org.jboss.forge.container.services.ServiceRegistry;

/**
 * Used when an addon does not provide services.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Typed()
public class NullServiceRegistry implements ServiceRegistry
{

   @Override
   public <T> void addService(Class<T> serviceType)
   {
      // no-op
   }

   @Override
   public <T> RemoteInstance<T> getRemoteInstance(Class<T> serviceType)
   {
      // no-op
      return null;
   }

   @Override
   public <T> RemoteInstance<T> getRemoteInstance(String serviceType)
   {
      // no-op
      return null;
   }

   @Override
   public <T> Set<RemoteInstance<T>> getRemoteInstances(Class<T> serviceType)
   {
      // no-op
      return Collections.emptySet();
   }

   @Override
   public Set<Class<?>> getServices()
   {
      // no-op
      return Collections.emptySet();
   }

   @Override
   public boolean hasService(Class<?> serviceType)
   {
      // no-op
      return false;
   }

   @Override
   public boolean hasService(String clazz)
   {
      // no-op
      return false;
   }

   @Override
   public Set<RemoteInstance<Object>> getRemoteInstances(String typeName)
   {
      // no-op
      return Collections.emptySet();
   }

}