package com.askitis.consulting.ws;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.print.attribute.standard.DateTimeAtCompleted;

import org.hibernate.search.query.dsl.QueryBuilder;
import org.infinispan.Cache;
import org.infinispan.commons.api.BasicCache;
import org.infinispan.context.Flag;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.query.CacheQuery;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

import com.askitis.consulting.jdg.manager.CacheManagerProvider;
import com.askitis.consulting.jdg.manager.RDSEmbeddedCacheManagerProvider;
import com.askitis.consulting.jdg.manager.RDSRemoteCacheManagerProvider;
import com.askitis.consulting.rds.cache.utils.CacheInfoUtil;
import com.askitis.consulting.rds.data.DerivativesDTO;
import com.askitis.consulting.rds.data.ProductsRepository;
import com.askitis.consulting.rds.model.DerivativeProduct;
import com.askitis.consulting.rds.model.FormulaLine;
import com.askitis.consulting.rds.model.FormulaPrice;

/**
 * The implementation of the RdsJDGDataLoad JAX-WS Web Service.
 * 
 * @author stkousso@redhat.com
 */
@WebService(serviceName = "RdsJDGDataLoadServiceZeroCapacity", portName = "RdsJDGDataLoadCapacityZero", name = "RdsJDGDataLoadCapacityZero", endpointInterface = "com.askitis.consulting.ws.RdsJDGDataLoadServiceZeroCapacity", targetNamespace = "http://www.askitis.com/jdg/load/capacityzero")
public class RdsJDGDataLoadServiceImpl implements RdsJDGDataLoadServiceZeroCapacity {

	private static String LOG_PREFIX = "[RDS POC] ";
	
    @Inject
    private ProductsRepository productsRepository;
    
    @Inject
    private RDSEmbeddedCacheManagerProvider embeddedJDG;
    
    @Inject
    private RDSRemoteCacheManagerProvider remoteJDG;    
    
    @Inject
    private Logger log;
    
    @Inject
    private CacheInfoUtil cacheUtils; 
	
    @PostConstruct
    public void initCacheManagers() {
    	long start = System.currentTimeMillis();
    	embeddedJDG.getEmbeddedCacheContainer();
		long elapsedTime = System.currentTimeMillis() - start;
		log.info(LOG_PREFIX+elapsedTime + " millisecs to create EMBEDDED CacheManager");
		
		start = System.currentTimeMillis();
    	try {
			remoteJDG.getRemoteCacheContainer();
			log.info(elapsedTime + " millisecs to create REMOTE CacheManager");		
    	} catch (Exception e) {
			log.info("Error during remote cache manager creation");
			e.printStackTrace();
		}
    	elapsedTime = System.currentTimeMillis() - start;
    }
    
