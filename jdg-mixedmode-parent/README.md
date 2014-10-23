jdg-mixedmode-data-loader:       Loads data to JDG Cluster. Embedded JDG with Capacity-Factor=1 contains data
jdg-mixedmode-web-capacity-zero: Reads & Writes Data to JDG Cluster. Embedded JDG with Capacity-Factor=0 contains no data
jdg-mixedmode-map-reducer:       Module to run remote initiated map-reduce task server side

==================================================
Author: Stelios Kousouris 
Technologies: JDG 6.3.1, EAP 6.1.1, JAX-WS
Summary: 3 Maven Modules to provide JDG Mixed Mode functionality
Target Product: JDG 6.3.1
Product Versions: EAP 6.1.1, JDG 6.3.1
Source: <https://github.com/skoussou/DataGrid_POCs>  

What is it?
-----------

This example demonstrates the clustering of JDG embedded & server modes in one functional cluster. Also utilizes the capacity-factor=0 for the distribution of data between no/storage nodes


System requirements
-------------------

The applications this project produces are designed to be run on Red Hat JBoss Enterprise Application Platform 6.1.1 or later and JDG 6.3.1
 - The web applications deployed on JBoss EAP 6.1.1 server
 - The module deployed on JBoss JDG 6.3.1 server
Mysql 5.X installation for the datasource
SOAP UI to run the client requests (

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.

Build the Environment
----------------------
see the configs folder for configurations

 - 2 EAP 6.1.1 Deployments. Configure with provided
    - standalone-jdg-data-loader.xml (configures EAP for jdg-mixedmode-data-loader deployment)
	- standalone-web-jdg-client.xml (jdg-mixedmode-web-capacity-zero)
	- add mysql module
 - Setup multiples (at least 2) JDG Servers 6.3.1 GA
    - clustered-rds.xml
	- add the modules folder which contains the rds askitis module and a modification to the infinispan clustering module
	- modify the modules\system\layers\base\org\jboss\as\clustering\infinispan module (use the provided)
 - createRDSDB.sql (create database tables)
 - db-inserts.sql (insert database data)
	
The applications
-------------------------
jdg-mixedmode-data-loader (loads from the database entries to the JDG cluster on an embedded node with capacity-factor=1, ie. it contains data)
jdg-mixedmode-web-capacity-zero (adds single entries, queries the JDG cluster on an embedded node with capacity-factor=0, ie. it doesn't contain data)
jdg-mixedmode-map-reducer (is a JDG server side module which executes map-reduce tasks on the JDG servers)


Build and Deploy the POCs
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. You have to also configure the maven repository for JDG 6.3.1 (an example is provided in the configs folder). Inside the jdg-mixedmode-parent execute:
mvn clean install -DskipTests

1. Start the JBoss EAP server as described below.
   a. Start the EAP 6.1.1 where the jdg-mixedmode-data-loader will be deployed: 
   standalone.bat -c standalone-full-ha.xml -Djboss.socket.binding.port-offset=800 -Djboss.node.name=RDS-JDG-DATA-LOADER -Djboss.bind.address=192.168.1.2 -Djgroups.bind_addr=192.168.1.2 -DCONFIGURATION=MixedMode
   b. Start the EAP 6.1.1 where the jdg-mixedmode-web-capacity-zero
   standalone.bat -c standalone-full-ha.xml -Djboss.socket.binding.port-offset=900 -Djboss.node.name=RDS-1 -Djboss.bind.address=192.168.1.2 -Djgroups.bind_addr=192.168.1.2 -DCONFIGURATION=MixedMode
3. Deploy via console/deployments directory the  jdg-mixedmode-data-loader in the RDS-JDG-DATA-LOADER EAP first
4. Utilize the SOAP UI project (RdsJDGDataLoadServiceSoapBinding) and "startCache" (RDS_SYNC_WRITE_THROUGH_embedded) operation on  jdg-mixedmode-data-loader
5. Utilize the SOAP UI project (RdsJDGDataLoadServiceSoapBinding) and "loadJDGData" (RDS_SYNC_WRITE_THROUGH_embedded) on jdg-mixedmode-data-loader (14999 entries in DB table)
6. Deploy via console/deployments directory the  jdg-mixedmode-web-capacity-zero in the "RDS-1" EAP
7. Utilize the SOAP UI project (RdsJDGDataLoadServiceSoapBinding) and "startCache" operation on  jdg-mixedmode-data-loader  
8. Start the JBoss DataGrid Servers: 
   a. Start JDG Server 1: clustered.bat -b 192.168.1.2 -Djgroups.bind_addr=192.168.1.2 -Djboss.socket.binding.port-offset=100 --server-config=clustered-rds.xml -Djboss.server.base.dir=C:\Stelios\sw6\JBoss-JDG\Distros\6.3.1\jboss-datagrid-6.3.1-server\JDG-VM-1 -Djboss.node.name=DATA-ONLY-Node1
   b. Start JDG Server 2: clustered.bat -b 192.168.1.2 -Djgroups.bind_addr=192.168.1.2 -Djboss.socket.binding.port-offset=200 --server-config=clustered-rds.xml -Djboss.server.base.dir=C:\Stelios\sw6\JBoss-JDG\Distros\6.3.1\jboss-datagrid-6.3.1-server\JDG-VM-1 -Djboss.node.name=DATA-ONLY-Node2 
8. Utilize the SOAP UI project (RdsJDGDataLoadServiceZeroCapacity) and the operations add/search/getall/retrieve operations (RDS_SYNC_WRITE_THROUGH_embedded) to manipulate and retrieve data



Investigate the Console Output
----------------------------

The console for every operation will output the results (time/data) on the console

00:49:12,783 INFO  [org.jboss.as.server] (HttpManagementService-threads - 3) JBAS018559: Deployed "jdg-mixedmode-web-capacity-zero.war" (runtime-name : "jdg-mixedmode-web-capacity-zero.war")
00:53:11,829 INFO  [com.askitis.consulting.jdg.manager.RDSEmbeddedCacheManagerProvider] (http-/192.168.1.2:8980-1) === Starting EmbeddedCacheManager ...
00:53:11,830 INFO  [com.askitis.consulting.jdg.manager.RDSEmbeddedCacheManagerProvider] (http-/192.168.1.2:8980-1) [RDS POC] ***> [USING MixedMode CacheManager Configuration
00:53:14,310 INFO  [com.askitis.consulting.jdg.manager.RDSEmbeddedCacheManagerProvider] (http-/192.168.1.2:8980-1) [RDS POC] RDS_SYNC_WRITE_THROUGH_DIST Configured)
00:53:14,327 INFO  [com.askitis.consulting.jdg.manager.RDSEmbeddedCacheManagerProvider] (http-/192.168.1.2:8980-1) [RDS POC] Creating cache with create_lucene_metadata_repl_Configuration()
00:53:14,329 INFO  [com.askitis.consulting.jdg.manager.RDSEmbeddedCacheManagerProvider] (http-/192.168.1.2:8980-1) [RDS POC] Creating cache with create_lucene_data_dist_Configuration()
00:53:14,329 INFO  [com.askitis.consulting.jdg.manager.RDSEmbeddedCacheManagerProvider] (http-/192.168.1.2:8980-1) [RDS POC] Creating cache with createRDS_SYNC_DIST_Configuration()
00:53:19,897 INFO  [stdout] (http-/192.168.1.2:8980-1) 
00:53:19,898 INFO  [stdout] (http-/192.168.1.2:8980-1) -------------------------------------------------------------------
00:53:19,898 INFO  [stdout] (http-/192.168.1.2:8980-1) GMS: address=skoussou-PC-25220, cluster=RDS-DATAGRID-CLUSTER, physical address=192.168.1.2:55628
00:53:19,899 INFO  [stdout] (http-/192.168.1.2:8980-1) -------------------------------------------------------------------
..........

00:32:57,227 INFO  [com.askitis.consulting.ws.RdsJDGDataLoadServiceImpl] (http-/192.168.1.2:8880-1) [RDS POC] [USING RDS_SYNC_WRITE_THROUGH] Cache
00:33:58,659 INFO  [com.askitis.consulting.ws.RdsJDGDataLoadServiceImpl] (http-/192.168.1.2:8880-1) [RDS POC] Found 14999 products to load in JDG
00:33:58,659 INFO  [com.askitis.consulting.ws.RdsJDGDataLoadServiceImpl] (http-/192.168.1.2:8880-1) 317 millisecs to read all data from DB
00:33:58,659 INFO  [com.askitis.consulting.ws.RdsJDGDataLoadServiceImpl] (http-/192.168.1.2:8880-1) [RDS POC] [USING RDS_SYNC_WRITE_THROUGH] Cache
00:33:58,660 INFO  [com.askitis.consulting.ws.RdsJDGDataLoadServiceImpl] (http-/192.168.1.2:8880-1) [RDS POC] Prior to adding entries the clustered keys in [RDS_SYNC_WRITE_THROUGH] are
00:33:58,660 INFO  [com.askitis.consulting.rds.cache.utils.CacheInfoUtil] (http-/192.168.1.2:8880-1) About to run MapReduce for the Keys
00:33:58,735 INFO  [com.askitis.consulting.rds.cache.utils.CacheInfoUtil] (http-/192.168.1.2:8880-1) ************KEYS*****************
00:33:58,735 INFO  [com.askitis.consulting.rds.cache.utils.CacheInfoUtil] (http-/192.168.1.2:8880-1) Num Of Entries< 0>
00:33:58,735 INFO  [com.askitis.consulting.rds.cache.utils.CacheInfoUtil] (http-/192.168.1.2:8880-1) *****************************
00:33:58,735 INFO  [com.askitis.consulting.ws.RdsJDGDataLoadServiceImpl] (http-/192.168.1.2:8880-1) [RDS POC] Loading New Entries into Embedded Cache
00:34:06,834 INFO  [stdout] (http-/192.168.1.2:8880-1) 
00:34:06,835 INFO  [stdout] (http-/192.168.1.2:8880-1) -------------------------------------------------------------------
00:34:06,835 INFO  [stdout] (http-/192.168.1.2:8880-1) GMS: address=skoussou-PC-2820, cluster=RDS-INDEX-JGROUPS-CLUSTER, physical address=192.168.1.2:61534
00:34:06,835 INFO  [stdout] (http-/192.168.1.2:8880-1) -------------------------------------------------------------------

00:34:08,840 INFO  [org.hibernate.search.backend.impl.jgroups.JGroupsChannelProvider] (http-/192.168.1.2:8880-1) HSEARCH000006: Connected to cluster [ RDS-INDEX-JGROUPS-CLUSTER ]. The local Address is skoussou-PC-2820
00:34:08,843 WARN  [org.hibernate.search.backend.impl.jgroups.JGroupsChannelProvider] (http-/192.168.1.2:8880-1) HSEARCH000007: FLUSH is not present in your JGroups stack! FLUSH is needed to ensure messages are not dropped while new nodes join the cluster. Will proceed, but inconsistencies may arise!
00:46:53,719 INFO  [org.hornetq.core.server] (Thread-6 (HornetQ-server-HornetQServerImpl::serverUUID=e254e990-49f8-11e4-8b3e-d5c08e831267-1126725437)) HQ221027: Bridge ClusterConnectionBridge@51cd127e [name=sf.my-cluster.50506175-4ad2-11e4-9dd4-05f578a41557, queue=QueueImpl[name=sf.my-cluster.50506175-4ad2-11e4-9dd4-05f578a41557, postOffice=PostOfficeImpl [server=HornetQServerImpl::serverUUID=e254e990-49f8-11e4-8b3e-d5c08e831267]]@6e793826 targetConnector=ServerLocatorImpl (identity=(Cluster-connection-bridge::ClusterConnectionBridge@51cd127e [name=sf.my-cluster.50506175-4ad2-11e4-9dd4-05f578a41557, queue=QueueImpl[name=sf.my-cluster.50506175-4ad2-11e4-9dd4-05f578a41557, postOffice=PostOfficeImpl [server=HornetQServerImpl::serverUUID=e254e990-49f8-11e4-8b3e-d5c08e831267]]@6e793826 targetConnector=ServerLocatorImpl [initialConnectors=[TransportConfiguration(name=netty, factory=org-hornetq-core-remoting-impl-netty-NettyConnectorFactory) ?port=6345&host=192-168-1-2], discoveryGroupConfiguration=null]]::ClusterConnectionImpl@2018972096[nodeUUID=e254e990-49f8-11e4-8b3e-d5c08e831267, connector=TransportConfiguration(name=netty, factory=org-hornetq-core-remoting-impl-netty-NettyConnectorFactory) ?port=6245&host=192-168-1-2, address=jms, server=HornetQServerImpl::serverUUID=e254e990-49f8-11e4-8b3e-d5c08e831267])) [initialConnectors=[TransportConfiguration(name=netty, factory=org-hornetq-core-remoting-impl-netty-NettyConnectorFactory) ?port=6345&host=192-168-1-2], discoveryGroupConfiguration=null]] is connected
00:48:26,328 INFO  [com.askitis.consulting.ws.RdsJDGDataLoadServiceImpl] (http-/192.168.1.2:8880-1) [RDS POC] 57 millisecs on average to write to JDG 
00:48:26,328 INFO  [com.askitis.consulting.ws.RdsJDGDataLoadServiceImpl] (http-/192.168.1.2:8880-1) [RDS POC] 867514 millisecs to write all entries on JDG
00:48:26,328 INFO  [com.askitis.consulting.rds.cache.utils.CacheInfoUtil] (http-/192.168.1.2:8880-1) About to run MapReduce for the Keys
00:48:26,472 INFO  [com.askitis.consulting.rds.cache.utils.CacheInfoUtil] (http-/192.168.1.2:8880-1) ************KEYS*****************
00:48:26,473 INFO  [com.askitis.consulting.rds.cache.utils.CacheInfoUtil] (http-/192.168.1.2:8880-1) Num Of Entries< 14999>
00:48:26,473 INFO  [com.askitis.consulting.rds.cache.utils.CacheInfoUtil] (http-/192.168.1.2:8880-1) *****************************
00:48:26,473 INFO  [com.askitis.consulting.ws.RdsJDGDataLoadServiceImpl] (http-/192.168.1.2:8880-1) [RDS POC] keys retrieved
00:53:20,295 INFO  [org.infinispan.CLUSTER] (asyncTransportThread-15,RDS-JDG-DATA-LOADER) ISPN000310: Starting cluster-wide rebalance for cache __cluster_registry_cache__, topology CacheTopology{id=1, currentCH=ReplicatedConsistentHash{members=[skoussou-PC-57320], numSegments=60, primaryOwners=[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}, pendingCH=ReplicatedConsistentHash{members=[skoussou-PC-57320, skoussou-PC-25220], numSegments=60, primaryOwners=[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}, unionCH=null}
00:53:20,418 INFO  [org.infinispan.CLUSTER] (asyncTransportThread-6,RDS-JDG-DATA-LOADER) ISPN000310: Starting cluster-wide rebalance for cache lucene_metadata_repl, topology CacheTopology{id=1, currentCH=ReplicatedConsistentHash{members=[skoussou-PC-57320], numSegments=60, primaryOwners=[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}, pendingCH=ReplicatedConsistentHash{members=[skoussou-PC-57320, skoussou-PC-25220], numSegments=60, primaryOwners=[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}, unionCH=null}
00:53:20,420 INFO  [org.infinispan.CLUSTER] (asyncTransportThread-6,RDS-JDG-DATA-LOADER) ISPN000310: Starting cluster-wide rebalance for cache lucene_locking_repl, topology CacheTopology{id=1, currentCH=ReplicatedConsistentHash{members=[skoussou-PC-57320], numSegments=60, primaryOwners=[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}, pendingCH=ReplicatedConsistentHash{members=[skoussou-PC-57320, skoussou-PC-25220], numSegments=60, primaryOwners=[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}, unionCH=null}
00:53:20,427 INFO  [org.infinispan.CLUSTER] (asyncTransportThread-18,RDS-JDG-DATA-LOADER) ISPN000310: Starting cluster-wide rebalance for cache lucene_data_dist, topology CacheTopology{id=1, currentCH=DefaultConsistentHash{numSegments=60, numOwners=2, members=[skoussou-PC-57320]}, pendingCH=DefaultConsistentHash{numSegments=60, numOwners=2, members=[skoussou-PC-57320, skoussou-PC-25220]}, unionCH=null}
01:00:24,259 INFO  [org.infinispan.CLUSTER] (asyncTransportThread-19,RDS-JDG-DATA-LOADER) ISPN000310: Starting cluster-wide rebalance for cache __cluster_registry_cache__, topology CacheTopology{id=3, currentCH=ReplicatedConsistentHash{members=[skoussou-PC-57320, skoussou-PC-25220], numSegments=60, primaryOwners=[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}, pendingCH=ReplicatedConsistentHash{members=[skoussou-PC-57320, skoussou-PC-25220, DATA-ONLY-Node1/RDS-JDG-CLUSTER], numSegments=60, primaryOwners=[2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}, unionCH=null}
01:00:24,260 INFO  [org.infinispan.CLUSTER] (asyncTransportThread-9,RDS-JDG-DATA-LOADER) ISPN000310: Starting cluster-wide rebalance for cache lucene_data_dist, topology CacheTopology{id=3, currentCH=DefaultConsistentHash{numSegments=60, numOwners=2, members=[skoussou-PC-57320, skoussou-PC-25220]}, pendingCH=DefaultConsistentHash{numSegments=60, numOwners=2, members=[skoussou-PC-57320, skoussou-PC-25220, DATA-ONLY-Node1/RDS-JDG-CLUSTER]}, unionCH=null}
01:00:24,332 INFO  [org.infinispan.CLUSTER] (asyncTransportThread-22,RDS-JDG-DATA-LOADER) ISPN000310: Starting cluster-wide rebalance for cache lucene_locking_repl, topology CacheTopology{id=3, currentCH=ReplicatedConsistentHash{members=[skoussou-PC-57320, skoussou-PC-25220], numSegments=60, primaryOwners=[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}, pendingCH=ReplicatedConsistentHash{members=[skoussou-PC-57320, skoussou-PC-25220, DATA-ONLY-Node1/RDS-JDG-CLUSTER], numSegments=60, primaryOwners=[2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}, unionCH=null}
01:00:24,332 INFO  [org.infinispan.CLUSTER] (asyncTransportThread-11,RDS-JDG-DATA-LOADER) ISPN000310: Starting cluster-wide rebalance for cache lucene_metadata_repl, topology CacheTopology{id=3, currentCH=ReplicatedConsistentHash{members=[skoussou-PC-57320, skoussou-PC-25220], numSegments=60, primaryOwners=[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}, pendingCH=ReplicatedConsistentHash{members=[skoussou-PC-57320, skoussou-PC-25220, DATA-ONLY-Node1/RDS-JDG-CLUSTER], numSegments=60, primaryOwners=[2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}, unionCH=null}
01:00:24,537 INFO  [org.hibernate.search.indexes.impl.DirectoryBasedIndexManager] (remote-thread-1,RDS-JDG-DATA-LOADER) HSEARCH000168: Serialization service Avro SerializationProvider v1.0 being used for index 'com.askitis.consulting.rds.model.DerivativeProduct'

