package com.aptech.constant;

public class FileConstant {
    private static final String IMAGE_FOLDER = System.getProperty("user.home") + "/Documents/MyDoc/Aptech/Aptech/springboot/e-project/static-file/images/";
    
    public static String IMAGE_FOLDER_DEFAULT = IMAGE_FOLDER;
    public static final String USER_IMAGE_FOLDER = IMAGE_FOLDER + "users/";
    public static final String ALBUM_IMGAGE_FOLDER = IMAGE_FOLDER + "albums/";
    
    public static final String TEMP_PROFILE_IMAGE_BASE_URL = "https://robohash.org/";
    public static final String DEFAULT_USER_MOCK_IMAGE_PATH = "/user/image/profile/";
    
    // Path
    public static final String USER_IMAGE_PATH = "/myfile/images/users/";
    public static final String ALBUM_IMAGE_PATH = "/myfile/images/albums/";
}
