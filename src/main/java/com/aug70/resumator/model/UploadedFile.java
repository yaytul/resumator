package com.aug70.resumator.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class UploadedFile {

	private final byte[] content;
	private String name;
	
	public UploadedFile(final String name, final String targetName, final byte[] content) throws IOException {
		this.name = targetName != null ? targetName : name;
		this.content = content;
	}
	
	public final InputStream getInputStream() {
		return new ByteArrayInputStream(content);
	}

	public final String getName() {
		return name;
	}	
	
}