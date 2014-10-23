package com.askitis.consulting.jdg.manager;

public interface CacheManagerProvider<T> {
	
	public enum CACHE_NAMES {
		RDS_SYNC_DIST,
		RDS_ASYNC_DIST,
		RDS_SYNC_WRITE_THROUGH,
		RDS_ASYNC_WRITE_BEHIND, 
		lucene_metadata_repl, 
		lucene_data_dist, 
		lucene_locking_repl;
	}
	
	T getCache(String cacheName);

}
