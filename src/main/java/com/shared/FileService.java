package com.shared;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Vinit Solanki
 *
 */
public interface FileService
{

	//Resource getResource(String location);

	void uploadFile(InputStream fileInputStream, String uploadLocation, String fileName) throws IOException;

	void uploadFile(List<MultipartFile> files, String uploadLocation) throws IOException;

	Resource getResource(String fileUrlPrefix, String path, boolean isAbsolutePath) throws FileNotFoundException, IOException;

}
