package com.aug70.resumator.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aug70.resumator.model.ConvertedFile;
import com.aug70.resumator.model.UploadedFile;

public class ConverterService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConverterService.class);
	
	private Converter htmlpdfConverter = new Md2HtmlPdf();
	private Converter xmlConverter = new Md2Xml();
	private Converter docxConverter = new Md2Docx();

	public ByteArrayOutputStream convertAll(final UploadedFile uploadedFile) throws Exception {
		LOGGER.debug("Converting uploaded file.");
		Long start = System.currentTimeMillis();
		try (
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
				ZipOutputStream zipOut = new ZipOutputStream(bufferedOutputStream);
			) {
			
			Stream.of(
					CompletableFuture.supplyAsync(() -> htmlpdfConverter.convert(uploadedFile)),
					CompletableFuture.supplyAsync(() -> xmlConverter.convert(uploadedFile)),
					CompletableFuture.supplyAsync(() -> docxConverter.convert(uploadedFile))
					)
				.map(CompletableFuture::join)
				.flatMap(s->s)
				.forEach(convertedFile -> addToZip(zipOut, convertedFile));
			zipOut.flush();
			LOGGER.info(String.format("Finished in %s ms.", System.currentTimeMillis() - start));
			return byteArrayOutputStream;
		}
	}
	
	private void addToZip(ZipOutputStream zipOut, ConvertedFile file) {
		ZipEntry zipEntry = new ZipEntry(file.getName());
		try {
			zipOut.putNextEntry(zipEntry);
			zipOut.write(file.getBytes());
		} catch (IOException e) {
			LOGGER.error("Exception while zipping.", e);
			throw new RuntimeException(e);
		}
	}
}