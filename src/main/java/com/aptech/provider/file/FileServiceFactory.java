package com.aptech.provider.file;

import org.springframework.stereotype.Component;

import com.aptech.provider.file.audio.AudioFileService;
import com.aptech.provider.file.image.ImageFileService;

@Component
public class FileServiceFactory {
	private FileServiceFactory() {

	}

	public final static IFileService getFileService(FileType fileType) {
		switch (fileType) {
		case IMAGE:
			return new ImageFileService();

		case TRACK:
			return new AudioFileService();

		default:
			throw new IllegalArgumentException("This FileType is unsupported!");
		}
	}
}
