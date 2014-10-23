package com.askitis.consulting.jdg.jgroups;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.infinispan.Cache;
import org.infinispan.commons.CacheException;
import org.infinispan.commons.util.FileLookup;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.Transport;
import org.infinispan.remoting.transport.jgroups.JGroupsChannelLookup;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;
import org.jgroups.Channel;
import org.jgroups.conf.ConfiguratorFactory;
import org.jgroups.conf.ProtocolStackConfigurator;

public class HybridCluster {
	static final String JGROUPS_CONFIGURATION_FILE = "jgroups-udp.xml";
   private DefaultCacheManager cm;
   
   @Inject
   private Logger log;

	public static class MuxChannelLookup implements JGroupsChannelLookup {
//		@Override
//		public Channel getJGroupsChannel(Properties p) {
////			FileLookup fileLookup = new FileLookup();
//			try {
////				String configFile = p.getProperty(JGroupsTransport.CONFIGURATION_FILE);
////				ProtocolStackConfigurator configurator = ConfiguratorFactory.getStackConfigurator(fileLookup.lookupFileLocation(configFile, HybridCluster.class.getClassLoader()));
//				InputStream inputStream = getClass().getClassLoader().getResourceAsStream(JGROUPS_CONFIGURATION_FILE);
//				ProtocolStackConfigurator configurator = ConfiguratorFactory.getStackConfigurator(inputStream);
//				return new MuxChannel(configurator);
//				//return new JChannel(configurator);
//			} catch (Exception e) {
//				throw new CacheException("Unable to start JGroups channel", e);
//			}
//		}
		
	      @Override
	      public Channel getJGroupsChannel(Properties p) {
	         FileLookup fileLookup = new FileLookup();
	         try {
	            String configFile = p.getProperty(JGroupsTransport.CONFIGURATION_FILE);
	            ProtocolStackConfigurator configurator = ConfiguratorFactory.getStackConfigurator(fileLookup.lookupFileLocation(configFile, HybridCluster.class.getClassLoader()));
	            return new MuxChannel(configurator);
	            //return new JChannel(configurator);
	         } catch (Exception e) {
	            throw new CacheException("Unable to start JGroups channel", e);
	         }
	      }		
		
		@Override
		public boolean shouldConnect() {
			return true;
		}
		@Override
		public boolean shouldDisconnect() {
			return true;
		}
		@Override
		public boolean shouldClose() {
			return true;
		}
	}
//	public static void main(String[] args) throws InterruptedException {
	
//	public static GlobalConfiguration buildHybridClusterManagerConfiguration(){
//		GlobalConfigurationBuilder global = new GlobalConfigurationBuilder();
//		global.clusteredDefault().transport().clusterName("clustered").nodeName("embedded").addProperty(JGroupsTransport.CONFIGURATION_FILE, JGROUPS_CONFIGURATION_FILE)
//		.addProperty(JGroupsTransport.CHANNEL_LOOKUP, MuxChannelLookup.class.getName());
//		global.serialization().classResolver(HybridClassResolver.getInstance(HybridCluster.class.getClassLoader()));
//		global.globalJmxStatistics().cacheManagerName("RDSCacheManager").allowDuplicateDomains(true).enable() ;
//
//		return global.build();
//
//	}
	
	public HybridCluster() {
		org.infinispan.remoting.transport.Transport muxTransport = getMUXChannel();

//		
//		GlobalConfigurationBuilder global = new GlobalConfigurationBuilder();
//		global.clusteredDefault().transport().clusterName("clustered").nodeName("embedded").addProperty(JGroupsTransport.CONFIGURATION_FILE, configurationFile).addProperty(JGroupsTransport.CHANNEL_LOOKUP, MuxChannelLookup.class.getName());
//		global.serialization().classResolver(HybridClassResolver.getInstance(HybridCluster.class.getClassLoader()));
//		ConfigurationBuilder config = new ConfigurationBuilder();
//		config.clustering().cacheMode(CacheMode.DIST_SYNC);
//		cm = new DefaultCacheManager(global.build(), config.build());
		
		DefaultCacheManager manager = new DefaultCacheManager(
		new GlobalConfigurationBuilder()
		.clusteredDefault().transport().transport(muxTransport).clusterName("RDS-DATAGRID-CLUSTER").nodeName("Loader").addProperty(JGroupsTransport.CHANNEL_LOOKUP, MuxChannelLookup.class.getName())
		.serialization().classResolver(HybridClassResolver.getInstance(HybridCluster.class.getClassLoader()))
		.globalJmxStatistics().cacheManagerName("RDSCacheManager").allowDuplicateDomains(true).enable()
		.build());
		
	}

	private Transport getMUXChannel() {
		org.infinispan.remoting.transport.Transport muxTransport = null;
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jgroups-udp.xml");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream ));
			
			ProtocolStackConfigurator configurator = ConfiguratorFactory.getStackConfigurator(inputStream);
			MuxChannel channel = new MuxChannel(configurator);
			muxTransport = new org.infinispan.remoting.transport.jgroups.JGroupsTransport(channel);
		} catch (Exception e1) {
			log.info("ERROR - creating MUXChannel");
		}
		return muxTransport;
	}

	public EmbeddedCacheManager getCacheManager() {
		return cm;
	}

	public <K, V> Cache<K, V> getCache() {
		return cm.getCache("default");
	}

}
