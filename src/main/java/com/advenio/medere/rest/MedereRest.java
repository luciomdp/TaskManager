package com.advenio.medere.rest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.advenio.medere.emr.dao.dto.PrescriptionExpirationJobDTO;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.web.util.UriComponentsBuilder;

@SpringComponent("MedereRest")
@Scope("singleton")
public class MedereRest {
	protected final Logger logger = LoggerFactory.getLogger(MedereRest.class);

	@Value("${medere.medereaddress}")
	private String baseURL;

	@Value("${webappointmentserver.usessl}")
	private boolean usessl;

	private String webmedererestcontroller = "rest/webmedererestcontroller";

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

	public List<PrescriptionExpirationJobDTO> getPrescriptionExpirationJobList() throws ResourceAccessException {
		String uri = String.format("%s%s%s", baseURL, webmedererestcontroller , "/prescriptionexpirationjob");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
		RestTemplate restTemplate = createRestTemplate();
		try {
			ResponseEntity<List<PrescriptionExpirationJobDTO>> res = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
					HttpEntity.EMPTY, new ParameterizedTypeReference<List<PrescriptionExpirationJobDTO>>() {
					});
			return res.getBody();
		} catch (HttpClientErrorException e) {
			return new ArrayList<PrescriptionExpirationJobDTO>();
		}
	}


	public Boolean updatePrescriptionExpirationJob(String medereUUID) {
		String uri = String.format("%s%s%s/%s", baseURL, webmedererestcontroller , "/prescriptionexpirationjob", medereUUID);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
		RestTemplate restTemplate = createRestTemplate();
		builder.queryParam("action", "update");
		try {
			ResponseEntity<String> res = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,HttpEntity.EMPTY,String.class);
			if (res.getStatusCode().is2xxSuccessful()) {
				return true;
			}
			return null;
		}
		catch (Exception e) {
			return false;
		}

	}

	public Boolean stopPrescriptionExpirationJob(String medereUUID) {
		String uri = String.format("%s%s%s/%s", baseURL, webmedererestcontroller , "/prescriptionexpirationjob", medereUUID);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
		RestTemplate restTemplate = createRestTemplate();
		try {
			ResponseEntity<String> res = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE,HttpEntity.EMPTY,String.class);
			if (res.getStatusCode().is2xxSuccessful()) {
				return true;
			}
			return null;
		}
		catch (Exception e) {
			return false;
		}
	}

	public Boolean runPrescriptionExpirationJob(String medereUUID) {
		String uri = String.format("%s%s%s/%s", baseURL, webmedererestcontroller , "/prescriptionexpirationjob", medereUUID);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
		RestTemplate restTemplate = createRestTemplate();
		builder.queryParam("action", "run");
		try {
			ResponseEntity<String> res = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,HttpEntity.EMPTY,String.class);
			if (res.getStatusCode().is2xxSuccessful()) {
				return true;
			}
			return null;
		}
		catch (Exception e) {
			return false;
		}
	}
}