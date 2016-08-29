package com.github.woooking.qa_sorting.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpUtils {
	public static String get(URI uri) {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			CloseableHttpResponse response = httpclient.execute(new HttpGet(uri));
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String get(String uri) {
		try {
			URI u = new URI(uri);
			return get(u);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
}
