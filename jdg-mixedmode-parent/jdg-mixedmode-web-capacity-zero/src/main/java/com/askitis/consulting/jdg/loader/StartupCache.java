package com.askitis.consulting.jdg.loader;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.askitis.consulting.jdg.manager.RDSEmbeddedCacheManagerProvider;

@Startup
@Singleton
public class StartupCache {

    @Inject
    private RDSEmbeddedCacheManagerProvider embeddedJDG;
	 
	  @PostConstruct
	  public void init() {
		  embeddedJDG.init();
		  System.out.println("StartupCache run");
	  }

	}