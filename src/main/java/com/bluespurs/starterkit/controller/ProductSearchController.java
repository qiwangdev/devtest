package com.bluespurs.starterkit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.bluespurs.starterkit.resource.Product;
import org.springframework.web.bind.annotation.RequestMethod;
import com.bluespurs.starterkit.resource.Product;
import com.bluespurs.starterkit.service.ProductSearchService;

@RestController
public class ProductSearchController {
	
    public static final Logger log = LoggerFactory.getLogger(ProductSearchController.class);
	
	public static final String NO_PRODUCT_FOUND = "{\"message\":\"No product found\"}";
	
	private ProductSearchService productSearchService;
	@Autowired
	public ProductSearchController(@Value("#{productSearchService}") ProductSearchService productSearchService) {
		this.productSearchService = productSearchService;
	}
	
    @RequestMapping(value = "/product/search", method = RequestMethod.GET)
    public @ResponseBody Product productSearch(HttpServletRequest req, HttpServletResponse res) throws Exception {
        log.info("Starting product search");
		String q = req.getParameter("name");
		if (q == null || q.trim().length() == 0) {
			res.sendError(400);
			return null;
		}
		Product product = productSearchService.getCheapestProduct(q);
		if (product == null) {
			req.getRequestDispatcher("notfound").forward(req, res);
			return null;
		} else {
			return product;
		}
    }
	
	@RequestMapping(value = "/product/notfound", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> notFound(HttpServletRequest req, HttpServletResponse res) {
	    HttpHeaders httpHeaders= new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(NO_PRODUCT_FOUND, httpHeaders, HttpStatus.OK);
    }
}
