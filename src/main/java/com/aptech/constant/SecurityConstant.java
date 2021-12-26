package com.aptech.constant;

public class SecurityConstant {
	public final static String COMPANY = "MusicHub";

	public final static String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
	
	public final static String AUTHORITIES = "authorities";
	
	public final static String OPTIONS_HTTP_METHOD ="OPTIONS";
	
	public final static String TOKEN_PREFIX = "Bearer ";
	
	public final static String APPLICATION_NAME = "MusicHub";
	
	public final static Long EXPIRATION_TIME = 432000000L;
	

	public final static String[] PUBLIC_URLS = { "/user/register", "/user/login", "/user/verify/**" };
	
	public final static String[] PUBLIC_GET_URLS = {};
}