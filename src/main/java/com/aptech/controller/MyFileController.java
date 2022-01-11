package com.aptech.controller;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.constant.FileConstant;
import com.aptech.provider.FileManager;

@RestController
@RequestMapping("/myfile")
public class MyFileController {

	private FileManager fileManager;

	@Autowired
	public MyFileController(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	@GetMapping(path = "/images/{category}/{filePath}/{fileName}", produces = IMAGE_JPEG_VALUE)
	public byte[] getImageFile(
			@PathVariable("category") String category, 
			@PathVariable("filePath") String filePath,
			@PathVariable("fileName") String fileName) throws IOException, NoSuchFileException {

		return Files.readAllBytes(
				Paths.get(FileConstant.IMAGE_FOLDER_DEFAULT + category + "/" + filePath + "/" + fileName));
	}
}
