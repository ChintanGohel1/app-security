package com.test.util;

import java.net.HttpCookie;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.util.TokenUtils;

public class CustomRestTemplate extends RestTemplate {

	private MediaType defaultResponseContentType;
	public String authenticationToken;

	public CustomRestTemplate() {
		super();
	}

	public CustomRestTemplate(ClientHttpRequestFactory requestFactory) {
		super(requestFactory);
	}

	public void setDefaultResponseContentType(String defaultResponseContentType) {
		this.defaultResponseContentType = MediaType.parseMediaType(defaultResponseContentType);
	}

	public <T> ResponseEntity<T> postForEntity(String absolutePath, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
		try {
			ResponseEntity<T> authenticationResponse = super.postForEntity(
			        TestApiConfig.getAbsolutePath(absolutePath),
			        request,
			        responseType
			        );
			System.out.println("authenticationResponse = " + authenticationResponse);

			setTokenFromResponseHeader(authenticationResponse.getHeaders());

			return authenticationResponse;

		} catch (HttpClientErrorException e) {

			setTokenFromResponseHeader(e.getResponseHeaders());

			throw e;

		}
	}

	public <T> ResponseEntity<T> exchange(String absolutePath, HttpMethod methodType, HttpEntity<Object> builder, Class<T> responseType) {

		System.out.println("authenticationToken = " + authenticationToken);
		try {
			ResponseEntity<T> authenticationResponse = super.exchange(
			        TestApiConfig.getAbsolutePath(absolutePath),
			        methodType,
			        builder,
			        responseType
			        );

			setTokenFromResponseHeader(authenticationResponse.getHeaders());

			return authenticationResponse;
		} catch (HttpClientErrorException e) {

			setTokenFromResponseHeader(e.getResponseHeaders());

			throw e;

		}

	}

	public void setTokenFromResponseHeader(HttpHeaders responseHeaders) {

		if (responseHeaders.containsKey("Set-Cookie")) {
			responseHeaders.get("Set-Cookie").stream().forEach(
			        headerCookie -> {

				        List<HttpCookie> cookies = HttpCookie.parse(headerCookie);

				        Optional<HttpCookie> cookie = cookies.stream().filter(cookieFilter -> TokenUtils.TOKEN_HEADER.equals(cookieFilter.getName())).findFirst();

				        if (cookie.isPresent()) {
					        authenticationToken = cookie.get().getValue();
				        }
			        }
			        );
		}
	}

	/**
	 * @param absolutePath
	 * @param request
	 * @param class1
	 * @return
	 */
	public ResponseEntity<?> exchange(String absolutePath, HttpEntity<MultiValueMap<String, Object>> request, Class<Object> responseType) {

		ResponseEntity<?> authenticationResponse = super.exchange(TestApiConfig.getAbsolutePath(absolutePath), HttpMethod.POST,
		        request,
		        String.class);

		return authenticationResponse;

	}

}