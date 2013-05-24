/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.manager.impl;

import javax.inject.Inject;

import org.jboss.forge.addon.dependencies.AddonDependencyResolver;
import org.jboss.forge.addon.dependencies.DependencyMetadata;
import org.jboss.forge.addon.dependencies.DependencyNode;
import org.jboss.forge.addon.dependencies.DependencyResolver;
import org.jboss.forge.addon.dependencies.builder.DependencyQueryBuilder;
import org.jboss.forge.addon.manager.AddonManager;
import org.jboss.forge.addon.manager.DisableRequest;
import org.jboss.forge.addon.manager.InstallRequest;
import org.jboss.forge.addon.manager.RemoveRequest;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.addons.AddonId;
import org.jboss.forge.furnace.repositories.AddonRepository;

/**
 * Installs addons into an {@link AddonRepository}
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class AddonManagerImpl implements AddonManager
{
   private AddonDependencyResolver resolver;
   private Furnace forge;
   private DependencyResolver dependencyResolver;

   @Inject
   public AddonManagerImpl(Furnace forge, AddonDependencyResolver resolver, DependencyResolver dependencyResolver)
   {
      this.forge = forge;
      this.resolver = resolver;
      this.dependencyResolver = dependencyResolver;
   }

   @Override
   public InstallRequest install(AddonId id)
   {
      String coordinates = id.getName() + ":jar:forge-addon:" + id.getVersion();
      DependencyQueryBuilder query = DependencyQueryBuilder.create(coordinates);
      DependencyNode requestedAddonNode = resolver.resolveAddonDependencyHierarchy(query);
      DependencyMetadata metadata = dependencyResolver.resolveDependencyMetadata(query);

      return new InstallRequestImpl(this, forge, requestedAddonNode, metadata);
   }

   @Override
   public RemoveRequest remove(AddonId id)
   {
      return new RemoveRequestImpl(this, forge, id);
   }

   @Override
   public DisableRequest disable(AddonId id)
   {
      return new DisableRequestImpl(this, forge, id);
   }
}
