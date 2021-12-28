package com.aptech.mapper;

import java.io.IOException;

import com.aptech.entity.AppUser;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class UserDtoSerializer extends StdSerializer<AppUser> {

	private static final long serialVersionUID = 1L;

	public UserDtoSerializer() {
		this(null);
	}

	public UserDtoSerializer(Class<AppUser> clazz) {
		super(clazz);
	}

	@Override
	public void serialize(AppUser value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		jgen.writeStartObject();

		jgen.writeNumberField("userId", value.getId());
		jgen.writeStringField("username", value.getUsername());
		jgen.writeStringField("email", value.getEmail());
		
		jgen.writeStringField("firstName", value.getUserInfo() != null ? value.getUserInfo().getFirstName() : null);
		jgen.writeStringField("lastName", value.getUserInfo() != null ? value.getUserInfo().getFirstName() : null);
		jgen.writeStringField("avatarImg", value.getUserInfo() != null ? value.getUserInfo().getAvatarImg() : null);
		jgen.writeStringField("story", value.getUserInfo() != null ?  value.getUserInfo().getStory() : null);
		
		jgen.writeEndObject();
	}
}
