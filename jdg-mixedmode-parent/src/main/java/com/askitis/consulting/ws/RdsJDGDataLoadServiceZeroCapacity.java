package com.askitis.consulting.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.askitis.consulting.rds.data.DerivativesDTO;
import com.askitis.consulting.rds.model.DerivativeProduct;
import com.askitis.consulting.ws.DataLocation;

@WebService(targetNamespace = "http://www.askitis.com/jdg/load/capacityzero")
public interface RdsJDGDataLoadServiceZeroCapacity {

    @WebMethod
    public @WebResult(name="CacheStartResult") Boolean startCache(@WebParam(name="CacheMode") Mode mode, 
    		@WebParam(name="CacheName") String cacheName);
	
    /**
     * Initiates the load of DATA from DB to JDG
     * 
     * @return The number of records loaded
     * 
     */

    @WebMethod
    public @WebResult(name="CacheLoadResult") Integer loadJDGData(@WebParam(name="CacheMode") Mode mode, 
    		@WebParam(name="CacheName") String cacheName);

    /**
     * Add single entrt to JDG
     **/
    @WebMethod
    public @WebResult(name="CacheLoadResult") Integer addJDGEntry(@WebParam(name="CacheMode") Mode mode, 
    		@WebParam(name="CacheName") String cacheName);
    
    @WebMethod
    public @WebResult(name="CacheRetrieveResult") DerivativesDTO retrieveDGData(@WebParam(name="CacheMode") Mode mode, 
    		@WebParam(name="CacheName") String cacheName, @WebParam(name="DataFrom") DataLocation dataFrom);
    
    @WebMethod
    public @WebResult(name="CacheRetrieveResult") List<DerivativeProduct> searchAllJDGData(@WebParam(name="CacheMode") Mode mode, 
    		@WebParam(name="CacheName") String cacheName, @WebParam(name="DataFrom") DataLocation dataFrom);

    @WebMethod
    public @WebResult(name="CacheRetrieveResult") List<DerivativeProduct> getAllJDGDataByMapReduceKeys(@WebParam(name="CacheMode") Mode mode, 
    		@WebParam(name="CacheName") String cacheName);    
    }