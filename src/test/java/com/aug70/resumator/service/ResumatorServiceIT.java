package com.aug70.resumator.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.aug70.resumator.ResumatorApplicationIT;
import com.aug70.resumator.TestUtil;
import com.aug70.resumator.model.ConvertedFile;
import com.aug70.resumator.model.UploadedFile;

public class ResumatorServiceIT extends ResumatorApplicationIT {

	@Resource
	private MdConversionService service;
	
	@Test
	public void zipperTest() throws Exception {
		
		
		Supplier<Future<ConvertedFile[]>> supplier = () -> TestUtil.sampleConvertedFuture();
		List<Future<ConvertedFile[]>> list = Stream.generate(supplier).limit(50).collect(Collectors.toList());
		try (ByteArrayOutputStream out = service.zipAndReturn(list))
		{
			Assert.assertTrue(out.size()>0);
		}
	}
	
	@Test
	public void serviceTest() throws Exception  {
		UploadedFile upload = new UploadedFile("name.md", null, TestUtil.sampleFile());
		try (ByteArrayOutputStream out = service.convertAll(upload))
		{
			Assert.assertTrue(out.size()>0);
		}
	}
	
	
}