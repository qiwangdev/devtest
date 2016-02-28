package com.bluespurs.starterkit.controller;

import com.bluespurs.starterkit.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.junit.Assert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.bluespurs.starterkit.resource.Product;
import com.bluespurs.starterkit.service.ProductSearchService;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.HttpStatus;

@Category(UnitTest.class)
public class ProductSearchControllerUnitTest extends UnitTest {

    private MockMvc mockMvc;
    @Mock
    private ProductSearchService productSearchService;

    @InjectMocks
    private ProductSearchController productSearchController;
    
	@Before
    public void setUp() {
        super.setUp();
        mockMvc = MockMvcBuilders.standaloneSetup(productSearchController).build();
    }

    @Test
    public void testProductSearchBadRequest() throws Exception {
        mockMvc.perform(get("/product/search"))
            .andExpect(status().is(400));
    }
	
	@Test
    public void testProductSearch404() throws Exception {
        mockMvc.perform(get("/product"))
            .andExpect(status().is(404));
    }
	
    @Test
    public void testProductSearch() throws Exception {
		String productName = "testProd";
		BigDecimal productPrice = new BigDecimal(32);
		when(productSearchService.getCheapestProduct(isA(String.class))).thenReturn(new Product(productName, productPrice, ProductSearchService.CAD, ProductSearchService.WALMART));
        MvcResult result = mockMvc.perform(get("/product/search?name=ipad"))
            .andExpect(status().isOk())
			.andReturn();
		JSONObject resultObj = new JSONObject(result.getResponse().getContentAsString());
		Assert.assertEquals(productName, resultObj.getString("productName"));
		Assert.assertEquals(productPrice, new BigDecimal(resultObj.getString("bestPrice")));
		Assert.assertEquals(ProductSearchService.CAD, resultObj.getString("currency"));
		Assert.assertEquals(ProductSearchService.WALMART, resultObj.getString("location"));
    }	
	
    @Test
    public void testProductSearchNotFound() throws Exception {
		when(productSearchService.getCheapestProduct(isA(String.class))).thenReturn(null);
        MvcResult result = mockMvc.perform(get("/product/notfound"))
            .andExpect(status().isOk())
			.andReturn();
		Assert.assertEquals(ProductSearchController.NO_PRODUCT_FOUND, result.getResponse().getContentAsString());
    }		
}
