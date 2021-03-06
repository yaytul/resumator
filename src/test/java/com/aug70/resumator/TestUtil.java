package com.aug70.resumator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import com.aug70.resumator.model.ConvertedFile;

public class TestUtil {

	public static byte[] sampleFile() throws Exception {
		String sampleFile = "sample.md";
		Path path = Paths.get(TestUtil.class.getClassLoader().getResource(sampleFile).toURI());       
		return Files.readAllBytes(path);
	}
	
	public static InputStream sampleStream() throws Exception {
		return new ByteArrayInputStream(sampleFile());
	}

	public static Future<Optional<ConvertedFile[]>> sampleConvertedFuture() {
		try {
			byte[] bytes = sampleFile();
			ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
			baos.write(bytes, 0, bytes.length);
			ConvertedFile[] array  = new ConvertedFile[] {new ConvertedFile(UUID.randomUUID().toString(), baos)};
			return CompletableFuture.completedFuture(Optional.of(array));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}




