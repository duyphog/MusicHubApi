package com.aptech.provider.file;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	MediaFile upload(String fileName, MultipartFile file) throws IOException, UnsupportedFileTypeException;

	Boolean remove(String pathFile);

}
