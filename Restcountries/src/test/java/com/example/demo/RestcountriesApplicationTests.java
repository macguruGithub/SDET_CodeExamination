package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class RestcountriesApplicationTests {

	@LocalServerPort
    int randomServerPort;
	
	@Test
    public void testGetEmployeeListSuccess() throws URISyntaxException 
    {
		String uriString = "/getresult/name/eesti";
		final String baseUrl = "http://localhost:" + randomServerPort + uriString;
	    URI uri = new URI(baseUrl);
	    RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<String[]> result = restTemplate.getForEntity(uri, String[].class);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		String[] listOfCapital = result.getBody();
		String[] expectedResponseArray = {"Tallinn"};
		assertTrue(Arrays.equals(expectedResponseArray, listOfCapital));
	
    }  

}
