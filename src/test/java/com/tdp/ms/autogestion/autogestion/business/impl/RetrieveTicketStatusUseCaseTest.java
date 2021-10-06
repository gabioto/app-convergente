package com.tdp.ms.autogestion.autogestion.business.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tdp.ms.autogestion.business.impl.RetrieveTicketStatusUseCaseImpl;
import com.tdp.ms.autogestion.exception.ResourceNotFoundException;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse;
import com.tdp.ms.autogestion.model.AdditionalData;
import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.LogData;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.TicketRepository;
import com.tdp.ms.autogestion.util.FunctionsUtil;

@ExtendWith(MockitoExtension.class)
public class RetrieveTicketStatusUseCaseTest {

	@InjectMocks
	RetrieveTicketStatusUseCaseImpl retrieveTicketStatusUseCase;

	@Mock
	private TicketRepository ticketRepository;

	@Mock
	private FunctionsUtil functionsUtil;

	private static Map<String, TicketStatusResponse> ticketResponseMap = new HashMap<>();
	private static List<AdditionalData> lstAdditionalData = new ArrayList<AdditionalData>();
	private static List<Ticket> lstTicket = new ArrayList<Ticket>();
	private static List<Ticket> lstTwoTickets = new ArrayList<Ticket>();
	private static Ticket ticket;

	@BeforeAll
	public static void setup() {
		LocalDateTime actualDate = LocalDateTime.now(ZoneOffset.of("-05:00"));

		// RESPONSE POST
		ticketResponseMap.put("get", new TicketStatusResponse());

		ticket = new Ticket();
		ticket.setDescription("averia");
		ticket.setCreationDate(actualDate);
		ticket.setType("TroubleTicket");
		ticket.setStatusChangeDate(actualDate);
		ticket.setStatus("WHATSAPP");
		ticket.setModifiedDateTicket(actualDate);
		ticket.setCustomer(new Customer("DNI", "10101010", "545454"));

		AdditionalData additionalData = new AdditionalData();
		additionalData.setKey("STATUS");
		additionalData.setValue("held");
		lstAdditionalData.add(additionalData);

		ticket.setAdditionalData(lstAdditionalData);

		lstTicket.add(ticket);

		lstTwoTickets.add(ticket);
		lstTwoTickets.add(ticket);
	}

	@Test
	void retrieveTicketStatus_complete() throws Exception {
		retrieveTicketStatusComplete();
	}

	@Test
	void retrieveTicketStatus_completeMoreTickets() throws Exception {
		lstTicket.add(ticket);
		retrieveTicketStatusComplete();
	}

	private void retrieveTicketStatusComplete() {
		when(ticketRepository.getTicketStatus(anyInt())).thenReturn(lstTicket);

		when(ticketRepository.getAdditionalData(any(Ticket.class), any(Integer.class))).thenReturn(lstAdditionalData);

		doNothing().when(functionsUtil).saveLogData(any(LogData.class));

		ResponseEntity<TicketStatusResponse> ticketResponse = retrieveTicketStatusUseCase
				.retrieveTicketStatus(19406198);

		assertNotNull(ticketResponse);
		assertEquals(ticketResponse.getStatusCode(), HttpStatus.OK);
	}

	@Test
	void retrieveTicketStatus_notexist() throws Exception {
		when(ticketRepository.getTicketStatus(anyInt())).thenThrow(ResourceNotFoundException.class);

		assertThrows(ResourceNotFoundException.class, () -> {
			retrieveTicketStatusUseCase.retrieveTicketStatus(194061982);
		});
	}
}