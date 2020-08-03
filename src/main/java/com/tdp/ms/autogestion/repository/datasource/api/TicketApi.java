package com.tdp.ms.autogestion.repository.datasource.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.tdp.ms.autogestion.config.PropertiesConfig;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.datasource.api.entities.TicketApiRequest;
import com.tdp.ms.autogestion.repository.datasource.api.entities.TicketApiResponse;
import com.tdp.ms.autogestion.util.FunctionsUtil;
import com.tdp.ms.autogestion.util.SSLClientFactory;
import com.tdp.ms.autogestion.util.SSLClientFactory.HttpClientType;

@Component
public class TicketApi {

	private static final Log log = LogFactory.getLog(TicketApi.class);
	private static final String TAG = TicketApi.class.getCanonicalName();

	@Autowired
	private PropertiesConfig config;

	@Autowired
	private FunctionsUtil functionsUtil;

	public Ticket generate(OAuth pOAuth, Ticket pTicket) {
		RestTemplate restTemplate = new RestTemplate(
				SSLClientFactory.getClientHttpRequestFactory(HttpClientType.OkHttpClient));
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		String requestUrl = config.getCreateTicket();
		System.out.println("requestUrl::::: " + requestUrl);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-IBM-Client-Id", config.getClientTicket());
		headers.set("Authorization", "Bearer " + pOAuth.getAccessToken());

		TicketApiRequest ticketRequest = new TicketApiRequest();
		ticketRequest.generateRequest(pTicket);

		HttpEntity<TicketApiRequest> entity = new HttpEntity<>(ticketRequest, headers);

		log.info(TAG + " " + new Gson().toJson(entity));

		try {
			ResponseEntity<TicketApiResponse> response = restTemplate.exchange(requestUrl, HttpMethod.POST, entity,
					TicketApiResponse.class);

			TicketApiResponse ticketResponse = response.getBody();

			if (ticketResponse != null && ticketResponse.getId() != null) {
				return ticketResponse.fromThis(pTicket);
			} else {
				functionsUtil.saveLogData(0, pTicket.getCustomer().getNationalId(),
						pTicket.getCustomer().getNationalType(), pTicket.getChannelId(), "ERROR", new Gson().toJson(entity),
						"TicketResponse inválido", "generateTicekt");
				throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (HttpClientErrorException e) {
			functionsUtil.saveLogData(0, pTicket.getCustomer().getNationalId(),
					pTicket.getCustomer().getNationalType(), pTicket.getChannelId(), "ERROR", new Gson().toJson(entity),
					e.getLocalizedMessage(), "generateTicekt");
			throw e;
		} catch (Exception e) {
			functionsUtil.saveLogData(0, pTicket.getCustomer().getNationalId(),
					pTicket.getCustomer().getNationalType(), pTicket.getChannelId(), "ERROR", new Gson().toJson(entity),
					e.getLocalizedMessage(), "generateTicekt");
			throw e;
		}
	}
}
