package com.lightdevel.wephuot.moneymanagement.configs;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Slf4j
@Configuration
public class ServiceConfig {
	@Value("${ssl.trust-store}")
	private Resource trustStore;

	@Value("${ssl.trust-store-password}")
	private String trustStorePassword;

	@Bean
	public RestTemplate restTemplate() {
		try {
			SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
					.build();
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
			HttpClient httpClient = HttpClients.custom()
					.setSSLSocketFactory(socketFactory)
					.build();
			HttpComponentsClientHttpRequestFactory factory =
					new HttpComponentsClientHttpRequestFactory(httpClient);
			return new RestTemplate(factory);
		} catch (Exception exception) {
			log.error("Could not load certificate");
		}
		return new RestTemplate();
	}
}
