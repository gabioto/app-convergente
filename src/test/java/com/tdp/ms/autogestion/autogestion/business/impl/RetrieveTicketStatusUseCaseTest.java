package com.tdp.ms.autogestion.autogestion.business.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyInt;

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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdp.ms.autogestion.business.impl.RetrieveTicketStatusUseCaseImpl;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse;
import com.tdp.ms.autogestion.model.AdditionalData;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class RetrieveTicketStatusUseCaseTest {

	@InjectMocks
	RetrieveTicketStatusUseCaseImpl retrieveTicketStatusUseCase;
	
	@Mock
	private TicketRepository ticketRepository;
	
	private static Map<String, TicketStatusResponse> ticketResponseMap = new HashMap<>();
	
	private static List<Ticket> lstTicket = new ArrayList<Ticket>();
	
	private static List<AdditionalData> lstAdditionalData = new ArrayList<AdditionalData>();
		
	@BeforeAll
	public static void setup() throws JsonProcessingException {
		// RESPONSE POST
		ticketResponseMap.put("get", new TicketStatusResponse());
		
		Ticket ticket = new Ticket();
		ticket.setDescription("averia");
		ticket.setCreationDate(LocalDateTime.now(ZoneOffset.of("-05:00")));
		ticket.setType("TroubleTicket");
		ticket.setStatusChangeDate(LocalDateTime.now(ZoneOffset.of("-05:00")));
		ticket.setStatus("WHATSAPP");
		ticket.setModifiedDateTicket(LocalDateTime.now(ZoneOffset.of("-05:00")));
		
		AdditionalData additionalData = new AdditionalData();
		additionalData.setKey("STATUS");
		additionalData.setValue("held");
		lstAdditionalData.add(additionalData);
		
		ticket.setAdditionalData(lstAdditionalData);
		lstTicket.add(ticket);
	}
	
	@Test
	void retrieveTicketStatus_complete() throws Exception {
		when(ticketRepository.getTicketStatus(anyInt())).thenReturn(lstTicket);
		
		when(ticketRepository.getAdditionalData(any(Ticket.class))).thenReturn(lstAdditionalData);
		
		ResponseEntity<TicketStatusResponse> ticketResponse = retrieveTicketStatusUseCase.retrieveTicketStatus(19406198);
		
		assertNotNull(ticketResponse);
		assertEquals(ticketResponse.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	void retrieveTicketStatus_notexist() throws Exception {
		when(ticketRepository.getTicketStatus(anyInt())).thenReturn(lstTicket);
		
		when(ticketRepository.getAdditionalData(any(Ticket.class))).thenReturn(lstAdditionalData);
		
		ResponseEntity<TicketStatusResponse> ticketResponse = retrieveTicketStatusUseCase.retrieveTicketStatus(194061982);
		
		assertNotNull(ticketResponse);
		assertEquals(ticketResponse.getStatusCode(), HttpStatus.OK);
	}
}