package com.aptech.constant;

public class FileConstant {
    // http://localhost:8080/api/user/image/profile/{username}  => https://robohash.org/{username}
    public static final String DEFAULT_USER_IMAGE_PATH = "/user/image/profile/";

    // http://localhost:8080/api/user/image/{username}/{fileName.jpg} => /user/ray/springAngularEcommerce/users/{username}/{fileName.jpg}
    public static final String USER_IMAGE_PATH = "/user/image/";

    public static final String TEMP_PROFILE_IMAGE_BASE_URL = "https://robohash.org/";

    // /user/ray/springAngularEcommerce2/users/
    public static final String USER_FOLDER = System.getProperty("user.home") + "/Documents/MyDoc/Aptech/Aptech/springboot/e-project/static-file/users/";
}
