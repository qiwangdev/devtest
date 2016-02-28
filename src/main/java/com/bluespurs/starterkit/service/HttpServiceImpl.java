package com.bluespurs.starterkit.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


@Service("httpService")
public class HttpServiceImpl implements HttpService {
    private static final Logger log = LoggerFactory.getLogger(HttpServiceImpl.class);	

	@Override
    public String getHttpResponse(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        log.info("Executing request " + httpGet.getRequestLine());

        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            @Override
            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                	return null;
                }
            }

        };
		String responseBody = null;
		try {
			responseBody = httpclient.execute(httpGet, responseHandler);
		} catch (Exception e) {
			log.error("Error executing HTTP request. Exception message: " + e.getMessage());
		}

		return responseBody;
    }
}