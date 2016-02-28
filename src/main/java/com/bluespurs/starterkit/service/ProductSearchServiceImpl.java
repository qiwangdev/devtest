package com.bluespurs.starterkit.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.bluespurs.starterkit.resource.Product;

@Service("productSearchService")
public class ProductSearchServiceImpl implements ProductSearchService {
    private static final Logger log = LoggerFactory.getLogger(ProductSearchServiceImpl.class);
	
	public static final BigDecimal USD_TO_CAD = new BigDecimal("1.35");
	
	private HttpService httpService;
	private String walmartKey;
	private String bestBuyKey;
	
	@Autowired
    public ProductSearchServiceImpl(@Value("#{httpService}") HttpService httpService, @Value("#{bestBuyKey}") String bestBuyKey, @Value("#{walmartKey}") String walmartKey) {
		this.httpService = httpService;
		this.bestBuyKey = bestBuyKey;
        this.walmartKey = walmartKey;
	}
	@Override
    public Product getCheapestProduct(String q) {
		String bbRes = httpService.getHttpResponse(String.format(BESTBUY_URL, q, bestBuyKey));
		String walRes = httpService.getHttpResponse(String.format(WALMART_URL, q, walmartKey));
		Product prod = null;
		Product prod2 = null;
		BigDecimal bestPrice = null;
		Exception ex = null;
		if (bbRes != null) {
			try {
				JSONObject bbResObj = new JSONObject(bbRes);
				JSONArray items = bbResObj.getJSONArray("products");
				if (items.length() > 0) {
					JSONObject item = items.getJSONObject(0);
					prod = new Product(item.getString("name"), new BigDecimal(item.getString("salePrice").replace(",", "")), USD, BESTBUY);
				}
			} catch (JSONException e) {
				log.error("Error parsing Best Buy API JSON response. Exception message: " + e.getMessage());
			} catch (NumberFormatException e) {
				log.error("Error parsing Best Buy API JSON response (price). Exception message: " + e.getMessage());	
			}
		}
		if (walRes != null) {
			try {
				JSONObject walResObj = new JSONObject(walRes);
				JSONArray items = walResObj.getJSONArray("items");
				if (items.length() > 0) {
					JSONObject item = items.getJSONObject(0);
					BigDecimal price = new BigDecimal(item.getString("salePrice").replace(",", ""));
					if (prod == null || price.compareTo(prod.getBestPrice()) < 0) {
						prod = new Product(item.getString("name"), price, USD, WALMART);
					}
				}
			} catch (JSONException e) {
				log.error("Error parsing Walmart API JSON response. Exception message: " + e.getMessage());
			} catch (NumberFormatException e) {
				log.error("Error parsing Walmart API JSON response (price). Exception message: " + e.getMessage());
			}
		}

		if (prod == null) {
			return null;
		}
		prod.setBestPrice(prod.getBestPrice().multiply(USD_TO_CAD).setScale(2, RoundingMode.HALF_UP));
		prod.setCurrency(CAD);

		return prod;
	}
}