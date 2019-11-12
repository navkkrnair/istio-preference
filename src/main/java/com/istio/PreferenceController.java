package com.istio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class PreferenceController
{
	private static final String RESPONSE_STRING_FORMAT = "preference => %s\n";

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final RestTemplate restTemplate;

	@Value("${recommendations.api.url:http://recommendation:8080}")
	private String remoteURL;

	public PreferenceController(RestTemplate restTemplate)
	{
		this.restTemplate = restTemplate;
	}

	@RequestMapping("/")
	public ResponseEntity<?> getPreferences()
	{
		logger.info(">> getPreferences() called");

		logger.info(">> Calling {}", remoteURL);
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(remoteURL, String.class);
		String                 response       = responseEntity.getBody();
		return ResponseEntity.ok(String.format(RESPONSE_STRING_FORMAT, response.trim()));
	}

	@ExceptionHandler
	public ResponseEntity<String> handleException(HttpStatusCodeException ex)
	{
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
			.body(
					String.format(
							RESPONSE_STRING_FORMAT,
								String.format("%d %s", ex.getRawStatusCode(), createHttpErrorResponseString(ex))));
	}

	private String createHttpErrorResponseString(HttpStatusCodeException ex)
	{
		return ex.getResponseBodyAsString().trim();

	}

}
