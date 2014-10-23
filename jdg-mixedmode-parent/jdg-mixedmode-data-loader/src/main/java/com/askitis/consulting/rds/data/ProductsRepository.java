package com.askitis.consulting.rds.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.askitis.consulting.rds.model.DerivativeProduct;

@ApplicationScoped
public class ProductsRepository {

    @Inject
    private EntityManager em;
    
    public List<DerivativeProduct> findAll() {
    	return em.createNamedQuery("DerivativeProduct.findAll", DerivativeProduct.class).getResultList();   	
    }
}
