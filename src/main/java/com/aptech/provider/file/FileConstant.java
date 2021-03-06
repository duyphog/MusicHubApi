package com.aptech.provider.file;

public class FileConstant {
	
	private static final String BASE_FOLDER = System.getProperty("user.home") + "/Documents/MyDoc/Aptech/Aptech/springboot/e-project/static-file";
	public static final String IMAGE_FOLDER = BASE_FOLDER + "/images/";
	public static final String TRACK_FOLDER = BASE_FOLDER + "/tracks/";

	public static final String TEMP_PROFILE_IMAGE_BASE_URL = "https://robohash.org/";

	// Path
	public static final String USER_URL_PATH = "/myfile/images/";
	public static final String TRACK_URL_PATH = "/myfile/tracks/";
}
