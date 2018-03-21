package com.aug70.resumator.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aug70.resumator.model.UploadedFile;
import com.aug70.resumator.service.MdConversionService;

@RestController
@RequestMapping("/go")
public class Controller {

	@Resource
	private MdConversionService service;
	
	@PostMapping(produces = "application/zip")
	public void upload(@RequestParam("data") MultipartFile uploadFile, @RequestParam(name="targetName", required=false) String targetName, HttpServletResponse response) throws IOException {

		try (ByteArrayOutputStream out = service.convertAll(
				new UploadedFile(uploadFile.getName(), targetName, uploadFile.getBytes()))) {
			response.setStatus(HttpStatus.CREATED.value());
			response.setContentType("application/zip");
			response.addHeader("Content-Disposition", "attachment; filename=\"converted_files.zip\"");
			out.writeTo(response.getOutputStream());
		} catch (Exception ex) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

	}
}

