package com.aug70.resumator.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aug70.resumator.model.ConvertedFile;
import com.aug70.resumator.model.UploadedFile;

class Md2HtmlPdf implements Converter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Md2HtmlPdf.class);

	@Override
	public ConvertedFile[] convert(UploadedFile file) throws IOException {
		long start = System.currentTimeMillis();
		try(
			InputStream stream = file.getInputStream();
			Reader reader = new InputStreamReader(stream);
		) {
			String html = ConverterWrapper.toHtml(reader);
			ConvertedFile convertedHtml =  new ConvertedFile(file.getName() + ".html", html);
			ConvertedFile convertedPdf =  new ConvertedFile(file.getName() + ".pdf", ConverterWrapper.toPdf(html));
			LOGGER.info(String.format("Html/pdf converted in %s ms.", System.currentTimeMillis() - start));
	        return new ConvertedFile[] {convertedHtml, convertedPdf};
		}
	}
}
