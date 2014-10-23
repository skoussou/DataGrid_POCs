package com.askitis.consulting.rds.cache.utils;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
//import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.infinispan.Cache;
//import org.infinispan.cdi.Input;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.distexec.mapreduce.Collator;
import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import org.infinispan.distexec.mapreduce.Mapper;
import org.infinispan.distexec.mapreduce.Reducer;
import org.infinispan.distribution.DistributionManager;
import org.infinispan.remoting.transport.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.askitis.consulting.jdg.manager.RDSEmbeddedCacheManagerProvider;

/**
 * <code>MBean</code> which provides functionality to calculate the total number of entries in the grid.
 * <p/>
 * The functionality is implemented using the <code>JBoss Data Grid</code> Map/Reduce model.
 * <p/>
 * The MBean is implemented as a <code>Singleton EJB</code>.
 * 
 * @author <a href="mailto:duncan.doyle@redhat.com">Duncan Doyle</a>
 */
//@Singleton
//@Startup
public class CacheInfoUtil implements CacheInfo {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheInfoUtil.class);

	private static final String mBeanName = "org.jboss.ddoyle.osc2013.infinispan.demo.cache.management:type=CacheStatistics";

	private ObjectName objectName;

	public static final int DEFAULT_SCOPE = 0;
	public static final int GLOBAL_SCOPE = 1;
	public static final int LOCAL_SCOPE = 2;

//	@Inject
//	private ApplicationCacheManager cacheManager;
    @Inject
    private RDSEmbeddedCacheManagerProvider cacheManager;

	public CacheInfoUtil() {
	}

//	@PostConstruct
//	public void initialize() {
//		LOGGER.info("Registering CacheStatistics MBean.");
//		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
//		try {
//			objectName = new ObjectName(mBeanName);
//			/*
//			 * Workaround for the fact that @PreDestroy does not get called correctly when application is undeployed. So, the MBean is not
//			 * unregistered from the MBeanServer.
//			 */
//			if (mbs.isRegistered(objectName)) {
//				try {
//					mbs.unregisterMBean(objectName);
//				} catch (InstanceNotFoundException infe) {
//					String errorMessage = "Error while unregistering MBean.";
//					LOGGER.error(errorMessage, infe);
//					throw new RuntimeException(errorMessage, infe);
//				}
//			}
//			mbs.registerMBean(this, objectName);
//		} catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
//			String errorMessage = "Unable to register MBean.";
//			LOGGER.error(errorMessage, e);
//			throw new RuntimeException(errorMessage, e);
//		}
//	}

