package com.test.controller;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.HttpServerErrorException;

import com.exceptions.ValidationError;
import com.test.util.TestApiConfig;
import com.shared.FileService;

/**
 * @author Vinit Solanki
 *
 */
public class FileControllerTest extends Configuration {

	private static final Logger log = Logger.getLogger(FileControllerTest.class);

	@Autowired
	private FileService fileService;

	private final String fileName = TestApiConfig.TESTING_CLIENT_DIRECTORY + "dump-" + System.currentTimeMillis() + ".txt";

	public static String uploadRootDir;

	@Value("${server.uploadRootDir}")
	public void setUploadRootDir(String uploadRootDir) {
		FileControllerTest.uploadRootDir = uploadRootDir;
	}

	@Test
	public void uploadFileWithOutFileTest() throws IOException {

		try {

			HttpEntity<MultiValueMap<String, Object>> request = createRequest(null, null);

			ResponseEntity<String> response = client.postForEntity("file", request, String.class);

			assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));

		} catch (HttpServerErrorException e) {

			ValidationError responseErrors = objectMapper.readValue(e.getResponseBodyAsString(), ValidationError.class);
			fail("Should have returned an HTTP : " + HttpStatus.OK + " but getting Status : " + e.getStatusCode() + " | Message : " + responseErrors.getErrorMessage() + " | " + responseErrors.getErrors());

		}

	}

	@Test
	public void uploadBlankFileTest() throws IOException {

		try (PrintStream out = new PrintStream(fileName)) {
			out.print("");
		}

		try {

			Resource resourceFile = fileService.getResource(ResourceUtils.FILE_URL_PREFIX, new File(fileName).getAbsolutePath(), true);

			HttpEntity<MultiValueMap<String, Object>> request = createRequest(TestApiConfig.TESTING_SERVER_DIRECTORY, resourceFile);

			ResponseEntity<String> response = client.postForEntity("file", request, String.class);

			assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));

		} catch (HttpServerErrorException e) {

			ValidationError responseErrors = objectMapper.readValue(e.getResponseBodyAsString(), ValidationError.class);
			fail("Should have returned an HTTP : " + HttpStatus.NO_CONTENT + " but getting Status : " + e.getStatusCode() + " | Message : " + responseErrors.getErrorMessage() + " | " + responseErrors.getErrors());

		}
	}

	@Test
	public void uploadSingleFileTest() throws IOException {

		try (PrintStream out = new PrintStream(fileName)) {

			out.print("This is txt file");
		}

		try {

			Resource resourceFile = fileService.getResource(ResourceUtils.FILE_URL_PREFIX, new File(fileName).getAbsolutePath(), true);

			HttpEntity<MultiValueMap<String, Object>> request = createRequest(TestApiConfig.TESTING_SERVER_DIRECTORY, resourceFile);

			ResponseEntity<String> response = client.postForEntity("file", request, String.class);

			assertThat(response.getStatusCode(), is(HttpStatus.OK));

		} catch (HttpServerErrorException e) {

			ValidationError responseErrors = objectMapper.readValue(e.getResponseBodyAsString(), ValidationError.class);
			fail("Should have returned an HTTP : " + HttpStatus.OK + " but getting Status : " + e.getStatusCode() + " | Message : " + responseErrors.getErrorMessage() + " | " + responseErrors.getErrors());

		}

	}

	@Test
	public void uploadMultipleFileTest() throws IOException {

		Resource[] resourceFiles = new Resource[10];
		IntStream.range(1, 10).forEach(i -> {
			try {

				String fileName = TestApiConfig.TESTING_CLIENT_DIRECTORY + "dump-" + i + ".txt";
				PrintWriter writer = new PrintWriter(fileName);
				writer.println("The first line");
				writer.close();

				resourceFiles[i] = fileService.getResource(ResourceUtils.FILE_URL_PREFIX, new File(fileName).getAbsolutePath(), true);

			} catch (Exception e) {
				log.error("Exception while creating temp file ");
			}

		});

		try {

			HttpEntity<MultiValueMap<String, Object>> request = createRequest(TestApiConfig.TESTING_SERVER_DIRECTORY, resourceFiles);

			ResponseEntity<String> response = client.postForEntity("file", request, String.class);

			assertThat(response.getStatusCode(), is(HttpStatus.OK));

		} catch (HttpServerErrorException e) {

			ValidationError responseErrors = objectMapper.readValue(e.getResponseBodyAsString(), ValidationError.class);
			fail("Should have returned an HTTP : " + HttpStatus.OK + " but getting Status : " + e.getStatusCode() + " | Message : " + responseErrors.getErrorMessage() + " | " + responseErrors.getErrors());

		}

	}

	/**
	 * @param string
	 * @param resourceFile
	 * @return HttpEntity<MultiValueMap<String, Object>>
	 */
	private HttpEntity<MultiValueMap<String, Object>> createRequest(String folderName, Resource... resourceFiles) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("uploadPath", folderName);

		if (resourceFiles != null && resourceFiles.length > 0)
			for (Resource res : resourceFiles) {
				map.add("file", res);
			}

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
		return request;

	}

}
