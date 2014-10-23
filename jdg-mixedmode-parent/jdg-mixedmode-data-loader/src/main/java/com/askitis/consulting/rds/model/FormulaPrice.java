package com.askitis.consulting.rds.model;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;

import com.askitis.consulting.jdg.search.bridges.BigDecimalNumericFieldBridge;

import java.math.BigInteger;
import java.util.List;


/**
 * The persistent class for the formulaprice database table.
 * 
 */
@Entity
@NamedQuery(name="FormulaPrice.findAll", query="SELECT f FROM FormulaPrice f")
public class FormulaPrice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
	private String id;

	@Field(index=Index.YES, analyze=Analyze.NO) //analyze text analysis
	private String name;

//	@NumericField()
//	@Field
//	@FieldBridge(impl=BigDecimalNumericFieldBridge.class)
//	@Field(store=Store.YES, index=Index.YES, analyze=Analyze.NO) // store allows projection, index allows querying on it
	private BigInteger price;

	//bi-directional many-to-one association to Derivativeproduct
//	@OneToMany(mappedBy="formulaprice")
//	@ContainedIn
//	private List<DerivativeProduct> derivativeproducts;

	public FormulaPrice() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getPrice() {
		return this.price;
	}

	public void setPrice(BigInteger price) {
		this.price = price;
	}

//	public List<DerivativeProduct> getDerivativeproducts() {
//		return this.derivativeproducts;
//	}
//
//	public void setDerivativeproducts(List<DerivativeProduct> derivativeproducts) {
//		this.derivativeproducts = derivativeproducts;
//	}

//	public DerivativeProduct addDerivativeproduct(DerivativeProduct derivativeproduct) {
//		getDerivativeproducts().add(derivativeproduct);
//		derivativeproduct.setFormulaprice(this);
//
//		return derivativeproduct;
//	}
//
//	public DerivativeProduct removeDerivativeproduct(DerivativeProduct derivativeproduct) {
//		getDerivativeproducts().remove(derivativeproduct);
//		derivativeproduct.setFormulaprice(null);
//
//		return derivativeproduct;
//	}

}