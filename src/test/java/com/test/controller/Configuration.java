package com.test.controller;

import java.io.File;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import com.Application;
import com.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.entities.AuthenticationRequest;
import com.test.util.CustomRestTemplate;
import com.test.util.FileUtil;
import com.test.util.RequestEntityBuilder;
import com.test.util.TestApiConfig;
import com.util.PropertiesUtil;

/**
 * @author Vinit Solanki
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
//@TestPropertySource(locations = "classpath:test.properties")
//@SqlGroup({
//        @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:createUser.sql")
//})
public class Configuration {

	protected CustomRestTemplate client;
	protected AuthenticationRequest authenticationRequest;
	//public static String authenticationToken;
	protected String authenticationRoute = "login";

	@Autowired
	public PropertiesUtil propertiesUtil;

	@Autowired
	public ObjectMapper objectMapper;

	@Before
	public void setUp() throws Exception {

		System.out.println("setUp");

		this.client = new CustomRestTemplate();
		this.propertiesUtil.setProprtyFile("ValidationMessages.properties");

		if (StringUtils.isEmpty(this.client.authenticationToken))
			initValidUserWithValidRole();

		File testingFilesFolder = new File(TestApiConfig.TESTING_CLIENT_DIRECTORY);
		testingFilesFolder.delete();
		testingFilesFolder.mkdir();

	}

	@After
	public void tearDown() throws Exception {

		client = null;
		FileUtil.emptyFolder().apply(TestApiConfig.TESTING_CLIENT_DIRECTORY);
		FileUtil.emptyFolder().apply(Paths.get(FileControllerTest.uploadRootDir, TestApiConfig.TESTING_SERVER_DIRECTORY).toString());

	}

	@Test
	public void dummyTest() {

	}

	protected void initValidUserWithValidRole() {

		try {
			authenticationRequest = TestApiConfig.ADMIN_AUTHENTICATION_REQUEST;

			ResponseEntity<UserDTO> authenticationResponse = client.postForEntity(
			        authenticationRoute,
			        authenticationRequest,
			        UserDTO.class
			        );

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	protected HttpEntity<Object> buildAuthenticationRequestEntity() {
	//		return RequestEntityBuilder.buildRequestEntityWithoutAuthenticationToken(authenticationRequest);
	//	}

	/**
	 * @param list
	 * @return
	 * @return
	 */
	protected HttpEntity<Object> buildHeaderWithToken() {
		System.out.println("client.authenticationToken = " + client.authenticationToken);
		return RequestEntityBuilder.buildRequestEntityWithoutBody(this.client.authenticationToken);
	}

	protected HttpEntity<Object> buildHeaderWithToken(Object bodyObject) {
		System.out.println("client.authenticationToken = " + client.authenticationToken);
		return RequestEntityBuilder.buildRequestEntity(this.client.authenticationToken, bodyObject);
	}

	private HttpEntity<Object> buildHeaderWithoutToken() {
		return RequestEntityBuilder.buildRequestEntityWithoutBodyOrAuthenticationToken();
	}

}
