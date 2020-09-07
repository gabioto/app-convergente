package com.tdp.ms.autogestion.autogestion.repository.datasource.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdp.ms.autogestion.config.PropertiesConfig;
import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.datasource.api.TicketApi;
import com.tdp.ms.autogestion.repository.datasource.api.entities.TicketApiResponse;
import com.tdp.ms.autogestion.util.FunctionsUtil;

@ExtendWith(MockitoExtension.class)
public class TicketApiTest {

//	@InjectMocks
//	private TicketApi ticketApi;
//
//	@Mock
//	private PropertiesConfig config;
//
//	@Mock
//	private FunctionsUtil functionsUtil;
//
//	@Mock
//	private RestTemplate restTemplate;
//
//	private static Map<String, TicketApiResponse> ticketResponseMap = new HashMap<>();
//	private static Map<String, Ticket> ticketRequestMap = new HashMap<>();
//	private static OAuth oAuthResponseMap;
//	private static Ticket ticketInitial;
//	private static TicketApiResponse ticketComplete;
//
////	@BeforeAll
//	public static void setup() throws JsonProcessingException {
////		ticketComplete = new Ticket(0, 19406743, "/ticket/v2/tickets/19406743", "averia", actualDate, "minor",
////				"TroubleTicket", "acknowledged", actualDate, "Ticket generado", 1, "", "20000032", "99", "serviceCode",
////				"broadband", "3", null, new Customer("70981983", "DNI", "10368606"), null, actualDate,
////				new ArrayList<>(), new ArrayList<>());
////		
//		ticketComplete = new TicketApiResponse("19406743", "/ticket/v2/tickets/19406743", "Ticket generado",
//				"2020-09-04T11:00:00.000z", "minor", "TroubleTicket", "acknowledged", "2020-09-04T11:00:00.000z", "",
//				1);
//
//		ticketInitial = new Ticket(0, 0, null, "averia", null, "minor", "TroubleTicket", null, null, null, 1, "",
//				"20000032", "99", "serviceCode", "broadband", "3", null, new Customer("70981983", "DNI", "10368606"),
//				null, null, new ArrayList<>(), new ArrayList<>());
//
//		oAuthResponseMap = new OAuth();
//
//		// TICKET RESPONSE
//		ticketResponseMap.put("generated_ticket", ticketComplete);
//
//		// TICKET REQUEST
//		ticketRequestMap.put("ticket_to_generate", ticketInitial);
//	}
//
////	@Test
//	void generateTicket() {
//		when(config.getCreateTicket()).thenReturn("https://api.us-east.apiconnect.ibmcloud.com/telefonica-del-peru-development/ter/triagefcr/createticket/tickets");
//		when(config.getClientTicket()).thenReturn("252525");
//
////		when(restTemplate.exchange(requestUrl, HttpMethod.POST, HttpEntity<TicketApiReque, responseType)(
////	            “http://localhost:8080/employee/E001”, Employee.class))
////	          .thenReturn(new ResponseEntity(emp, HttpStatus.OK));
//
//		when(restTemplate.exchange(
//                anyString(),
//                any(HttpMethod.class),
//                any(),
//                ArgumentMatchers.<Class<TicketApiResponse>> any()))
//						.thenReturn(new ResponseEntity<TicketApiResponse>(ticketResponseMap.get("generated_ticket"),
//								HttpStatus.OK));
//
//		Ticket ticket = ticketApi.generate(oAuthResponseMap, ticketRequestMap.get("ticket_to_generate"));
//	}
}
