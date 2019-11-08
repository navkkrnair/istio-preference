package com.istio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;

@SpringBootApplication
public class PreferenceApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(PreferenceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder)
	{
		return restTemplateBuilder.build();
	}

	@Bean
	public Tracer tracer()
	{
		return Configuration.fromEnv().getTracer();
	}

}
