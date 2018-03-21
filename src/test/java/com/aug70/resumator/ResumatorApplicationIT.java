package com.aug70.resumator;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResumatorApplicationIT {
	
	@Resource
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Test
	public void contextLoads() {
		
		Assert.assertEquals(3, threadPoolTaskExecutor.getCorePoolSize());
		Assert.assertEquals(4, threadPoolTaskExecutor.getMaxPoolSize());
	}

}