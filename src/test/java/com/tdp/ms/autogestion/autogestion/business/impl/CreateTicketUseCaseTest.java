package com.tdp.ms.autogestion.autogestion.business.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdp.ms.autogestion.business.impl.CreateTicketUsecaseImpl;
import com.tdp.ms.autogestion.exception.DomainException;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.AdditionalData;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.Channel;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.RelatedObject;
import com.tdp.ms.autogestion.expose.entities.TicketCreateResponse;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.OAuthRepository;
import com.tdp.ms.autogestion.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class CreateTicketUseCaseTest {

	@InjectMocks
	private CreateTicketUsecaseImpl createTicketUseCase;

	@Mock
	private OAuthRepository oAuthRepository;

	@Mock
	private TicketRepository ticketRepository;

	private static Map<String, TicketCreateRequest> ticketRequestMap = new HashMap<>();
	private static OAuth oAuthResponseMap = new OAuth();

	private static AdditionalData nationalTypeAdditional = new AdditionalData("nationalIdType", "DNI");
	private static AdditionalData nationalIdAdditional = new AdditionalData("nationalId", "70981983");
	private static AdditionalData productIdAdditional = new AdditionalData("productIdentifier", "serviceCode");

	private static List<AdditionalData> additionalRequest = new ArrayList<>();

	@BeforeAll
	public static void setup() throws JsonProcessingException {
		additionalRequest.add(nationalTypeAdditional);
		additionalRequest.add(nationalIdAdditional);
		additionalRequest.add(productIdAdditional);

		// TICKET REQUEST
		ticketRequestMap.put("post_internet_complete", new TicketCreateRequest("averia", "minor", "TroubleTicket", 1,
				new Channel("AppConvergente", "3"), new RelatedObject("broadband", "10368606"), additionalRequest));

		ticketRequestMap.put("post_landline_complete", new TicketCreateRequest("averia", "minor", "TroubleTicket", 1,
				new Channel("AppConvergente", "3"), new RelatedObject("landline", "10368606"), additionalRequest));

		ticketRequestMap.put("post_cable_complete", new TicketCreateRequest("averia", "minor", "TroubleTicket", 1,
				new Channel("AppConvergente", "3"), new RelatedObject("cableTv", "10368606"), additionalRequest));

		ticketRequestMap.put("post_mobile_complete", new TicketCreateRequest("averia", "minor", "TroubleTicket", 1,
				new Channel("AppConvergente", "3"), new RelatedObject("mobil", "10368606"), additionalRequest));
		
		ticketRequestMap.put("post_other_complete", new TicketCreateRequest("averia", "minor", "TroubleTicket", 1,
				new Channel("AppConvergente", "3"), new RelatedObject("mobillllll", "10368606"), additionalRequest));
	}

	@Test
	void createTicket_completeInternetParams() throws Exception {
		createTicket(ticketRequestMap.get("post_internet_complete"));
	}

	@Test
	void createTicket_completeLandlineParams() throws Exception {
		createTicket(ticketRequestMap.get("post_landline_complete"));
	}

	@Test
	void createTicket_completeCableParams() throws Exception {
		createTicket(ticketRequestMap.get("post_cable_complete"));
	}

	@Test
	void createTicket_completeMobileParams() throws Exception {
		createTicket(ticketRequestMap.get("post_mobile_complete"));
	}
	
	@Test
	void createTicket_completeOtherServiceParams() throws Exception {
		createTicket(ticketRequestMap.get("post_other_complete"));
	}

	private void createTicket(TicketCreateRequest request) throws Exception {
		when(oAuthRepository.getOAuthValues()).thenReturn(oAuthResponseMap);

		when(ticketRepository.generateTicket(any(OAuth.class), any(Ticket.class))).thenAnswer(new Answer<Ticket>() {

			@Override
			public Ticket answer(InvocationOnMock invocation) throws Throwable {
				Ticket ticket = (Ticket) invocation.getArguments()[1];
				ticket.setCreationDate(LocalDateTime.now(ZoneOffset.of("-05:00")));
				return ticket;
			}
		});

		doNothing().when(ticketRepository).saveGeneratedTicket(any(Ticket.class));

		ResponseEntity<TicketCreateResponse> ticketResponse = createTicketUseCase.createTicket(request);

		assertNotNull(ticketResponse);
		assertEquals(ticketResponse.getStatusCode(), HttpStatus.OK);
	}

	@Test
	void createTicket_exception() throws Exception {
		when(oAuthRepository.getOAuthValues()).thenReturn(oAuthResponseMap);

		when(ticketRepository.generateTicket(any(OAuth.class), any(Ticket.class)))
				.thenThrow(HttpClientErrorException.class);

		assertThrows(DomainException.class, () -> {
			createTicketUseCase.createTicket(ticketRequestMap.get("post_internet_complete"));
		});
	}
}
