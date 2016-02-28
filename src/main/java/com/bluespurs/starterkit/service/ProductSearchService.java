package com.bluespurs.starterkit.service;
import com.bluespurs.starterkit.resource.Product;
public interface ProductSearchService {

	public static final String BESTBUY_URL = "http://api.bestbuy.com/v1/products(search=%1$s)?show=name,salePrice&format=json&sort=salePrice&apiKey=%2$s";
    public static final String WALMART_URL = "http://api.walmartlabs.com/v1/search?query=%1$s&sort=price&apiKey=%2$s";	
	
	public static final String CAD = "CAD";
	public static final String USD = "USD";
	public static final String WALMART = "Walmart";
	public static final String BESTBUY = "Best Buy";

    Product getCheapestProduct(String q);
}
