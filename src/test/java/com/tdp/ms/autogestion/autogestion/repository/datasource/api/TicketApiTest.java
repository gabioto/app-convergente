package com.tdp.ms.autogestion.autogestion.repository.datasource.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdp.ms.autogestion.config.PropertiesConfig;
import com.tdp.ms.autogestion.exception.ExternalServerException;
import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.LogData;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.datasource.api.TicketApi;
import com.tdp.ms.autogestion.repository.datasource.api.entities.TicketApiResponse;
import com.tdp.ms.autogestion.util.FunctionsUtil;

@ExtendWith(MockitoExtension.class)
public class TicketApiTest {

	@InjectMocks
	private TicketApi ticketApi;

	@Mock
	private PropertiesConfig config;

	@Mock
	private FunctionsUtil functionsUtil;

	@Mock
	private HttpComponentsClientHttpRequestFactory initClientRestTemplate;

	@Mock
	private RestTemplate restTemplate;

	private static Map<String, TicketApiResponse> ticketApiResponseMap = new HashMap<>();
	private static Map<String, Ticket> ticketRequestMap = new HashMap<>();
	private static OAuth oAuthResponseMap;
	private static Ticket ticketInitial;
	private static TicketApiResponse ticketComplete;
	private final String fakeUrl = "";

	@BeforeAll
	public static void setup() throws JsonProcessingException {

		ticketComplete = new TicketApiResponse("19406743", "/ticket/v2/tickets/19406743", "Ticket generado",
				"2020-09-07T16:20:49.108-05:00", "minor", "TroubleTicket", "acknowledged",
				"2020-09-07T16:20:49.108-05:00", "", 1);

		ticketInitial = new Ticket(0, 0, null, "averia", null, "minor", "TroubleTicket", null, null, null, 1, "",
				"20000032", "99", "serviceCode", "broadband", "3", null, new Customer("70981983", "DNI", "10368606"),
				null, null, new ArrayList<>(), new ArrayList<>());

		oAuthResponseMap = new OAuth();

		// TICKET RESPONSE
		ticketApiResponseMap.put("generated_ticket", ticketComplete);
		ticketApiResponseMap.put("generated_null", null);
		ticketApiResponseMap.put("generated_empty", new TicketApiResponse());

		// TICKET REQUEST
		ticketRequestMap.put("ticket_to_generate", ticketInitial);
	}

	@Test
	void generateTicket_responseOk() {
		when(config.getCreateTicket()).thenReturn(fakeUrl);

		when(config.getClientTicket()).thenReturn("252525");

		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(),
				ArgumentMatchers.<Class<TicketApiResponse>>any()))
						.thenReturn(new ResponseEntity<TicketApiResponse>(ticketApiResponseMap.get("generated_ticket"),
								HttpStatus.OK));
	}

	@Test
	void generateTicket_responseNull() {
		generateTicketException(ticketApiResponseMap.get("generated_null"));
	}

	@Test
	void generateTicket_responseEmpty() {
		generateTicketException(ticketApiResponseMap.get("generated_empty"));
	}

	private void generateTicketException(TicketApiResponse response) {
		when(config.getCreateTicket()).thenReturn(fakeUrl);

		when(config.getClientTicket()).thenReturn("252525");

		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(),
				ArgumentMatchers.<Class<TicketApiResponse>>any()))
						.thenReturn(new ResponseEntity<TicketApiResponse>(response, HttpStatus.OK));

		doNothing().when(functionsUtil).saveLogData(any(LogData.class));

		ExternalServerException exception = assertThrows(ExternalServerException.class, () -> {
			ticketApi.generate(oAuthResponseMap, ticketRequestMap.get("ticket_to_generate"));
		});

		assertEquals(exception.getMessage(), "500 invalid ticketResponse");
	}

}
