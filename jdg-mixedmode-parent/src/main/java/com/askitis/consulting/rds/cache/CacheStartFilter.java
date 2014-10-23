package com.askitis.consulting.rds.cache;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;

import com.askitis.consulting.jdg.manager.RDSEmbeddedCacheManagerProvider;

public class CacheStartFilter implements Filter {

		public static final String CACHE_START = "cachemanagerinstantiation";
		private final Logger logger = org.slf4j.LoggerFactory.getLogger(CacheStartFilter.class);
		private ServletContext context;

	    @Inject
	    private RDSEmbeddedCacheManagerProvider embeddedJDG;
		

		@Override
		public void init(FilterConfig filterConfig) throws ServletException {
			logger.info("Starting Cache Manager via filter ...");
			embeddedJDG.init();
		}
	    
		@Override
	    public void destroy() {
	        // Nothing to do
	    }

		@Override
		public void doFilter(ServletRequest request, ServletResponse response,
                FilterChain chain) throws IOException, ServletException {
			// Filter on nothing
		}
	}