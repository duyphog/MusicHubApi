package com.aptech.domain;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaFile {
	private int id;
	FileType type;
	private String path;

	public File getFile() {
		return new File(path);
	}
}
