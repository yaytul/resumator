package com.aug70.resumator.web;

import java.io.ByteArrayOutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aug70.resumator.model.UploadedFile;
import com.aug70.resumator.service.ConverterService;

@RestController
@RequestMapping("/go")
public class Controller {

	private ConverterService service = new ConverterService();
	
	@PostMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> upload(@RequestParam("data") MultipartFile uploadFile, 
			@RequestParam(name="targetName", required=false) String targetName) {

		
		try (ByteArrayOutputStream out = service
				.convertAll(new UploadedFile(uploadFile.getName(), targetName, uploadFile.getBytes()))) {
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"converted_files.zip\"");
			headers.setContentLength(out.size());
			return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);			
			
			
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}

