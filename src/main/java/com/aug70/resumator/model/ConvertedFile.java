package com.aug70.resumator.model;

import java.io.ByteArrayOutputStream;

public final class ConvertedFile {
	
	private final byte[] bytes;
	private final String name;
	
	public ConvertedFile(String name, String content) {
		this.name = name;
		this.bytes = content.getBytes();
	}
	
	public ConvertedFile(String name, ByteArrayOutputStream baos) {
		this.bytes = baos.toByteArray();
		this.name = name;
	}
	
	public byte[] getBytes() {
		return bytes;
	}

	public String getName() {
		return name;
	}

}