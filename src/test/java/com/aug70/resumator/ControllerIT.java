package com.aug70.resumator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ControllerIT {

	private MockMvc mockMvc;
	
	@Autowired
	protected WebApplicationContext webApplicationContext;

	@Before
    public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void testIndexPage() throws Exception {
		mockMvc.perform(
				get("/").accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testUpload() throws Exception {
		
		MockMultipartFile sample = new MockMultipartFile("data", "sample.md", "text/plain", TestUtil.sampleStream());

		mockMvc.perform(
				MockMvcRequestBuilders
				.multipart("/go")
		        .file(sample)
		        .param("targetName", "some name").accept(MediaType.APPLICATION_OCTET_STREAM_VALUE))
          .andExpect(status().isCreated())
          .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
	}
	
}