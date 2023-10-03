package com.project.webapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebappApplicationTests {
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void testHealthz() {
		String url = "http://localhost:"+port+"/healthz";
		ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(url,String.class);
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}

}
