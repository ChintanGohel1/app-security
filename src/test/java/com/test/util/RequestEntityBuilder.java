package com.test.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.util.TokenUtils;

public class RequestEntityBuilder {

	private static final HttpHeaders HEADERS;

	static {
		HEADERS = new HttpHeaders();
		//		HEADERS.add("Content-Type", "application/json");
		HEADERS.setContentType(MediaType.APPLICATION_JSON);
		HEADERS.add(TokenUtils.TOKEN_HEADER, "");
	}

	public static HttpEntity<Object> buildRequestEntity(String authenticationToken, Object body) {
		//		HttpHeaders headers = new HttpHeaders();
		HEADERS.set(TokenUtils.TOKEN_HEADER, authenticationToken);
		//		headers.putAll(headers);
		//		headers.add("Content-Type", "application/json");
		HttpEntity<Object> entity = new HttpEntity<Object>(
		        body,
		        HEADERS
		        );
		return entity;
	}

	public static HttpEntity<Object> buildRequestEntityWithoutBody(String authenticationToken) {

		//		HttpHeaders headers = new HttpHeaders();
		HEADERS.set(TokenUtils.TOKEN_HEADER, authenticationToken);
		//		headers.putAll(headers);
		//		headers.add("Content-Type", "application/json");
		HttpEntity<Object> entity = new HttpEntity<Object>(HEADERS);
		return entity;
	}

	public static HttpEntity<Object> buildRequestEntityWithoutAuthenticationToken(Object body) {

		//		HttpHeaders headers = new HttpHeaders();
		//		headers.add("Content-Type", "application/json");
		//		headers.putAll(headers);
		HttpEntity<Object> entity = new HttpEntity<Object>(
		        body,
		        HEADERS
		        );
		return entity;
	}

	public static HttpEntity<Object> buildRequestEntityWithoutBodyOrAuthenticationToken() {

		//		HttpHeaders headers = new HttpHeaders();
		//		headers.putAll(headers);
		//		headers.add("Content-Type", "application/json");
		HttpEntity<Object> entity = new HttpEntity<Object>(HEADERS);
		return entity;
	}

}
