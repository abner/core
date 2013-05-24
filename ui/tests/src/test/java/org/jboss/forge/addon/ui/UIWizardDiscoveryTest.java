package org.jboss.forge.addon.ui;

import java.util.Set;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.ui.wizard.UIWizard;
import org.jboss.forge.arquillian.Addon;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.forge.furnace.services.ExportedInstance;
import org.jboss.forge.furnace.services.ServiceRegistry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class UIWizardDiscoveryTest
{
   @Deployment
   @Dependencies(@Addon(name = "org.jboss.forge.addon:ui", version = "2.0.0-SNAPSHOT"))
   public static ForgeArchive getDeployment()
   {
      ForgeArchive archive = ShrinkWrap
               .create(ForgeArchive.class)
               .addClasses(MyFirstWizard.class)
               .addBeansXML()
               .addAsAddonDependencies(AddonDependencyEntry.create("org.jboss.forge.addon:ui", "2.0.0-SNAPSHOT"));

      return archive;
   }

   @Inject
   private ServiceRegistry registry;

   @Test
   public void testWizardRegisteredWithoutRemote() throws Exception
   {
      Set<ExportedInstance<UIWizard>> wizards = registry.getExportedInstances(UIWizard.class);
      Assert.assertFalse(wizards.isEmpty());
      ExportedInstance<UIWizard> instance = wizards.iterator().next();
      Assert.assertNotNull(instance);
      UIWizard uiWizard = instance.get();
      Assert.assertNotNull(uiWizard);
   }

}