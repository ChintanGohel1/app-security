package com.test.controller;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.Application;
import com.test.entities.AuthenticationRequest;
import com.test.entities.AuthenticationResponse;
import com.test.util.RequestEntityBuilder;
import com.test.util.TestApiConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@Ignore
public class ProtectedControllerTest {

	private RestTemplate client;
	private AuthenticationRequest authenticationRequest;
	private String authenticationToken;

	private String authenticationRoute = "login";

	//	private String protectedRoute = "user";

	@Before
	public void setUp() throws Exception {
		this.initValidUserWithValidRole();
		client = new RestTemplate();
	}

	@After
	public void tearDown() throws Exception {
		client = null;
	}

	@Test
	public void loginTestWithValidUser() {

		try {

			ResponseEntity<String> responseEntity = checkRest("login", HttpMethod.POST, TestApiConfig.ADMIN_AUTHENTICATION_REQUEST);

			assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));

		} catch (Exception e) {
			fail("Should have returned an HTTP 200: Ok status code");
		}

	}

	@Test
	public void validUserWithValidRole() {

		try {

			ResponseEntity<String> responseEntity = checkRest("user", HttpMethod.GET, TestApiConfig.ADMIN_AUTHENTICATION_REQUEST);

			assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));

		} catch (Exception e) {
			fail("Should have returned an HTTP 200: Ok status code");
		}

		//		checkStatus("role");

	}

	/*@Test
	public void getUserValidCredentials() {

		this.initValidUserWithValidRole();

		try {

			ResponseEntity<String> responseEntity = client.exchange(
			        TestApiConfig.getAbsolutePath("user"),
			        HttpMethod.GET,
			        buildProtectedRequestEntity(),
			        String.class
			        );

			assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		} catch (Exception e) {
			fail("Should have returned an HTTP 200: Ok status code");
		}

	}*/

	public ResponseEntity<String> checkRest(String url, HttpMethod methodType, AuthenticationRequest authenticationRequest) {

		this.authenticationRequest = authenticationRequest;

		return client.exchange(
		        TestApiConfig.getAbsolutePath(url),
		        methodType,
		        buildRequestHeaderEntity(),
		        String.class
		        );
	}

	/*@Test
	public void getUserInvalidCredentials() {

		this.initValidUserWithInvalidRole();

		try {
			client.exchange(
			        TestApiConfig.getAbsolutePath("user"),
			        HttpMethod.GET,
			        buildProtectedRequestEntity(),
			        String.class
			        );

		} catch (HttpClientErrorException e) {

			assertThat(e.getStatusCode(), is(HttpStatus.UNAUTHORIZED));

			//fail("Should have returned an HTTP 400: Ok status code");
		} catch (Exception e) {
			fail("Should have returned an HTTP 401: Ok status code");
		}

	}*/

	private void initializeStateForMakingValidProtectedRequest() {
		authenticationRequest = TestApiConfig.ADMIN_AUTHENTICATION_REQUEST;

		ResponseEntity<AuthenticationResponse> authenticationResponse = client.postForEntity(
		        TestApiConfig.getAbsolutePath(authenticationRoute),
		        authenticationRequest,
		        AuthenticationResponse.class
		        );

		authenticationToken = authenticationResponse.getBody().getToken();
	}

	private void initValidUserWithValidRole() {
		authenticationRequest = TestApiConfig.ADMIN_AUTHENTICATION_REQUEST;

		ResponseEntity<AuthenticationResponse> authenticationResponse = client.postForEntity(
		        TestApiConfig.getAbsolutePath(authenticationRoute),
		        authenticationRequest,
		        AuthenticationResponse.class
		        );

		authenticationToken = authenticationResponse.getBody().getToken();
	}

	private void initValidUserWithInvalidRole() {
		authenticationRequest = TestApiConfig.USER_WITH_NO_ROLE;

		ResponseEntity<AuthenticationResponse> authenticationResponse = client.postForEntity(
		        TestApiConfig.getAbsolutePath(authenticationRoute),
		        authenticationRequest,
		        AuthenticationResponse.class
		        );

		authenticationToken = authenticationResponse.getBody().getToken();
	}

	private void initializeStateForMakingInvalidProtectedRequest() {
		authenticationRequest = TestApiConfig.USER_AUTHENTICATION_REQUEST;

		ResponseEntity<AuthenticationResponse> authenticationResponse = client.postForEntity(
		        TestApiConfig.getAbsolutePath(authenticationRoute),
		        authenticationRequest,
		        AuthenticationResponse.class
		        );

		authenticationToken = authenticationResponse.getBody().getToken();
	}

	private HttpEntity<Object> buildProtectedRequestEntity() {
		return RequestEntityBuilder.buildRequestEntityWithoutBody(authenticationToken);
	}

	private HttpEntity<Object> buildRequestHeaderEntity() {
		ResponseEntity<AuthenticationResponse> authenticationResponse = client.postForEntity(
		        TestApiConfig.getAbsolutePath(authenticationRoute),
		        authenticationRequest,
		        AuthenticationResponse.class
		        );

		authenticationToken = authenticationResponse.getBody().getToken();
		return RequestEntityBuilder.buildRequestEntityWithoutBody(authenticationToken);
	}

	private HttpEntity<Object> buildProtectedRequestEntityWithoutAuthorizationToken() {
		return RequestEntityBuilder.buildRequestEntityWithoutBodyOrAuthenticationToken();
	}

}
