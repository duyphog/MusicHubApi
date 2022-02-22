package com.aptech.provider;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aptech.constant.FileConstant;
import com.aptech.handle.exception.NotAnAudioFileException;
import com.aptech.handle.exception.NotAnImageFileException;

@Component
public class FileManager {

	public String uploadUserImage(Path pathUpload, String fileName, MultipartFile file)
			throws IOException, NotAnImageFileException {
		this.uploadImage(pathUpload, fileName, file);

		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(FileConstant.USER_IMAGE_PATH + fileName + "/" + fileName + "." + "jpg").toUriString();

		return imageUrl;
	}

	public String uploadAlbumImage(Path pathUpload, String fileName, MultipartFile file)
			throws IOException, NotAnImageFileException {
		this.uploadImage(pathUpload, fileName, file);

		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(FileConstant.ALBUM_IMAGE_PATH + fileName + "/" + fileName + "." + "jpg").toUriString();

		return imageUrl;
	}

	public String uploadSongImage(Path pathUpload, String fileName, MultipartFile file)
			throws IOException, NotAnImageFileException {
		this.uploadImage(pathUpload, fileName, file);

		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(FileConstant.TRACK_IMAGE_PATH + fileName + "/" + fileName + "." + "jpg").toUriString();

		return imageUrl;
	}

	private void uploadImage(Path pathUpload, String fileName, MultipartFile file)
			throws IOException, NotAnImageFileException {

		if (!Arrays.asList(MimeTypeUtils.IMAGE_JPEG_VALUE, MimeTypeUtils.IMAGE_GIF_VALUE, MimeTypeUtils.IMAGE_PNG_VALUE)
				.contains(file.getContentType())) {
			throw new NotAnImageFileException(file.getOriginalFilename() + " is not an image file (jpeg, png, gif)");
		}

		if (!Files.exists(pathUpload)) {
			Files.createDirectories(pathUpload);
		}

		Files.deleteIfExists(Paths.get(pathUpload + fileName + "." + "jpg"));

		Files.copy(file.getInputStream(), pathUpload.resolve(fileName + "." + "jpg"), REPLACE_EXISTING);
	}

	public String uploadAudioFile(Path pathUpload, String userName, String fileName, MultipartFile file) throws IOException, NotAnAudioFileException {
		
		if (!Arrays.asList("mp3", "wav", "m4a", "audio/mpeg").contains(file.getContentType())) {
			throw new NotAnAudioFileException(file.getOriginalFilename() + " is not an audio file (mp3, wav, m4a)");
		}
			
		if (!Files.exists(pathUpload)) {
			Files.createDirectories(pathUpload);
		}
		
		String extensionFile = getFileExtension(file.getOriginalFilename());
		
		Files.deleteIfExists(Paths.get(pathUpload + fileName + "." + extensionFile));

		Files.copy(file.getInputStream(), pathUpload.resolve(fileName + "." + extensionFile), REPLACE_EXISTING);
		
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(FileConstant.TRACK_PATH + "/" + fileName + "." + extensionFile).toUriString();
		
		return imageUrl;
	}

	private String getFileExtension(String fileName) {
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}
}
