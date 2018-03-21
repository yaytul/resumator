package com.aug70.resumator.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.aug70.resumator.model.ConvertedFile;
import com.aug70.resumator.model.UploadedFile;

class Md2HtmlPdf implements Converter {

	@Override
	public ConvertedFile[] convert(UploadedFile file) throws IOException {
		try(
			InputStream stream = file.getInputStream();
			Reader reader = new InputStreamReader(stream);
		) {
			String html = ConverterWrapper.toHtml(reader);
			ConvertedFile convertedHtml =  new ConvertedFile(file.getName() + ".html", html);
			ConvertedFile convertedPdf =  new ConvertedFile(file.getName() + ".pdf", ConverterWrapper.toPdf(html));
	        return new ConvertedFile[] {convertedHtml, convertedPdf};
		}
	}
	
}
