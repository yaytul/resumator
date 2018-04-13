package com.aug70.resumator.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aug70.resumator.model.ConvertedFile;
import com.aug70.resumator.model.UploadedFile;

class Md2Docx implements Converter {

	private static final Logger LOGGER = LoggerFactory.getLogger(Md2Docx.class);
	
	@Override
	public Stream<ConvertedFile> convert(UploadedFile file) {
		long start = System.currentTimeMillis();
		try(
			InputStream stream = file.getInputStream();
			Reader reader = new InputStreamReader(stream);
			ByteArrayOutputStream baos = ConverterWrapper.toDocx(reader);
		) {
			LOGGER.info(String.format("Docx converted in %s ms.", System.currentTimeMillis() - start));
			return Stream.of(new ConvertedFile(file.getName() + ".docx", baos));
		} catch(IOException ioex) {
			return Stream.empty();
		}
	}
}
