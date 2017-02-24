package com.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import com.exceptions.ValidationError;
import com.shared.FileService;

@RestController
@RequestMapping(value = "file")
public class FileController {

	private static final Logger log = Logger.getLogger(FileController.class);

	@Autowired
	private FileService fileService;

	@RequestMapping(method = RequestMethod.GET, value = "/**")
	@ResponseBody
	public ResponseEntity<?> getFile(HttpServletRequest request) {

		Object uriObject = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		if (null != uriObject) {

			String relativePath = uriObject.toString().replaceFirst("^/file/", "");
			Resource file;
			try {
				file = fileService.getResource(ResourceUtils.FILE_URL_PREFIX, relativePath, false);
				return ResponseEntity.ok(file);
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ValidationError("File [" + relativePath + "] not found )"));
			} 

		}

		return ResponseEntity.notFound().build();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> uploadFile(@RequestParam("file") List<MultipartFile> files,
			@RequestParam(value = "uploadPath", defaultValue = "") String uploadPath) {

		try {

			fileService.uploadFile(files, uploadPath);
			return ResponseEntity.ok().build();

		} catch (IOException e) {
			e.printStackTrace();
			log.error("Exeption while upload file(s) | Exception  : " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ValidationError("file(s) not found"));
		}
	}

}