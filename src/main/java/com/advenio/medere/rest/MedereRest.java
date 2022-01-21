package com.advenio.medere.rest;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent("MedereRest")
@Scope("singleton")
public class MedereRest {
	protected final Logger logger = LoggerFactory.getLogger(MedereRest.class);
	@Value("${webappointmentserver.usessl}")
	private boolean usessl;

	public RestTemplate createRestTemplate() {
		if (usessl) {
			try {
				SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
						new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
						NoopHostnameVerifier.INSTANCE);

				CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
						.setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

				HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
				requestFactory.setHttpClient(httpClient);

				RestTemplate restTemplate = new RestTemplate(requestFactory);
				return restTemplate;
			} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
				logger.error(e.getMessage(), e);
			}
		}

		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}
}