package com.aug70.resumator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import com.aug70.resumator.model.ConvertedFile;

public class TestUtil {

	public static byte[] sampleFile() throws Exception {
		String sampleFile = "resume.md";
		Path path = Paths.get(TestUtil.class.getClassLoader().getResource(sampleFile).toURI());       
		return Files.readAllBytes(path);
	}
	
	public static InputStream sampleStream() throws Exception {
		return new ByteArrayInputStream(sampleFile());
	}

	public static Future<ConvertedFile[]> sampleConvertedFuture() throws Exception {
		byte[] bytes = sampleFile();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
		baos.write(bytes, 0, bytes.length);
		ConvertedFile[] array  = new ConvertedFile[] {new ConvertedFile("sample.txt", baos)};
		CompletableFuture<ConvertedFile[]> completableFuture = CompletableFuture.completedFuture(array);
		return completableFuture;
	}
}