//	@PreDestroy
//	public void destroy() {
//		LOGGER.info("Unregistering ERDF CacheStatistics MBean.");
//		if (objectName != null) {
//			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
//			try {
//				mbs.unregisterMBean(objectName);
//			} catch (MBeanRegistrationException | InstanceNotFoundException e) {
//				String errorMessage = "Error unregistering CacheStatistics MBean.";
//				LOGGER.error(errorMessage);
//				// Not much we can do here to be honest, so swallowing exception.
//			}
//		} else {
//			LOGGER.warn("No MBean found to unregister.");
//		}
//	}

	public Set<Object> cacheKeys(String cacheName) {
		Map<Object, String> cacheEntryInfos = null;
		try {
			// grabbing the default application cache here ....
			Cache<Object, Object> cache = cacheManager.getCache(cacheName);
			LOGGER.info("About to run MapReduce for the Keys");

			cacheEntryInfos = getCacheEntriesInfo(cache);
			LOGGER.info("************KEYS*****************");
			LOGGER.info("Num Of Entries< "+cacheEntryInfos.keySet().size()+">");
//			LOGGER.info("KEYSET< "+cacheEntryInfos.keySet()+">");
//			LOGGER.info("Values< "+cacheEntryInfos.values()+">");
			LOGGER.info("*****************************");
			return cacheEntryInfos.keySet();

		} catch (Exception e) {
			LOGGER.error("Error CacheEntriesInfo in grid.", e);
//			RuntimeException runtimeException = new RuntimeException(e.getMessage());
//			throw runtimeException;
		}
		return new HashSet<Object>();
	}
	
	@Override
	public int numberOfEntries() {
		// grabbing the default application cache here ....
		Cache cache = cacheManager.getCache(null);
		int numberOfKeys;
		/*
		 * Doing a try catch here so we can send any exception to the JMX client as a standard Java Exception. Otherwise, the JMX client
		 * needs the proper classes on its classpath.
		 */
		try {
			numberOfKeys = getNumberOfKeys(cache);
		} catch (Exception e) {
			LOGGER.error("Error getting number of keys in grid.", e);
			RuntimeException runtimeException = new RuntimeException(e.getMessage());
			throw runtimeException;
		}
		return numberOfKeys;
		
	}

	@Override
	public Map<Object, String> cacheEntriesInfo() {
//		Map<Object, String> cacheEntryInfos = null;
//		try {
//			// grabbing the default application cache here ....
//			Cache<Object, Object> cache = cacheManager.getCache();
//			cacheEntryInfos = getCacheEntriesInfo(cache);
//		} catch (Exception e) {
//			LOGGER.error("Error CacheEntriesInfo in grid.", e);
//			RuntimeException runtimeException = new RuntimeException(e.getMessage());
//			throw runtimeException;
//		}
//		
//		return cacheEntryInfos;
		return null;
	}

	private Map<Object, String> getCacheEntriesInfo(Cache<Object, Object> cache) {

		MapReduceTask<Object, Object, Object, CacheEntryInfo> task = new MapReduceTask<Object, Object, Object, CacheEntryInfo>(cache)
				.mappedWith(new EntryInfoMapper()).reducedWith(new EntryInfoReducer());
		//return task.execute(new EntryInfoCollator());
		return task.execute(new EntryInfoCollator());
	}

	private int getNumberOfKeys(Cache cache) {
		CacheMode cacheMode = cache.getAdvancedCache().getCacheConfiguration().clustering().cacheMode();
		/*
		 * If we don't have a clustered cache or if the cache is replicated, we just return the size of the local keyset.
		 */
		boolean keysAreLocal = !cacheMode.isClustered() || cacheMode.isReplicated();
		if (keysAreLocal) {
			return cache.keySet().size();
		} else {
			MapReduceTask<Object, Object, Object, Object> task = new MapReduceTask<Object, Object, Object, Object>(cache).mappedWith(
					new KeyMapper()).reducedWith(new KeyReducer());
			return task.execute(new KeysCollator());
		}
	}

	/**
	 * The Mapper should be generic, i.e. should not only work in HotRod mode, so using Object instead of ByteArrayKey and CacheValue.
	 */
	static class KeyMapper implements Mapper<Object, Object, Object, Object> {

		/**
		 * SerialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void map(Object key, Object value, Collector<Object, Object> collector) {
			// we only want to count the keys, so we omit the values when adding to the collector.
			// LOGGER.info("==> Running Mapper : "+value);

			collector.emit(key, null);
		}
	}

	static class KeyReducer implements Reducer<Object, Object> {

		/**
		 * SerialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Object reduce(Object reducedKey, Iterator<Object> iter) {
			// Don't need to reduce anything, just return null.
			return null;
		}
	}

	static class KeysCollator implements Collator<Object, Object, Integer> {
		@Override
		public Integer collate(Map<Object, Object> reducedResults) {
			// And we return the size ....
			return reducedResults.keySet().size();
		}
	}

	static class EntryInfoMapper implements Mapper<Object, Object, Object, CacheEntryInfo> {

		/**
		 * SerialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

//		@Inject
//		@Input
//		private Cache<Object, Object> cache;

		public EntryInfoMapper() {
		}

		@Override
		public void map(Object keyIn, Object valueIn, Collector<Object, CacheEntryInfo> out) {
			// LOGGER.info("==> Running EntryInfoMapper : "+valueIn);
//			DistributionManager distManager = cache.getAdvancedCache().getDistributionManager();
			CacheEntryInfo entryInfo = new CacheEntryInfo();
			entryInfo.setKey(keyIn);
//			entryInfo.setValue(valueIn);
//			entryInfo.setPrimaryOwner(distManager.getPrimaryLocation(keyIn).toString());
//			List<Address> locationAddresses = distManager.locate(keyIn);
//			for (Address nextAddress : locationAddresses) {
//				entryInfo.addOwner(nextAddress.toString());
//			}
			out.emit(keyIn, entryInfo);
		}
	}

	static class EntryInfoReducer implements Reducer<Object, CacheEntryInfo> {

		/**
		 * SerialVersionUID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public CacheEntryInfo reduce(Object key, Iterator<CacheEntryInfo> cacheEntryInfoIterator) {
			CacheEntryInfo cacheEntryInfo;
			if (cacheEntryInfoIterator.hasNext()) {
				cacheEntryInfo = cacheEntryInfoIterator.next();
				if (cacheEntryInfoIterator.hasNext()) {
					throw new IllegalStateException("Cannot have two CacheEntryInfo objects per key. Something is seriously wrong here.");
				}
			} else {
				throw new IllegalStateException("Each key should have a CacheEntryInfo object. Something is seriously wrong here.");
			}
			return cacheEntryInfo;
		}
	}

	static class EntryInfoCollator implements Collator<Object, CacheEntryInfo, Map<Object, String>> {

		@Override
		public Map<Object, String> collate(Map<Object, CacheEntryInfo> cacheEntryInfoMap) {
			Map<Object, String> returnMap = new HashMap<Object, String>();
			Set<Map.Entry<Object, CacheEntryInfo>> entrySet = cacheEntryInfoMap.entrySet();
			for (Map.Entry<Object, CacheEntryInfo> nextEntry: entrySet) {
				returnMap.put(nextEntry.getKey(), nextEntry.getValue().toString());
			}
			return returnMap;
		}
	}

}
