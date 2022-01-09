package com.aptech.provider;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aptech.constant.FileConstant;

@Component
public class FileManager {

	public String uploadImage(Path path, String fileName, MultipartFile file) throws IOException  {

		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}

		Files.deleteIfExists(Paths.get(path + fileName + "." + "jpg"));

		Files.copy(file.getInputStream(), path.resolve(fileName + "." + "jpg"), REPLACE_EXISTING);

		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(FileConstant.USER_IMAGE_PATH + fileName + "/" + fileName + "." + "jpg").toUriString();

		return imageUrl;
	}
}
