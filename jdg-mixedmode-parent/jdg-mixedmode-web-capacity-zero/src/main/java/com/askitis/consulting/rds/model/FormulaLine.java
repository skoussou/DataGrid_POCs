package com.askitis.consulting.rds.model;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;

import java.util.List;


/**
 * The persistent class for the formulaline database table.
 * 
 */
@Entity
@NamedQuery(name="FormulaLine.findAll", query="SELECT f FROM FormulaLine f")
public class FormulaLine implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
	private String id;

	@Field(index=Index.YES, analyze=Analyze.NO) //analyze text analysis
	private String name;

	@Field(index=Index.YES, analyze=Analyze.NO) //analyze text analysis
	private String type;

	//bi-directional many-to-one association to Derivativeproduct
//	@OneToMany(mappedBy="formulaline")
//	@ContainedIn
//	private List<DerivativeProduct> derivativeproducts;

	public FormulaLine() {
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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
//		derivativeproduct.setFormulaline(this);
//
//		return derivativeproduct;
//	}
//
//	public DerivativeProduct removeDerivativeproduct(DerivativeProduct derivativeproduct) {
//		getDerivativeproducts().remove(derivativeproduct);
//		derivativeproduct.setFormulaline(null);
//
//		return derivativeproduct;
//	}

}