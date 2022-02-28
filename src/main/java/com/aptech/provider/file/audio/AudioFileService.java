package com.aptech.provider.file.audio;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.aptech.provider.file.FileConstant;
import com.aptech.provider.file.IFileService;
import com.aptech.provider.file.MediaFile;
import com.aptech.provider.file.UnsupportedFileTypeException;
import com.aptech.provider.file.util.DateUtil;
import com.aptech.provider.file.util.FileUtil;
import com.aptech.provider.file.util.StringUtil;

@Service
public class AudioFileService implements IFileService {

	private final String[] mimeTypeSupport = { "mp3", "wav", "m4a", "audio/mpeg" };

	public AudioFileService() {

	}

	@Override
	public MediaFile upload(String fileName, MultipartFile file) throws IOException, UnsupportedFileTypeException {
		if (!Arrays.asList(mimeTypeSupport).contains(file.getContentType())) {
			throw new UnsupportedFileTypeException(
					file.getOriginalFilename() + " is not an audio file [" + String.join("; ", mimeTypeSupport) + "]");
		}

		Path imageFolder = Paths.get(FileConstant.TRACK_FOLDER).toAbsolutePath().normalize();

		if (!Files.exists(imageFolder)) {
			Files.createDirectories(imageFolder);
		}

		fileName = StringUtil.normalizeUri(fileName) + "-" + DateUtil.GetCurrentTimeMillis() + "."
				+ FileUtil.getFileExtension(file.getOriginalFilename());

		Files.deleteIfExists(Paths.get(imageFolder + fileName));
		Files.copy(file.getInputStream(), imageFolder.resolve(fileName), REPLACE_EXISTING);

		return new MediaFile(imageFolder + "/" + fileName, FileConstant.TRACK_URL_PATH + fileName);
	}

	@Override
	public Boolean remove(String pathFile) {
		return FileUtil.deleteFile(pathFile);
	}
}
