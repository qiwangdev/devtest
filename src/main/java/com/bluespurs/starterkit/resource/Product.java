package com.bluespurs.starterkit.resource;

import java.math.BigDecimal;
public class Product {

	private String productName;
	private BigDecimal bestPrice;
	private String currency;
	private String location;
	
	public Product(String productName, BigDecimal bestPrice, String currency,
			String location) {
		super();
		this.productName = productName;
		this.bestPrice = bestPrice;
		this.currency = currency;
		this.location = location;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getBestPrice() {
		return bestPrice;
	}
	public void setBestPrice(BigDecimal bestPrice) {
		this.bestPrice = bestPrice;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
