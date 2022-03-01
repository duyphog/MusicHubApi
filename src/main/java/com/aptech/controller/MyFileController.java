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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.util.StringUtil;

import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.util.Optional;

@RestController
@RequestMapping("/myfile")
public class MyFileController {

	public static final int BYTE_RANGE = 128;

	@Autowired
	public MyFileController() {
	}

	@GetMapping(path = "/images/{fileName}", produces = IMAGE_JPEG_VALUE)
	public byte[] getImageFile(@PathVariable("fileName") String fileName) throws IOException, NoSuchFileException {

		return Files.readAllBytes(Paths.get(com.aptech.provider.file.FileConstant.IMAGE_FOLDER + fileName));
	}

	@RequestMapping(path = "/dowload/track/{fileName}", method = RequestMethod.GET)
	public void dowloadTrack(HttpServletResponse response, @PathVariable("fileName") String fileName)
			throws IOException {

		File initialFile = new File(com.aptech.provider.file.FileConstant.TRACK_FOLDER + fileName);

		// Get your file stream from wherever.
		InputStream myStream = new FileInputStream(initialFile);

		// Set the content type and attachment header.
		response.addHeader("Content-disposition", "attachment;filename=" + fileName);
		
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		response.setContentType(StringUtil.getMimeType(fileType));

		// Copy the stream to the response's output stream.
		IOUtils.copy(myStream, response.getOutputStream());
		response.flushBuffer();
	}

	@GetMapping("/tracks/{fileName}")
	public Mono<ResponseEntity<byte[]>> streamAudio(
			@RequestHeader(value = "Range", required = false) String httpRangeList,
			@PathVariable("fileName") String fileName) {
		return Mono
				.just(getContent(com.aptech.provider.file.FileConstant.TRACK_FOLDER, fileName, httpRangeList));
	}

	private ResponseEntity<byte[]> getContent(String location, String fileName, String range) {
		long rangeStart = 0;
		long rangeEnd;
		byte[] data;
		Long fileSize;
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		try {
			fileSize = Optional.ofNullable(fileName).map(file -> Paths.get(getFilePath(location), file))
					.map(this::sizeFromFile).orElse(0L);
			
			if (range == null) {
				return ResponseEntity.status(HttpStatus.OK).header("Content-Type", StringUtil.getMimeType(fileType))
						.header("Content-Length", String.valueOf(fileSize))
						.body(readByteRange(location, fileName, rangeStart, fileSize - 1));
			}
			
			String[] ranges = range.split("-");
			rangeStart = Long.parseLong(ranges[0].substring(6));
			if (ranges.length > 1) {
				rangeEnd = Long.parseLong(ranges[1]);
			} else {
				rangeEnd = fileSize - 1;
			}
			if (fileSize < rangeEnd) {
				rangeEnd = fileSize - 1;
			}
			data = readByteRange(location, fileName, rangeStart, rangeEnd);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.header("Content-Type", StringUtil.getMimeType(fileType)).header("Accept-Ranges", "bytes")
				.header("Content-Length", contentLength)
				.header("Content-Range", "bytes" + " " + rangeStart + "-" + rangeEnd + "/" + fileSize).body(data);
	}

	private byte[] readByteRange(String location, String filename, long start, long end) throws IOException {
		Path path = Paths.get(getFilePath(location), filename);
		try (InputStream inputStream = (Files.newInputStream(path));
				ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
			byte[] data = new byte[BYTE_RANGE];
			int nRead;
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				bufferedOutputStream.write(data, 0, nRead);
			}
			bufferedOutputStream.flush();
			byte[] result = new byte[(int) (end - start) + 1];
			System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
			return result;
		}
	}

	private String getFilePath(String pathFolder) {
		Path imageFolder = Paths.get(pathFolder).toAbsolutePath().normalize();
		return imageFolder.toString();
	}

	private Long sizeFromFile(Path path) {
		try {
			return Files.size(path);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return 0L;
	}
}
