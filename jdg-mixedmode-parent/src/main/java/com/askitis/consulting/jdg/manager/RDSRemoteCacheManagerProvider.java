package com.askitis.consulting.jdg.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.commons.CacheException;
import org.infinispan.commons.marshall.jboss.GenericJBossMarshaller;
import org.infinispan.commons.util.Util;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.protostream.SerializationContext;





import com.google.protobuf.Descriptors;

@ApplicationScoped
public class RDSRemoteCacheManagerProvider {

    @Inject
    private Logger log;

	
	   private static final String JDG_HOST = "jdg.host";
	   private static final String HOTROD_PORT = "jdg.hotrod.port";
	   private static final String JMX_PORT = "jdg.jmx.port";
	   private static final String PROPERTIES_FILE = "jdg.properties";
//	   private static final String PROTOBUF_DESCRIPTOR_RESOURCE = "/quickstart/addressbook.protobin";
	
	   private RemoteCacheManager cacheManager;
	   private RemoteCache<Integer, String> cache;
	   
	   /**
	    * The name of yor cache container, as defined in your server config.
	    */
	   private static final String CACHE_CONTAINER_NAME = "RDS.JDG";
	   
	   /**
	    * The name of your cache, as defined in your server config.
	    */
	   private static final String CACHE_NAME = "RDS.CACHE";	   
	   
	   
	   public RemoteCacheManager getRemoteCacheContainer() throws Exception {
		   final String host = jdgProperty(JDG_HOST);
		   final int hotrodPort = Integer.parseInt(jdgProperty(HOTROD_PORT));
		   final int jmxPort = Integer.parseInt(jdgProperty(JMX_PORT));

		   if (cacheManager == null) {
			   log.info("=== Preparing RemoteCacheManager (distributed CLIENT/SERVER mode) START ===");
			   try {

				   ConfigurationBuilder builder = new ConfigurationBuilder();
				   builder.addServer()
				   .host(host)
				   .port(hotrodPort)
				   .marshaller(new GenericJBossMarshaller());
				   //.marshaller(new ProtoStreamMarshaller());
				   cacheManager = new RemoteCacheManager(builder.build());

				   //		      cache = cacheManager.getCache(CACHE_NAME);
				   //		      if (cache == null) {
				   //		         throw new RuntimeException("Cache '" + CACHE_NAME + "' not found. Please make sure the server is properly configured");
				   //		      }

				   //		      registerProtofile(host, jmxPort, CACHE_CONTAINER_NAME);
				   //
				   //		      registerMarshallers(cacheManager);				   

			   } catch (Exception e) {
				   // TODO Auto-generated catch block
				   throw new CacheException("Failed to start Cache", e);
			   }
			   log.info("=== Preparing RemoteCacheManager (distributed CLIENT/SERVER mode) END ===");
		   }
		   return cacheManager;
	   }
	   
//	   /**
//	    * Register the Protobuf descriptors file on the server via JMX.
//	    */
//	   private void registerProtofile(String jmxHost, int jmxPort, String cacheContainerName) throws Exception {
//	      JMXConnector jmxConnector = JMXConnectorFactory.connect(new JMXServiceURL("service:jmx:remoting-jmx://" + jmxHost + ":" + jmxPort));
//	      MBeanServerConnection jmxConnection = jmxConnector.getMBeanServerConnection();
//
//	      ObjectName protobufMetadataManagerObjName = new ObjectName("jboss.infinispan:type=RemoteQuery,name="
//	                                                + ObjectName.quote(cacheContainerName) + ",component=ProtobufMetadataManager");
//
//	      //initialize client-side serialization context via JMX
//	      byte[] descriptor = readClasspathResource(PROTOBUF_DESCRIPTOR_RESOURCE);
//	      jmxConnection.invoke(protobufMetadataManagerObjName, "registerProtofile", new Object[]{descriptor}, new String[]{byte[].class.getName()});
//	      jmxConnector.close();
//	   }
//
//	   private byte[] readClasspathResource(String c) throws IOException {
//	      InputStream is = getClass().getResourceAsStream(c);
//	      try {
//	         return Util.readStream(is);
//	      } finally {
//	         if (is != null) {
//	            is.close();
//	         }
//	      }
//	   }
//
//	   /**
//	    * Register entity marshallers on the client side ProtoStreamMarshaller instance associated with the remote cache manager.
//	    */
//	   private void registerMarshallers(RemoteCacheManager cacheManager) throws IOException, Descriptors.DescriptorValidationException {
//	      SerializationContext ctx = ProtoStreamMarshaller.getSerializationContext(cacheManager);
//	      ctx.registerProtofile(PROTOBUF_DESCRIPTOR_RESOURCE);
//	      ctx.registerMarshaller(Person.class, new PersonMarshaller());
//	      ctx.registerMarshaller(PhoneNumber.class, new PhoneNumberMarshaller());
//	      ctx.registerMarshaller(PhoneType.class, new PhoneTypeMarshaller());
//	   }	   

	   private String jdgProperty(String name) {
	      Properties props = new Properties();
	      try {
	         props.load(RDSRemoteCacheManagerProvider.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
	      } catch (IOException ioe) {
	         throw new RuntimeException(ioe);
	      }
	      return props.getProperty(name);
	   }	   
}