	@Override
	public Boolean startCache(Mode mode, String cacheName) {
		Cache cache = null;
		if (mode.equals(Mode.EMBEDDED)) {
			log.info(LOG_PREFIX+"[USING "+cacheName+"] Cache");
			cache = embeddedJDG.getEmbeddedCacheContainer().getCache(cacheName);
		} else {
			try {
//				cache= (Cache) remoteJDG.getRemoteCacheContainer().getCache(CACHE_NAME_IN_USE);
//				loadRemoteCache(cache, products);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return cache == null ? false : true;
	}
    
	@Override
	public Integer loadJDGData(Mode mode, String cacheName) {
		long start = System.currentTimeMillis();
		List<DerivativeProduct> products = productsRepository.findAll();
		log.info(LOG_PREFIX+"Found "+products.size()+" products to load in JDG");
		long elapsedTime = System.currentTimeMillis() - start;
		log.info(elapsedTime + " millisecs to read all data from DB");
		
		Cache cache = null;
		
		if (mode.equals(Mode.EMBEDDED)) {
			log.info(LOG_PREFIX+"[USING "+cacheName+"] Cache");
			cache = embeddedJDG.getEmbeddedCacheContainer().getCache(cacheName);
			loadEmbeddedCache(cache, products);
		} else {
			try {
//				cache= (Cache) remoteJDG.getRemoteCacheContainer().getCache(CACHE_NAME_IN_USE);
//				loadRemoteCache(cache, products);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return cache.size();
	}
    
	@Override
	public Integer addJDGEntry(Mode mode, String cacheName) {
		long start = System.currentTimeMillis();
		
		// NOT FROM DB 
		List<DerivativeProduct> products = new ArrayList<DerivativeProduct>();
		DerivativeProduct product1 = new DerivativeProduct();
		product1.setName("DERIVATIVE-PRODUCT-20001");
		product1.setId("20001");
		product1.setType("Product-20001");
		FormulaLine line = new FormulaLine();
		line.setId("1");
		line.setName("Extractable");
		line.setType("Extractable");
		FormulaPrice price = new FormulaPrice();
		price.setId("1");
		price.setName("OilPriceBarrel");
		price.setPrice(new BigInteger("150"));
		product1.setFormulaline(line);
		product1.setFormulaprice(price);
		products.add(product1);

		log.info(LOG_PREFIX+products.size()+" products to load in JDG (NOT FROM DB)");
		long elapsedTime = System.currentTimeMillis() - start;
		
		Cache cache = null;
		
		if (mode.equals(Mode.EMBEDDED)) {
			log.info(LOG_PREFIX+"Running with cache "+cacheName+"]");
			cache = embeddedJDG.getEmbeddedCacheContainer().getCache(cacheName);
			loadEmbeddedCache(cache, products);
		} else {
			try {
//				cache= (Cache) remoteJDG.getRemoteCacheContainer().getCache(CACHE_NAME_IN_USE);
//				loadRemoteCache(cache, products);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int resultKeys = cacheUtils.cacheKeys(cacheName).size();

		return resultKeys;
	}
	
	private Cache loadEmbeddedCache(Cache cache, List<DerivativeProduct> products) {
		long average = 0L;
		long total = 0L;
		long count = 1L;
		
		log.info(LOG_PREFIX+"Prior to adding entries the clustered keys in ["+cache.getName()+"] are");
		cacheUtils.cacheKeys(cache.getName());

		log.info(LOG_PREFIX+"Loading New Entries into Embedded Cache");

		Map<String, String> values = new HashMap<String, String>();
		values.put("key-"+System.currentTimeMillis(), "value-"+System.currentTimeMillis());
		values.put("key-"+System.currentTimeMillis()+1, "value-"+System.currentTimeMillis()+1);
		values.put("key-"+System.currentTimeMillis()+2, "value-"+System.currentTimeMillis()+2);
		values.put("key-"+System.currentTimeMillis()+3, "value-"+System.currentTimeMillis()+3);

		for (DerivativeProduct p : products) {
			long start = System.currentTimeMillis();
//			if (count < 100) {
			cache.getAdvancedCache()
			.withFlags(Flag.IGNORE_RETURN_VALUES, Flag.SKIP_CACHE_LOAD, Flag.SKIP_REMOTE_LOOKUP)
			.put(p.getName()+System.currentTimeMillis(), p);
//			}
			long elapsedTime = System.currentTimeMillis() - start;
			average += elapsedTime; 
			total += elapsedTime;
			
			
			count++;
		}
//		Runnable worker = new LoadDataRunable(products, cache);
//        executor.execute(worker);
		
		log.info(LOG_PREFIX+(average/count) + " millisecs on average to write to JDG ");
		log.info(LOG_PREFIX+total + " millisecs to write all entries on JDG");

//		log.info("getting all keys ...");
//		CacheInfoUtil utils = new CacheInfoUtil();
		cacheUtils.cacheKeys(cache.getName());
		log.info(LOG_PREFIX+"keys retrieved");
		
		long querystart = System.currentTimeMillis();

//		QueryBuilder qb = Search.getSearchManager(cache).buildQueryBuilderForClass(DerivativeProduct.class).get();
////		org.apache.lucene.search.Query query = qb.keyword().onFields("id", "type").matching("20001 Product-20001").createQuery();
//		org.apache.lucene.search.Query query = qb.keyword().onFields("type").matching("Product-20001").createQuery();
//		CacheQuery q = Search.getSearchManager(cache).getQuery(query);
//		
//		List list = q.list();
//		long elapsedQueryTime = System.currentTimeMillis() - querystart;
//
////		log.info(LOG_PREFIX+"<---------------------------- FOUND ENTRIES ---------------------------------->");
////		for (Object o : list) {
////			log.info(LOG_PREFIX+"Entry ["+o+ "]");
////		}
////		log.info(LOG_PREFIX+"<---------------------------- END OF ENTRIES ---------------------------------->");
				
//		log.info(LOG_PREFIX+" ["+products.size()+ "] entries were retrieved from JDG in ["+elapsedQueryTime+"] millisecs");


		
		return cache;
	}
	
	
	@Override
	public DerivativesDTO retrieveDGData(Mode mode, String cacheName, DataLocation dataFrom) {

		if (dataFrom != null && dataFrom.equals(DataLocation.JDG)) {
			log.info(LOG_PREFIX+"Retrieving data from JDG");

			Set<Object> keys = cacheUtils.cacheKeys(cacheName);


			//		int count=0;
			//		for (Object key : keys) {
			//			count++;
			//			log.info((String)key);
			////			log.info("Entry ["+count+"] --> "+embeddedJDG.getCache(CacheManagerProvider.CACHE_NAMES.RDS_SYNC_DIST.toString()).get((String)key));
			//		}


			Cache cache = embeddedJDG.getEmbeddedCacheContainer().getCache(cacheName);
			log.info(LOG_PREFIX+" Running with cache ["+cacheName+"]");

			QueryBuilder qb = Search.getSearchManager(cache).
					buildQueryBuilderForClass(DerivativeProduct.class).get();
			//		org.apache.lucene.search.Query query = qb.keyword().onFields("name", "author").matching("Java ro cks!").createQuery();
			org.apache.lucene.search.Query query = qb.keyword().onFields("name").matching("DerivProduct-5128").createQuery();
			CacheQuery q = Search.getSearchManager(cache).getQuery(query);
			List list = q.list();
			log.info("<-----------["+list.size()+"]----------------- FOUND ENTRIES for search onFields(\"name\").matching(\"DerivProduct-5128\")---------------------------------->");
			//		for (Object o : list) {
			//			log.info("Entry ["+o+ "]");
			//		}

			query = qb.keyword().onFields("name").matching("DERIVATIVE-PRODUCT-20001").createQuery();
			q = Search.getSearchManager(cache).getQuery(query);
			list = q.list();
			log.info("<---------["+list.size()+"]------------------- FOUND ENTRIES for search onFields(\"name\").matching(\"DERIVATIVE-PRODUCT-20001\")---------------------------------->");
			//			for (Object o : list) {
			//				log.info(LOG_PREFIX+"Entry ["+o+ "]");
			//			}

			qb = Search.getSearchManager(cache).buildQueryBuilderForClass(DerivativeProduct.class).get();
			//		org.apache.lucene.search.Query query = qb.keyword().onFields("id", "type").matching("20001 Product-20001").createQuery();
			query = qb.keyword().onFields("name").matching("DerivProduct-2").createQuery();
			q = Search.getSearchManager(cache).getQuery(query);

			list = q.list();
			log.info(LOG_PREFIX+"<-----------["+list.size()+"]----------------- FOUND ENTRIES for search onFields(\"name\").matching(\"DerivProduct-2\")---------------------------------->");

			qb = Search.getSearchManager(cache).buildQueryBuilderForClass(DerivativeProduct.class).get();
			query = qb.keyword().wildcard().onField("name").matching("DerivProduct-2*").createQuery();
			q = Search.getSearchManager(cache).getQuery(query);

			list = q.list();
			log.info(LOG_PREFIX+"<-----------["+list.size()+"]----------------- FOUND ENTRIES for search onFields(\"name\").matching(\"DerivProduct-2*\")---------------------------------->");		
			//		for (Object o : list) {
			//			log.info("Entry ["+o+ "]");
			//		}

			log.info(LOG_PREFIX+"Entry from key["+keys.toArray()[0]+"]: "+cache.get(keys.toArray()[0]));
			log.info(LOG_PREFIX+"Entry from key["+keys.toArray()[1]+"]: "+cache.get(keys.toArray()[1]));
			if (keys.size()>=14999) {
				log.info(LOG_PREFIX+"Entry from key["+keys.toArray()[14998]+"]: "+cache.get(keys.toArray()[14999]));
			}
			return new DerivativesDTO(keys.size(), list);
		}
		
		if (dataFrom != null && dataFrom.equals(DataLocation.DB)) {
			log.info(LOG_PREFIX+"Retrieving data from DB");

			long start = System.currentTimeMillis();
			List<DerivativeProduct> products = productsRepository.findAll();
			log.info(LOG_PREFIX+"Found "+products.size()+" products to load in JDG");
			long elapsedTime = System.currentTimeMillis() - start;
			log.info(elapsedTime + " millisecs to read all data from DB");
			return new DerivativesDTO(products.size(), products);

		}

		return null;
	}

	@Override
	public List<DerivativeProduct> searchAllJDGData(Mode mode, String cacheName, DataLocation dataFrom) {
		List list = new ArrayList();
		long querystart = System.currentTimeMillis();

		if (dataFrom != null && dataFrom.equals(DataLocation.JDG)) {
			log.info(LOG_PREFIX+"Searching in JDG");
			Cache cache = embeddedJDG.getEmbeddedCacheContainer().getCache(cacheName);
			log.info(LOG_PREFIX+"Running with cache "+cacheName+"]");

			QueryBuilder qb = Search.getSearchManager(cache).
					buildQueryBuilderForClass(DerivativeProduct.class).get();
			org.apache.lucene.search.Query query = qb.keyword().wildcard().onField("name").matching("*Deriv*").createQuery();
			CacheQuery q = Search.getSearchManager(cache).getQuery(query);
			list = q.list();
			log.info(LOG_PREFIX+"<---------["+list.size()+"]------------------- FOUND ENTRIES for search onFields(\"name\").matching(\"*Deriv*\")---------------------------------->");
			long elapsedQueryTime = System.currentTimeMillis() - querystart;

			log.info(LOG_PREFIX+"searchAllJDGData Retrieved ["+list.size()+ "] entries from JDG in ["+elapsedQueryTime+"] millisecs ");
		}
		if (dataFrom != null && dataFrom.equals(DataLocation.DB)) {
			log.info(LOG_PREFIX+"Retrieving data from DB");

			long start = System.currentTimeMillis();
			List<DerivativeProduct> products = productsRepository.findAll();
			log.info(LOG_PREFIX+"Found "+products.size()+" products to load in JDG");
			list.addAll(products);
			long elapsedTime = System.currentTimeMillis() - start;
			log.info(elapsedTime + " millisecs to read all data from DB");
		}
    	return list;
	}

	@Override
	public List<DerivativeProduct> getAllJDGDataByMapReduceKeys(Mode mode, String cacheName) {
		long querystart = System.currentTimeMillis();
		Set<Object> keys = cacheUtils.cacheKeys(cacheName);
//		log.info(LOG_PREFIX+"End of retrieving clustered keyes");
		
		Cache cache = embeddedJDG.getEmbeddedCacheContainer().getCache(cacheName);
		log.info(LOG_PREFIX+"Running with cache ["+cacheName+"]");
		
		List<DerivativeProduct> products = new ArrayList<DerivativeProduct>();
		Iterator keysIt = keys.iterator();
		while (keysIt.hasNext()) {
			String key = (String) keysIt.next();
			products.add((DerivativeProduct) cache.get(key));
		}
		log.info(LOG_PREFIX+"<---------["+products.size()+"]------------------- FOUND ENTRIES for search onFields(\"name\").matching(\"*Deriv*\")---------------------------------->");
		long elapsedQueryTime = System.currentTimeMillis() - querystart;

		log.info(LOG_PREFIX+"searchAllJDGData Retrieved ["+products.size()+ "] entries from JDG in ["+elapsedQueryTime+"] millisecs ");

		return products;
	}

	
	private Cache loadRemoteCache(Cache cache, List<DerivativeProduct> products) {

		return cache;
	}
}
