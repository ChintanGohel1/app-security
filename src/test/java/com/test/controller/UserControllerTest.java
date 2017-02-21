package com.test.controller;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.dto.CreateUserDTO;
import com.dto.RoleDTO;
import com.dto.UserDTO;
import com.exceptions.ValidationError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.test.util.TestApiConfig;

/**
 * @author Vinit Solanki
 *
 */
public class UserControllerTest extends Configuration {

	private static UserDTO userVO;

	private static final Logger log = Logger.getLogger(UserControllerTest.class);

	@Test
	public void getSingleUserTest() {

		ResponseEntity<String> responseEntity = client.exchange(
		        "user/1",
		        HttpMethod.GET,
		        this.buildHeaderWithToken(),
		        String.class
		        );

		String authenticationResponse = responseEntity.getBody();

		//		log.info("authenticationResponse = " + authenticationResponse);

		try {
			assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));

		} catch (Exception e) {
			fail("Should have returned an HTTP 400: Ok status code");
		}

	}

	@Test
	public void saveUserWithInvalidEmailTest() throws JsonParseException, JsonMappingException, IOException {

		CreateUserDTO user = new CreateUserDTO();
		user.setFirstName("Ravi");
		user.setLastName("Chaouhan");
		user.setEmail("worngemail");
		user.setPassword("password");
		user.setConfirmedPassword("password");
		user.setEnabled(Boolean.TRUE);

		try {

			ResponseEntity<String> responseEntity = client.exchange(
			        "user",
			        HttpMethod.POST,
			        this.buildHeaderWithToken(user),
			        String.class
			        );

		} catch (HttpClientErrorException e) {

			ValidationError responseErrors = objectMapper.readValue(e.getResponseBodyAsString(), ValidationError.class);
			assertThat(e.getStatusCode(), is(HttpStatus.BAD_REQUEST));
			assertThat(responseErrors.getErrors().get("email"), is(propertiesUtil.getValue("uservo.email.invalid.message")));

		} catch (Exception e) {
			fail("Should have returned an HTTP 400: Bad Request status code");

		}

	}

	@Test
	public void saveUserWithDuplicateEmailTest() throws JsonParseException, JsonMappingException, IOException {

		CreateUserDTO user = new CreateUserDTO();
		user.setFirstName("Diplicate Email");
		user.setLastName("Test");
		user.setEmail("vinit.solanki@indianic.com");
		user.setPassword("password");
		user.setConfirmedPassword("password");
		user.setEnabled(Boolean.TRUE);

		try {
			client.exchange(
			        "user",
			        HttpMethod.POST,
			        this.buildHeaderWithToken(user),
			        String.class
			        );

			fail("Should have returned an HTTP : " + HttpStatus.CONFLICT);

		} catch (HttpClientErrorException e) {

			assertThat(e.getStatusCode(), is(HttpStatus.CONFLICT));

			ValidationError responseErrors = objectMapper.readValue(e.getResponseBodyAsString(), ValidationError.class);

			assertThat(responseErrors.getErrors().get("email"), is(propertiesUtil.getValue("uservo.email.exist.message")));

		} catch (Exception e) {
			fail("Should have returned an HTTP 400: Bad Request status code");

		}

	}

	@Test
	public void saveUserCheckPasswordAndConfirmPasswordTest() throws JsonParseException, JsonMappingException, IOException {

		CreateUserDTO user = new CreateUserDTO();
		user.setFirstName("Ravi");
		user.setLastName("Chaouhan");
		user.setEmail("vinit.solanki@gmail.com");
		user.setPassword("password");
		user.setConfirmedPassword("otherpassword");
		user.setEnabled(Boolean.TRUE);

		try {
			client.exchange(
			        "user",
			        HttpMethod.POST,
			        this.buildHeaderWithToken(user),
			        String.class
			        );

			fail("Should have returned an HTTP : " + HttpStatus.BAD_REQUEST);

		} catch (HttpClientErrorException e) {

			ValidationError responseErrors = objectMapper.readValue(e.getResponseBodyAsString(), ValidationError.class);
			assertThat(e.getStatusCode(), is(HttpStatus.BAD_REQUEST));

			assertThat(responseErrors.getErrors().get("confirmedPassword"), is(propertiesUtil.getValue("uservo.confirmedPassword.notmatch.message")));

		} catch (Exception e) {
			fail("Should have returned an HTTP : " + HttpStatus.BAD_REQUEST);

		}

	}

	@Test
	public void saveUserTest() throws JsonParseException, JsonMappingException, IOException {

		CreateUserDTO user = new CreateUserDTO();
		user.setFirstName("New User");
		user.setLastName("Last Name");
		user.setEmail("unique@gmail.com");
		user.setPassword("admin");
		user.setConfirmedPassword("admin");
		user.setEnabled(Boolean.TRUE);
		//user.setRole(new RoleDTO(13L));

		try {
			ResponseEntity<UserDTO> responseEntity = client.exchange(
			        "user",
			        HttpMethod.POST,
			        this.buildHeaderWithToken(user),
			        UserDTO.class
			        );

			userVO = responseEntity.getBody();

			assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
			assertThat(userVO.getEmail(), is(user.getEmail()));

		} catch (HttpClientErrorException e) {

			ValidationError responseErrors = objectMapper.readValue(e.getResponseBodyAsString(), ValidationError.class);
			fail("Should have returned an HTTP : " + HttpStatus.OK + " but getting Status : " + e.getStatusCode() + " | Message : " + responseErrors.getErrorMessage() + " | " + responseErrors.getErrors());

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			System.out.println("exception = " + e);
			fail("Should have returned an HTTP : " + HttpStatus.OK + " | Message : " + e.getMessage());

		}

	}

	@Test
	public void updateUserTest() throws JsonParseException, JsonMappingException, IOException {

		assertNotNull("userVO Object should not null : check 'saveUserTest()'", userVO);

		userVO.setFirstName("VinitNew");
		userVO.setRole(new RoleDTO(1L));
		System.out.println("userVO = " + userVO);
		System.out.println("userVO.getRole() = " + userVO.getRole());

		try {
			ResponseEntity<UserDTO> responseEntity = client.exchange(
			        "user/" + userVO.getId(),
			        HttpMethod.PUT,
			        this.buildHeaderWithToken(userVO),
			        UserDTO.class
			        );

			UserDTO responseUserVO = responseEntity.getBody();

			System.out.println("responseUserVO = " + responseUserVO);

			assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
			assertThat(userVO.getFirstName(), is(responseUserVO.getFirstName()));
			assertThat(userVO.getRole().getId(), is(responseUserVO.getRole().getId()));

		} catch (HttpClientErrorException e) {

			ValidationError responseErrors = objectMapper.readValue(e.getResponseBodyAsString(), ValidationError.class);

			fail("Should have returned an HTTP : " + HttpStatus.OK + " but getting Status : " + e.getStatusCode() + " | Message : " + responseErrors.getErrorMessage() + " | " + responseErrors.getErrors());

		} catch (Exception e) {
			fail("Should have returned an HTTP : " + HttpStatus.OK + " | Exception : " + e.getMessage());

		}
	}

	@Test
	public void deleteUserTest() throws JsonParseException, JsonMappingException, IOException {

		try {
			ResponseEntity<UserDTO> responseEntity = client.exchange(
			        "user/" + userVO.getId(),
			        HttpMethod.DELETE,
			        this.buildHeaderWithToken(),
			        UserDTO.class
			        );

			assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));

		} catch (HttpClientErrorException e) {

			ValidationError responseErrors = objectMapper.readValue(e.getResponseBodyAsString(), ValidationError.class);

			fail("Should have returned an HTTP : " + HttpStatus.OK + " but getting Status : " + e.getStatusCode() + " | Message : " + responseErrors.getErrorMessage() + " | " + responseErrors.getErrors());

		} catch (Exception e) {

			fail("Should have returned an HTTP : " + HttpStatus.OK + " | Exception : " + e.getMessage());

		}

	}

}
