/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.resource.hints;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.environment.Environment;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.hints.HintsLookup;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.arquillian.Addon;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.addons.AddonId;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ResourceHintsTest
{
   @Deployment
   @Dependencies({ @Addon(name = "org.jboss.forge.addon:ui-spi", version = "2.0.0-SNAPSHOT"),
            @Addon(name = "org.jboss.forge.addon:environment", version = "2.0.0-SNAPSHOT"),
            @Addon(name = "org.jboss.forge.addon:resources", version = "2.0.0-SNAPSHOT") })
   public static ForgeArchive getDeployment()
   {
      ForgeArchive archive = ShrinkWrap
               .create(ForgeArchive.class)
               .addBeansXML()
               .addAsAddonDependencies(
                        AddonDependencyEntry.create("org.jboss.forge.addon:resources", "2.0.0-SNAPSHOT"),
                        AddonDependencyEntry.create("org.jboss.forge.addon:ui-spi", "2.0.0-SNAPSHOT")
               );

      return archive;
   }

   @Inject
   private Environment environment;

   @Test
   public void testNotNull() throws Exception
   {
      Assert.assertNotNull(environment);
   }

   @Test
   public void testSimpleHintLookup() throws Exception
   {
      HintsLookup hints = new HintsLookup(environment);
      InputType type = hints.getInputType(FileResource.class);
      Assert.assertNotNull(type);
      Assert.assertEquals(InputType.FILE_PICKER, type);
   }
}
