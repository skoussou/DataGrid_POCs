package com.askitis.consulting.rds.cache.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CacheEntryInfo implements Serializable {
	
	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = -5100799893486074856L;

	private Object key;
		
	private String primaryOwner;
	
	private List<String> owners = new ArrayList<String>();
	
	public CacheEntryInfo() {
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public String getPrimaryOwner() {
		return primaryOwner;
	}

	public void setPrimaryOwner(String primaryOwner) {
		this.primaryOwner = primaryOwner;
	}

	public List<String> getOwners() {
		return Collections.unmodifiableList(owners);
	}

	public void addOwner(String owner) {
		owners.add(owner);
	}

	@Override
	public String toString() {
		StringBuilder toStringBuilder = new StringBuilder();
		toStringBuilder.append("\"key\" : \"").append(key).append("\"");
		toStringBuilder.append(", \"primaryOwner\" : \"").append(primaryOwner).append("\"");
		toStringBuilder.append(". \"owners\" : \"");
		Iterator<String> ownersIterator = owners.iterator();
		while (ownersIterator.hasNext()) {
			toStringBuilder.append(ownersIterator.next());
			if (ownersIterator.hasNext()) {
				toStringBuilder.append(", ");
			}
		}
		toStringBuilder.append("\"");
		
		return toStringBuilder.toString();
	}
	
	
	
}
