package com.tdp.ms.autogestion.autogestion.business.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tdp.ms.autogestion.business.impl.RetrieveTicketsUseCaseImpl;
import com.tdp.ms.autogestion.exception.ForbiddenException;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse;
import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class RetrieveTicketsUseCaseTest {

	@InjectMocks
	private RetrieveTicketsUseCaseImpl retrieveTicketsUseCase;

	@Mock
	private TicketRepository ticketRepository;

	private static List<Ticket> pendingOneTicket = new ArrayList<>();
	private static List<Ticket> solvedOneTicket = new ArrayList<>();
	private static List<Ticket> solvedTwoTickets = new ArrayList<>();
	private static List<Ticket> completeTrackTickets = new ArrayList<>();
	private static List<Ticket> completeTrackTwoTickets = new ArrayList<>();
	
	private static Ticket pendingTicket, anotherPendingTicket, solvedTicket, waSolvedTicket;

	@BeforeAll
	public static void setup() {
		LocalDateTime actualDate = LocalDateTime.now(ZoneOffset.of("-05:00"));

		pendingTicket = new Ticket(1, 19406743, "/ticket/v2/tickets/19406743", "averia", actualDate, "minor",
				"TroubleTicket", "acknowledged", actualDate, "Ticket generado", 1, "", "20000032", "99", "serviceCode",
				"broadband", "3", null, new Customer("70981983", "DNI", "10368606"), "IN_PROGRESS", actualDate,
				new ArrayList<>(), new ArrayList<>());

		solvedTicket = new Ticket(2, 19406743, "/ticket/v2/tickets/19406743", "averia", actualDate, "minor",
				"TroubleTicket", "acknowledged", actualDate, "Ticket generado", 1, "", "20000032", "99", "serviceCode",
				"broadband", "3", null, new Customer("70981983", "DNI", "10368606"), "SOLVED", actualDate,
				new ArrayList<>(), new ArrayList<>());

		anotherPendingTicket = new Ticket(3, 19406744, "/ticket/v2/tickets/19406744", "averia", actualDate, "minor",
				"TroubleTicket", "acknowledged", actualDate, "Ticket generado", 1, "", "20000032", "99", "serviceCode",
				"broadband", "3", null, new Customer("70981983", "DNI", "10368606"), "IN_PROGRESS", actualDate,
				new ArrayList<>(), new ArrayList<>());

		waSolvedTicket = new Ticket(4, 19406744, "/ticket/v2/tickets/19406744", "averia", actualDate, "minor",
				"TroubleTicket", "acknowledged", actualDate, "Ticket generado", 1, "", "20000032", "99", "serviceCode",
				"broadband", "3", null, new Customer("70981983", "DNI", "10368606"), "WA_SOLVED", actualDate,
				new ArrayList<>(), new ArrayList<>());

		pendingOneTicket.add(pendingTicket);

		solvedOneTicket.add(solvedTicket);

		completeTrackTickets.add(pendingTicket);
		completeTrackTickets.add(solvedTicket);

		solvedTwoTickets.add(solvedTicket);
		solvedTwoTickets.add(waSolvedTicket);
		
		completeTrackTwoTickets.add(pendingTicket);
		completeTrackTwoTickets.add(solvedTicket);
		completeTrackTwoTickets.add(anotherPendingTicket);
		completeTrackTwoTickets.add(waSolvedTicket);
	}

	@Test
	void getPendingTicket_withoutTickets() {

		when(ticketRepository.findByCustomerAndUseCase(anyString(), anyString(), anyString(), anyString(),
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(new ArrayList<>());

		when(ticketRepository.findByCustomerAndUseCasePast(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(new ArrayList<>());

		ResponseEntity<TicketStatusResponse> response = retrieveTicketsUseCase.pendingTicket("CMS", "broadband",
				"10400884", "DNI", "70981983");

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNull(response.getBody().getTicketId());
	}

	@Test
	void getPendingTicket_currentOneTicket() {
		when(ticketRepository.findByCustomerAndUseCase(anyString(), anyString(), anyString(), anyString(),
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(pendingOneTicket);

		when(ticketRepository.getTicket(anyInt())).thenReturn(pendingTicket);

		when(ticketRepository.getAdditionalData(any(Ticket.class))).thenReturn(new ArrayList<>());

		ResponseEntity<TicketStatusResponse> response = retrieveTicketsUseCase.pendingTicket("CMS", "broadband",
				"10400884", "DNI", "70981983");

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNotNull(response.getBody().getTicketId());
		assertEquals(response.getBody().getTicketId(), 19406743);
	}

	@Test
	void getPendingTicket_currentOneSolvedTicket() {

		when(ticketRepository.findByCustomerAndUseCase(anyString(), anyString(), anyString(), anyString(),
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(pendingOneTicket);

		when(ticketRepository.getTicket(anyInt())).thenReturn(solvedTicket);

		ResponseEntity<TicketStatusResponse> response = retrieveTicketsUseCase.pendingTicket("CMS", "broadband",
				"10400884", "DNI", "70981983");

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNull(response.getBody().getTicketId());
	}

	@Test
	void getPendingTicket_currentTwoSolvedTickets() {

		when(ticketRepository.findByCustomerAndUseCase(anyString(), anyString(), anyString(), anyString(),
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(solvedTwoTickets);

		when(ticketRepository.getTicket(anyInt())).thenReturn(waSolvedTicket);

		ForbiddenException ex = assertThrows(ForbiddenException.class, () -> {
			retrieveTicketsUseCase.pendingTicket("CMS", "broadband", "10400884", "DNI", "70981983");
		});

		assertEquals(ex.getMessage(), "User can´t create more tickets");
	}

	@Test
	void getPendingTicket_completeTrackTickets() {

		when(ticketRepository.findByCustomerAndUseCase(anyString(), anyString(), anyString(), anyString(),
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(completeTrackTickets);

		when(ticketRepository.getTicket(anyInt())).thenReturn(solvedTicket);

		ResponseEntity<TicketStatusResponse> response = retrieveTicketsUseCase.pendingTicket("CMS", "broadband",
				"10400884", "DNI", "70981983");

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNull(response.getBody().getTicketId());
	}
	
	@Test
	void getPendingTicket_completeTrackTwoTickets() {

		when(ticketRepository.findByCustomerAndUseCase(anyString(), anyString(), anyString(), anyString(),
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(completeTrackTwoTickets);

		when(ticketRepository.getTicket(anyInt())).thenReturn(waSolvedTicket);

		ForbiddenException ex = assertThrows(ForbiddenException.class, () -> {
			retrieveTicketsUseCase.pendingTicket("CMS", "broadband", "10400884", "DNI", "70981983");
		});

		assertEquals(ex.getMessage(), "User can´t create more tickets");
	}

	@Test
	void getPendingTicket_pastOnePendingTicket() {

		when(ticketRepository.findByCustomerAndUseCase(anyString(), anyString(), anyString(), anyString(),
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(new ArrayList<>());

		when(ticketRepository.findByCustomerAndUseCasePast(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(pendingOneTicket);

		when(ticketRepository.getTicket(anyInt())).thenReturn(pendingTicket);

		when(ticketRepository.getAdditionalData(any(Ticket.class))).thenReturn(new ArrayList<>());

		ResponseEntity<TicketStatusResponse> response = retrieveTicketsUseCase.pendingTicket("CMS", "broadband",
				"10400884", "DNI", "70981983");

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNotNull(response.getBody().getTicketId());
		assertEquals(response.getBody().getTicketId(), 19406743);
	}

	@Test
	void getPendingTicket_pastOneSolvedTicket() {

		when(ticketRepository.findByCustomerAndUseCase(anyString(), anyString(), anyString(), anyString(),
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(new ArrayList<>());

		when(ticketRepository.findByCustomerAndUseCasePast(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(solvedOneTicket);

		when(ticketRepository.getTicket(anyInt())).thenReturn(solvedTicket);

		ResponseEntity<TicketStatusResponse> response = retrieveTicketsUseCase.pendingTicket("CMS", "broadband",
				"10400884", "DNI", "70981983");

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNull(response.getBody().getTicketId());
	}
	
	@Test
	void getPendingTicket_completePastTrackTwoTickets() {
		
		when(ticketRepository.findByCustomerAndUseCase(anyString(), anyString(), anyString(), anyString(),
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(new ArrayList<>());

		when(ticketRepository.findByCustomerAndUseCasePast(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(completeTrackTwoTickets);

		ResponseEntity<TicketStatusResponse> response = retrieveTicketsUseCase.pendingTicket("CMS", "broadband",
				"10400884", "DNI", "70981983");

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNull(response.getBody().getTicketId());
	}

}
