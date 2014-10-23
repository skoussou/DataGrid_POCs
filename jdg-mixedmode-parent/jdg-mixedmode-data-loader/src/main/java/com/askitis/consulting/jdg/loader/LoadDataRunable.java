package com.askitis.consulting.jdg.loader;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.infinispan.Cache;
import org.infinispan.context.Flag;

import com.askitis.consulting.jdg.manager.RDSEmbeddedCacheManagerProvider;
import com.askitis.consulting.rds.model.DerivativeProduct;

public class LoadDataRunable implements Runnable {

//	@Inject
    private static Logger log = Logger.getLogger(LoadDataRunable.class.getName());
//	
//    @Inject
//    private RDSEmbeddedCacheManagerProvider embeddedJDG;
    
    private Cache cache;
	
	private final List<DerivativeProduct> items;
	
	long start = 0l;
	 
	    public LoadDataRunable(List<DerivativeProduct> items, Cache cache) {
	        this.items = items;
	        this.cache = cache;
	        this.start = System.currentTimeMillis();
	    }
	 
	    @Override
	    public void run() {
	    	log.info("Going to add items in cache with multiple threads"); 
	    	
	    	long total = 0;
	    	for (DerivativeProduct p : items) {

	    		cache.getAdvancedCache()
	    		.withFlags(Flag.IGNORE_RETURN_VALUES, Flag.SKIP_CACHE_LOAD, Flag.SKIP_REMOTE_LOOKUP)
	    		.put(p.getName()+System.currentTimeMillis(), p);
	    	}

    		long elapsedTime = System.currentTimeMillis() - start;
    		//			average += elapsedTime; 
//    		total += elapsedTime;
    		log.info(total + " millisecs to write all entries on JDG");
	    }
	} 