package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.exceptionhandling.CustomException;
import com.example.demo.vo.Response;

@RestController
public class RestApiController {
	
	@Autowired
	public RestTemplate restTemplate;
	
	@RequestMapping(value= {"/getresult/{type}/{pathValue}","/getresult/{type}"},method = RequestMethod.GET,produces = "application/json")
	public ResponseEntity<List<String>> getOutput(@PathVariable(value = "type",required = true)String type, 
			@PathVariable(value = "pathValue",required = false)String pathValue,
			@RequestParam(value="fullText",required = false, defaultValue = "false") Boolean fullText, 
			@RequestParam(value="codes", required = false) String codes) {
		/*
		 /rest/v2/name/{name}
/rest/v2/name/{name}?fullText=true
/rest/v2/alpha/{code}
/rest/v2/alpha?codes={code};{code};{code}
rest/v2/currency/{currency}
/rest/v2/lang/{et}
rest/v2/capital/{capital}
rest/v2/callingcode/{callingcode}
rest/v2/region/europe
/v2/regionalbloc/{regionalbloc}
		 */
		StringBuilder uri = new StringBuilder();
		uri.append("https://restcountries.eu/rest/v2");
		switch (type) {
		case "name":
			if(!isNullOrEmpty(pathValue)) {
				uri.append("/name/")
				.append(pathValue);
				if(fullText) {
					uri.append("?fullText=true");
				}
			}else {
				throw new CustomException("400", "Invaild input 'pathValue is null or empty'");
			}
			break;
		case "alpha":
			if(!isNullOrEmpty(pathValue)) {
				uri.append("/alpha/")
				.append(pathValue);
			}else if(!isNullOrEmpty(codes)){
				uri.append("/alpha?codes=")
				.append(codes);
			}else {
				throw new CustomException("400", "Invaild input 'alpha'");
			}
			break;
		default:
			if(!isNullOrEmpty(type)) {
				if(type.matches("currency|lang|capital|callingcode|region|regionalbloc")) {
					uri.append("/")
					.append(type)
					.append("/")
					.append(pathValue);
				}else {
					throw new CustomException("400", "Invaild input 'type'");
				}
			}else {
				throw new CustomException("400", "Invaild input 'type is null or empty'");
			}
		}
		return ResponseEntity.ok(consumeServices(uri));
	}

	private List<String> consumeServices(StringBuilder uri) {
		List<String> outputList = null;
		try {
			String data = restTemplate.getForEntity(uri.toString(), String.class).getBody();
			if(data.startsWith("[")) {
				ResponseEntity<Response[]> responseEntity= restTemplate.getForEntity(uri.toString(), Response[].class);
				Response[] body = responseEntity.getBody();
				List<Response> reponseList = Arrays.asList(body);
				outputList = reponseList.stream().map( o -> o.getCapital()).collect(Collectors.toList());
			}else {
				ResponseEntity<Response> responseEntityWithoutList= restTemplate.getForEntity(uri.toString(), Response.class);
				outputList = new ArrayList<>();
				outputList.add(responseEntityWithoutList.getBody().getCapital());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new CustomException("", e.getMessage());
		}
		return outputList;
	}
	
	private Boolean isNullOrEmpty(String pathValue) {
		if(pathValue != null && !"".equalsIgnoreCase(pathValue.trim())) {
			return false;
		}else {
			return true;
		}
	}

}
