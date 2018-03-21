package com.aug70.resumator.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.aug70.resumator.model.ConvertedFile;
import com.aug70.resumator.model.UploadedFile;

@Service
public class MdConversionService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MdConversionService.class);

	@Resource
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	public ByteArrayOutputStream convertAll(final UploadedFile uploadedFile) throws Exception {
		LOGGER.debug("Converting uploaded file.");
		Stream<Future<ConvertedFile[]>> files = Stream.of(
				threadPoolTaskExecutor.submit(new ConverterTask(new Md2HtmlPdf(), uploadedFile)),
				threadPoolTaskExecutor.submit(new ConverterTask(new Md2Xml(), uploadedFile)),
				threadPoolTaskExecutor.submit(new ConverterTask(new Md2Docx(), uploadedFile)));
		
		return zipAndReturn(files);
		
	}
	
	final ByteArrayOutputStream zipAndReturn(Stream<Future<ConvertedFile[]>> stream) throws Exception {
        try(
        		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        		ZipOutputStream zipOut = new ZipOutputStream(bufferedOutputStream);
        	) {
        		Long start = System.currentTimeMillis();
		    stream.forEach(fileArray -> addToZip(zipOut, fileArray));
		    zipOut.flush();
		    LOGGER.info(String.format("Finished in %s ms.", System.currentTimeMillis() - start));
	        return byteArrayOutputStream;
	       }
	}
	
	private void addToZip(ZipOutputStream zipOut, Future<ConvertedFile[]> convertedFileArray) {
		try {
			ConvertedFile[] fileArray = convertedFileArray.get();
			for(ConvertedFile file : fileArray) {
				ZipEntry zipEntry = new ZipEntry(file.getName());
				zipOut.putNextEntry(zipEntry);
				zipOut.write(file.getBytes());
			}
		} catch (InterruptedException | ExecutionException | IOException e) {
			LOGGER.error("Exception while zipping.", e);
			throw new RuntimeException(e);
		}
	}
}

class ConverterTask implements Callable<ConvertedFile[]> {

	private final Converter converter;
	private final UploadedFile uploadFile;

	public ConverterTask(final Converter converter, final UploadedFile uploadFile) {
		this.uploadFile = uploadFile;
		this.converter = converter;
	}

	@Override
	public ConvertedFile[] call() throws Exception {
		return converter.convert(uploadFile);
	}
}