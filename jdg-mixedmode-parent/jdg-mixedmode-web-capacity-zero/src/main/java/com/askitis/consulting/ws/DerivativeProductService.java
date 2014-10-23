package com.askitis.consulting.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.askitis.consulting.rds.model.DerivativeProduct;

@WebService(targetNamespace = "http://www.askitis.com/jdg/DerivativeProductt")
public interface DerivativeProductService {

    /**
     * Inserts/Updates DerivativeProduct in (JDG) Cache
     */
    @WebMethod	
    public void insertUpdateDerivativeProduct(DerivativeProduct product);
	
    /**
     * Inserts/Updates multiple DerivativeProduct in (JDG) Cache
     */
    @WebMethod	
    public void insertBatchDerivativeProduct(List<DerivativeProduct> products);
	
    /**
     * Search DerivativeProduct in (JDG) Cache
     * 
     * @return List {@link DerivativeProduct}
     */
    @WebMethod	    
	public List<DerivativeProduct> searchDerivativeProducts(String searchTerm);
	
}
