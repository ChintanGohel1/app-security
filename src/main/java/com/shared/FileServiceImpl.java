package com.shared;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.exceptions.EmptyFileException;

@Component
public class FileServiceImpl implements FileService {
	@Autowired
	private ResourceLoader resourceLoader;

	private static final Logger log = Logger.getLogger(FileServiceImpl.class);

	private static final StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;

	public static String uploadRootDir;

	@Value("${server.uploadRootDir}")
	public void setUploadRootDir(String uploadRootDir) {
		FileServiceImpl.uploadRootDir = uploadRootDir;
	}

	private Resource getResource(String location) {
		return resourceLoader.getResource(location);
	}

	@Override
	public void uploadFile(InputStream fileInputStream, String uploadLocation, String fileName) throws IOException {

		Path path = Paths.get(uploadLocation, fileName);

		Files.copy(fileInputStream, path, copyOption);

		log.info(fileName + " copied to " + uploadLocation);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shared.FileService#uploadFile(org.springframework.web.multipart.
	 * MultipartFile, java.lang.String, java.lang.String)
	 */
	@Override
	public void uploadFile(List<MultipartFile> files, String uploadLocation) throws IOException {

		Path uploadDir = Paths.get(uploadRootDir, uploadLocation);
		if (!StringUtils.isEmpty(uploadLocation)) {
			File dir = new File(uploadDir.toString());
			if (!dir.exists())
				dir.mkdirs();
		}

		if (null == files || files.isEmpty()) {
			log.info("file(s) not found");
			throw new EmptyFileException("file(s) not found");
		}

		for (MultipartFile file : files) {
			if (!file.isEmpty()) {

				if (!StringUtils.isEmpty(file.getOriginalFilename())) {
					uploadFile(file.getInputStream(), uploadDir.toString(), file.getOriginalFilename());
				}

			} else {

				log.error("Exeption while upload file " + file.getOriginalFilename() + " : File dont have any content");
				throw new EmptyFileException("file[" + file.getOriginalFilename() + "] dont have any content");

			}
		}

	}

	@Override
	public Resource getResource(String fileUrlPrefix, String filePath, boolean isAbsolutePath) throws IOException {

		if (!StringUtils.isEmpty(fileUrlPrefix) && !StringUtils.isEmpty(filePath)) {

			if (ResourceUtils.CLASSPATH_URL_PREFIX.equals(fileUrlPrefix)) {

				return getResource(fileUrlPrefix + filePath);

			} else {

				File file = new File(isAbsolutePath ? filePath : Paths.get(uploadRootDir, filePath).toString());

				if (null != file && file.exists() && !file.isDirectory()) {
					return getResource(fileUrlPrefix + file.getAbsolutePath());
				}

			}
		}
		throw new FileNotFoundException();

	}
}