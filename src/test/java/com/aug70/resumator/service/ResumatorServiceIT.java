package com.aug70.resumator.service;

import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.aug70.resumator.ResumatorApplicationIT;
import com.aug70.resumator.TestUtil;
import com.aug70.resumator.model.UploadedFile;

public class ResumatorServiceIT extends ResumatorApplicationIT {

	private ConverterService service = new ConverterService();

	@Test
	public void serviceTest() throws Exception  {
		UploadedFile upload = new UploadedFile("name.md", null, TestUtil.sampleFile());
		try (ByteArrayOutputStream out = service.convertAll(upload))
		{
			Assert.assertTrue(out.size()>0);
		}
	}
	
	
}