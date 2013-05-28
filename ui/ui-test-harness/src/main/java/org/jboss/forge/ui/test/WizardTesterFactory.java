/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.ui.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.wizard.UIWizard;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.ui.test.impl.UIContextImpl;
import org.jboss.forge.ui.test.impl.WizardTesterImpl;

/**
 * A factory for {@link WizardTester} objects
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 * 
 */
public class WizardTesterFactory
{

   @Inject
   private AddonRegistry addonRegistry;

   @Inject
   private ResourceFactory resourceFactory;

   @SuppressWarnings("unchecked")
   @Produces
   public <W extends UIWizard> WizardTester<W> produceWizardTester(InjectionPoint injectionPoint) throws Exception
   {
      Type type = injectionPoint.getAnnotated().getBaseType();

      if (type instanceof ParameterizedType)
      {
         ParameterizedType parameterizedType = (ParameterizedType) type;

         Type[] typeArguments = parameterizedType.getActualTypeArguments();
         Class<W> wizardClass = (Class<W>) typeArguments[0];
         return WizardTesterFactory.create(wizardClass, addonRegistry);
      }
      else
      {
         throw new IllegalStateException("Cannot inject a generic instance of type " + WizardTester.class.getName()
                  + "<?> without specifying concrete generic types at injection point " + injectionPoint + ".");
      }
   }

   public static <W extends UIWizard> WizardTesterImpl<W> create(Class<W> wizardClass, AddonRegistry addonRegistry,
            Resource<?>... initialSelection) throws Exception
   {
      UIContextImpl context = new UIContextImpl(initialSelection);
      return new WizardTesterImpl<W>(wizardClass, addonRegistry, context);
   }
}
