/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.bootstrap.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.forge.addon.manager.InstallRequest;
import org.jboss.forge.addon.manager.impl.AddonManagerImpl;
import org.jboss.forge.container.AddonId;
import org.jboss.forge.container.AddonRepository;
import org.jboss.forge.container.Forge;
import org.jboss.forge.container.exception.ContainerException;
import org.jboss.forge.container.spi.ContainerLifecycleListener;
import org.jboss.forge.dependencies.Coordinate;
import org.jboss.forge.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.dependencies.builder.DependencyQueryBuilder;
import org.jboss.forge.maven.dependencies.FileResourceFactory;
import org.jboss.forge.maven.dependencies.MavenContainer;
import org.jboss.forge.maven.dependencies.MavenDependencyResolver;

/**
 * Installs the required addons
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 *
 */
public class InstallRequiredAddonsListener implements ContainerLifecycleListener
{
   private Logger logger = Logger.getLogger(getClass().getName());

   private MavenDependencyResolver resolver = new MavenDependencyResolver(new FileResourceFactory(),
            new MavenContainer());

   /**
    * These addons will be installed when forge starts
    */
   private static final String[] REQUIRED_ADDON_COORDINATES = {
            "ui",
            "maven",
            "convert",
            "dependencies",
            "facets",
            "ui-hints",
            "addon-manager",
            "projects",
            "resources"
   };

   @Override
   public void beforeStart(Forge forge) throws ContainerException
   {
      AddonRepository repository = forge.getRepository();
      List<AddonId> addons = new ArrayList<AddonId>();
      for (String addonCoordinate : REQUIRED_ADDON_COORDINATES)
      {
         AddonId addonId = toAddonId(addonCoordinate);
         if (repository.isDeployed(addonId))
         {
            logger.fine("Addon " + addonId + " is already deployed");
         }
         else
         {
            addons.add(addonId);
         }
      }
      install(forge, addons);
   }

   @Override
   public void beforeStop(Forge forge) throws ContainerException
   {
      // Do nothing
   }

   @Override
   public void afterStop(Forge forge) throws ContainerException
   {
      // Do nothing
   }

   private void install(Forge forge, Iterable<AddonId> addons)
   {
      try
      {
         AddonManagerImpl addonManager = new AddonManagerImpl(forge.getRepository(), resolver);

         for (AddonId addon : addons)
         {
            InstallRequest request = addonManager.install(addon);
            logger.info(request.toString());
            request.perform();
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   private AddonId toAddonId(String addonCoordinate)
   {
      AddonId addon;
      // This allows forge --install maven
      if (addonCoordinate.contains(","))
      {
         addon = AddonId.fromCoordinates(addonCoordinate);
      }
      else
      {
         String coordinates = "org.jboss.forge:" + addonCoordinate;
         CoordinateBuilder coordinate = CoordinateBuilder.create(coordinates);
         List<Coordinate> versions = resolver.resolveVersions(DependencyQueryBuilder.create(coordinate));
         if (versions.isEmpty())
         {
            throw new IllegalArgumentException("No Artifact version found for " + coordinate);
         }
         Coordinate vCoord = versions.get(versions.size() - 1);
         addon = AddonId.from(vCoord.getGroupId() + ":" + vCoord.getArtifactId(), vCoord.getVersion());
      }
      return addon;
   }

}