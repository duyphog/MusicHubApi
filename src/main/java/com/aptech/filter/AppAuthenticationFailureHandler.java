package com.aptech.filter;

import java.io.OutputStream;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.aptech.domain.HttpResponse;
import com.aptech.domain.HttpResponseError;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AppAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		HttpResponse httpResponse = new HttpResponseError(HttpStatus.BAD_REQUEST,
				HttpStatus.BAD_REQUEST.getReasonPhrase(), "Login fail");

		response.setContentType("application/json");
		response.setStatus(HttpStatus.BAD_REQUEST.value());

		OutputStream outputStream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(outputStream, httpResponse);
		outputStream.flush();
	}

}
