package com.askitis.consulting.rds.model;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.*;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;


/**
 * The persistent class for the derivativeproduct database table.
 * 
 */
@Entity
@NamedQuery(name="DerivativeProduct.findAll", query="SELECT d FROM DerivativeProduct d")
@Indexed()
public class DerivativeProduct implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@DocumentId
	private String id;

	@Column
	@Fields ({
	@Field(index=Index.YES, store=Store.NO, analyze=Analyze.NO),//name="name_analyzed",
//	@Field(name="tokenizedDerivProductName", index=Index.YES, store=Store.NO, analyze=Analyze.YES)
//	@Field(index=Index.YES, store=Store.YES, analyze=Analyze.NO)
	})
	private String name;

	@Field(index=Index.YES, store=Store.YES)
	private String type;

	//bi-directional many-to-one association to Formulaprice
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="formula_price")
	@IndexedEmbedded
	private FormulaPrice formulaprice;

	//bi-directional many-to-one association to Formulaline
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="formula_line")
	@IndexedEmbedded
	private FormulaLine formulaline;

	//bi-directional many-to-one association to Balmoproductcode
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="product_code")
	@IndexedEmbedded
	private BalmoProductCode balmoproductcode;

	public DerivativeProduct() {
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

	public FormulaPrice getFormulaprice() {
		return this.formulaprice;
	}

	public void setFormulaprice(FormulaPrice formulaprice) {
		this.formulaprice = formulaprice;
	}

	public FormulaLine getFormulaline() {
		return this.formulaline;
	}

	public void setFormulaline(FormulaLine formulaline) {
		this.formulaline = formulaline;
	}

	public BalmoProductCode getBalmoproductcode() {
		return this.balmoproductcode;
	}

	public void setBalmoproductcode(BalmoProductCode balmoproductcode) {
		this.balmoproductcode = balmoproductcode;
	}

	@Override
	public String toString() {
		return "DerivativeProduct [id=" + id + ", name=" + name + ", type="
				+ type + ", formulaprice=" + formulaprice + ", formulaline="
				+ formulaline + ", balmoproductcode=" + balmoproductcode + "]";
	}
}