package com.test.util;

import com.test.entities.AuthenticationRequest;

public class TestApiConfig {

	public static final String HOSTNAME = "localhost";
	public static final String SERVER_CONTEXT = "";
	public static final Integer PORT = 8081;

	public static final AuthenticationRequest USER_AUTHENTICATION_REQUEST = new AuthenticationRequest("admin", "admin");
	public static final AuthenticationRequest ADMIN_AUTHENTICATION_REQUEST = new AuthenticationRequest("vinit.solanki@indianic.com", "admin");
	public static final AuthenticationRequest EXPIRED_AUTHENTICATION_REQUEST = new AuthenticationRequest("vinit", "admin");
	public static final AuthenticationRequest INVALID_AUTHENTICATION_REQUEST = new AuthenticationRequest("user", "abc123");
	public static final AuthenticationRequest USER_WITH_NO_ROLE = new AuthenticationRequest("ravi", "admin");
	public static final AuthenticationRequest DUMMY_USER = new AuthenticationRequest("wrong", "password");

	public static final String TESTING_CLIENT_DIRECTORY = "testing-client-files/";
	public static final String TESTING_SERVER_DIRECTORY = "testing-server-files/";

	public static String getAbsolutePath(String relativePath) {
		String url = String.format("http://%s:%d/%s/%s", HOSTNAME, PORT, SERVER_CONTEXT, relativePath);
		System.out.println("Called : " + url);
		return url;
	}

}
