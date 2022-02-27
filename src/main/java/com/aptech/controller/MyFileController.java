package com.aptech.controller;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.provider.FileManager;
import com.aptech.service.ITrackService;

@RestController
@RequestMapping("/myfile")
public class MyFileController {

	private ITrackService trackService;

	@Autowired
	public MyFileController(ITrackService trackService, FileManager fileManager) {
		this.trackService = trackService;
	}

	@GetMapping(path = "/images/{fileName}", produces = IMAGE_JPEG_VALUE)
	public byte[] getImageFile(
			@PathVariable("fileName") String fileName) throws IOException, NoSuchFileException {

		return Files.readAllBytes(
				Paths.get(com.aptech.provider.file.FileConstant.IMAGE_FOLDER + fileName));
	}
	
//	@RequestMapping(path = "/songs/{fileName}", method=RequestMethod.GET)
//	public void getDownload(
//			HttpServletResponse response,
//			@PathVariable("fileName") String fileName
//		) throws IOException {
//		
//		trackService.listenedTrack(1000L);
//		
//		File initialFile = new File(FileConstant.TRACK_FOLDER + "/" + fileName);
//		
//		// Get your file stream from wherever.
//		InputStream myStream = new FileInputStream(initialFile);
//
//		// Set the content type and attachment header.
//		response.addHeader("Content-disposition", "attachment;filename=" + fileName);
//		response.setContentType("audio/mpeg3");
//
//		// Copy the stream to the response's output stream.
//		IOUtils.copy(myStream, response.getOutputStream());
//		response.flushBuffer();
//	}
}
