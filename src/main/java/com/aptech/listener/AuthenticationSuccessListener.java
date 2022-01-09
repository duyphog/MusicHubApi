package com.aptech.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.aptech.domain.AppUserDomain;
import com.aptech.service.ILoginAttemptService;

@Component
public class AuthenticationSuccessListener {
    private ILoginAttemptService loginAttemptService;

    @Autowired
    public AuthenticationSuccessListener(ILoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof AppUserDomain) {
        	AppUserDomain user = (AppUserDomain) event.getAuthentication().getPrincipal();
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }
}