package com.aptech.provider.file.audio;

import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.provider.file.IFileService;
import com.aptech.provider.file.MediaFile;
import com.aptech.provider.file.UnsupportedFileTypeException;

@Service
public class AudioFileService implements IFileService {

	@Override
	public MediaFile upload(String fileName, MultipartFile file) throws IOException, UnsupportedFileTypeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean remove(String pathFile) {
		// TODO Auto-generated method stub
		return null;
	}
}
