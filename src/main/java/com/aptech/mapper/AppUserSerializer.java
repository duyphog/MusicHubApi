package com.aptech.mapper;

import java.io.IOException;

import com.aptech.dto.UserRegister;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AppUserSerializer extends StdSerializer<UserRegister> {

	private static final long serialVersionUID = 1L;

	public AppUserSerializer() {
		this(null);
	}

	public AppUserSerializer(Class<UserRegister> clazz) {
		super(clazz);
	}

	@Override
	public void serialize(UserRegister value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		jgen.writeStartObject();

		jgen.writeStringField("username", value.getUsername());
		jgen.writeStringField("email", value.getEmail());

		jgen.writeEndObject();
	}
}
