package com.aptech.service;

import java.util.concurrent.ExecutionException;

public interface ILoginAttemptService {
	void addUserToLoginAttemptCache(String username);
	void evictUserFromLoginAttemptCache(String username);
	boolean hasExceededMaxAttempts(String username) throws ExecutionException;
}
