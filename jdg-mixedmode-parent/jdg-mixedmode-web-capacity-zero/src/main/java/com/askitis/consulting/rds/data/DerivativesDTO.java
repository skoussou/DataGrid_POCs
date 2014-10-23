package com.askitis.consulting.rds.data;

import java.util.List;

import com.askitis.consulting.rds.model.DerivativeProduct;

public class DerivativesDTO {

	private Integer dataSize;
	private List<DerivativeProduct> products;
	
	public DerivativesDTO(Integer dataSize, List<DerivativeProduct> products) {
		super();
		this.dataSize = dataSize;
		this.products = products;
	}
	
	@Override
	public String toString() {
		return "DerivativesDTO [dataSize=" + dataSize + ", products="
				+ products + "]";
	}
}
