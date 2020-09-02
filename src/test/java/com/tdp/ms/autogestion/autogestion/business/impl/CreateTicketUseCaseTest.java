package com.tdp.ms.autogestion.autogestion.business.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdp.ms.autogestion.business.impl.CreateTicketUsecaseImpl;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.AdditionalData;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.Channel;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.RelatedObject;
import com.tdp.ms.autogestion.expose.entities.TicketCreateResponse;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.OAuthRepository;
import com.tdp.ms.autogestion.repository.TicketRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.LocalDateTimeConverter;

@ExtendWith(MockitoExtension.class)
public class CreateTicketUseCaseTest {

	@InjectMocks
	CreateTicketUsecaseImpl createTicketUseCase;

	@Mock
	private OAuthRepository oAuthRepository;
//	private OAuthRepository oAuthRepository = mock(OAuthRepository.class);

	@Mock
	private TicketRepository ticketRepository;

	private static Map<String, TicketCreateResponse> ticketResponseMap = new HashMap<>();
	private static Map<String, TicketCreateRequest> ticketRequestMap = new HashMap<>();

	private static AdditionalData nationalTypeAdditional = new AdditionalData("nationalIdType", "DNI");
	private static AdditionalData nationalIdAdditional = new AdditionalData("nationalId", "70981983");
	private static AdditionalData productIdAdditional = new AdditionalData("productIdentifier", "serviceCode");

	private static List<AdditionalData> additionalRequest = new ArrayList<>();
	private static List<AdditionalData> emptyAdditionalRequest = new ArrayList<>();

	@BeforeAll
	public static void setup() throws JsonProcessingException {
		additionalRequest.add(nationalTypeAdditional);
		additionalRequest.add(nationalIdAdditional);
		additionalRequest.add(productIdAdditional);

		// RESPONSE POST
		ticketResponseMap.put("post", new TicketCreateResponse());

		// REQUEST POST
		ticketRequestMap.put("POST_COMPLETE", new TicketCreateRequest("averia", "minor", "TroubleTicket", 1,
				new Channel("AppConvergente", "3"), new RelatedObject("broadband", "10368606"), additionalRequest));

		ticketRequestMap.put("POST_EMPTY",
				new TicketCreateRequest("averia", "minor", "TroubleTicket", 1, new Channel("AppConvergente", "3"),
						new RelatedObject("broadband", "10368606"), emptyAdditionalRequest));
	}

	@Test
	void createTicket_completeParams() throws Exception {
		when(oAuthRepository.getOAuthValues()).thenReturn(new OAuth("PARAM_KEY_OAUTH_TOKEN", "Bearer",
				"AAIkZjhmZmU1YjUtNzVlYy00ZDY1LWIwZDYtODY5Y2Y2NDJiNjQyrDDAdK2T5ncDoyvUAUPnnWEkdwMjDc29b02RHIHbm76DDFCMLxGFMT5oBaolkQ5fyqsWOAxg6R1J6N-8pHz1NmGdlNX2ZA5OCiNAEdR5fZg",
				"3600", "1599053571", "scope1",
				"AAKstYVcRgMA8xwn90jhSB008ds-uUkIamtsIfP6UV7WZAD5qJpepT8MGhF70H9qtOryvdwKl-Tnth3P8p3jJtyEDVQD48TQGrUA4jKJG6C89A",
				"2682000"));

		when(ticketRepository.generateTicket(any(OAuth.class), any(Ticket.class))).thenAnswer(new Answer<Ticket>() {

			@Override
			public Ticket answer(InvocationOnMock invocation) throws Throwable {
				Ticket ticket = (Ticket) invocation.getArguments()[1];
				ticket.setCreationDate(LocalDateTime.now(ZoneOffset.of("-05:00")));
				return ticket;
			}
		});

		doNothing().when(ticketRepository).saveGeneratedTicket(any(Ticket.class));

		ResponseEntity<TicketCreateResponse> ticketResponse = createTicketUseCase
				.createTicket(ticketRequestMap.get("POST_COMPLETE"));

		assertNotNull(ticketResponse);
		assertEquals(ticketResponse.getStatusCode(), HttpStatus.OK);
	}
}
