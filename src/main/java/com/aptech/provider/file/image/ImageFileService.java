package com.aptech.provider.file.image;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.provider.file.FileConstant;
import com.aptech.provider.file.IFileService;
import com.aptech.provider.file.MediaFile;
import com.aptech.provider.file.UnsupportedFileTypeException;
import com.aptech.provider.file.util.DateUtil;
import com.aptech.provider.file.util.FileUtil;
import com.aptech.util.StringUtil;

@Service
public final class ImageFileService implements IFileService {
	private final String imageExtensionSave = ".jpg";

	private final String[] mimeTypeSupport = { MimeTypeUtils.IMAGE_JPEG_VALUE, MimeTypeUtils.IMAGE_GIF_VALUE,
			MimeTypeUtils.IMAGE_PNG_VALUE };

	@Autowired
	public ImageFileService() {

	}

	@Override
	public MediaFile upload(String fileName, MultipartFile file) throws IOException, UnsupportedFileTypeException {

		if (!Arrays.asList(mimeTypeSupport).contains(file.getContentType())) {
			throw new UnsupportedFileTypeException(
					file.getOriginalFilename() + " is not an image file: [" + String.join("; ", mimeTypeSupport) + "]");
		}

		Path imageFolder = Paths.get(FileConstant.IMAGE_FOLDER).toAbsolutePath().normalize();

		if (!Files.exists(imageFolder)) {
			Files.createDirectories(imageFolder);
		}

		fileName = StringUtil.normalizeUri(fileName) + "-" + DateUtil.GetCurrentTimeMillis() + imageExtensionSave;

		Files.deleteIfExists(Paths.get(imageFolder + fileName));
		Files.copy(file.getInputStream(), imageFolder.resolve(fileName), REPLACE_EXISTING);

		return new MediaFile(imageFolder + "/" + fileName, FileConstant.USER_URL_PATH + fileName);
	}

	@Override
	public Boolean remove(String pathFile) {
		return FileUtil.deleteFile(pathFile);
	}
}
