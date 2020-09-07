package com.tdp.ms.autogestion.autogestion.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.TicketRepositoryImpl;
import com.tdp.ms.autogestion.repository.datasource.api.TicketApi;
import com.tdp.ms.autogestion.repository.datasource.db.JpaAttachmentAdditionalDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaCustomerRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceNotificationRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaTicketRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomer;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomerPK;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;

@ExtendWith(MockitoExtension.class)
public class TicketRepositoryTest {

	@InjectMocks
	private TicketRepositoryImpl ticketRepository;

	@Mock
	private TicketApi ticketApi;

	@Mock
	private JpaCustomerRepository jpaCustomerRepository;

	@Mock
	private JpaTicketRepository jpaTicketRepository;

	@Mock
	private JpaEquivalenceRepository jpaEquivalenceRepository;

	@Mock
	private JpaEquivalenceNotificationRepository jpaEquivalenceNotificationRepository;

	@Mock
	private JpaAttachmentAdditionalDataRepository attachmentAdditionalDataRepository;

	private static Map<String, Ticket> ticketResponseMap = new HashMap<>();
	private static Map<String, Ticket> ticketRequestMap = new HashMap<>();
	private static OAuth oAuthResponseMap;
	private static Ticket ticketComplete, ticketInitial;

	@BeforeAll
	public static void setup() throws JsonProcessingException {
		LocalDateTime actualDate = LocalDateTime.now(ZoneOffset.of("-05:00"));
		ticketComplete = new Ticket(0, 19406743, "/ticket/v2/tickets/19406743", "averia", actualDate, "minor",
				"TroubleTicket", "acknowledged", actualDate, "Ticket generado", 1, "", "20000032", "99", "serviceCode",
				"broadband", "3", null, new Customer("70981983", "DNI", "10368606"), null, actualDate,
				new ArrayList<>(), new ArrayList<>());

		ticketInitial = new Ticket(0, 0, null, "averia", null, "minor", "TroubleTicket", null, null, null, 1, "",
				"20000032", "99", "serviceCode", "broadband", "3", null, new Customer("70981983", "DNI", "10368606"),
				null, null, new ArrayList<>(), new ArrayList<>());

		oAuthResponseMap = new OAuth();

		// TICKET RESPONSE
		ticketResponseMap.put("generated_ticket", ticketComplete);

		// TICKET REQUEST
		ticketRequestMap.put("ticket_to_generate", ticketInitial);
		ticketRequestMap.put("generated_ticket", ticketComplete);
	}

	@Test
	void createTicket_generateTicket() {
		when(ticketApi.generate(any(OAuth.class), any(Ticket.class)))
				.thenReturn(ticketResponseMap.get("generated_ticket"));

		Ticket ticket = ticketRepository.generateTicket(oAuthResponseMap, ticketRequestMap.get("ticket_to_generate"));

		assertNotNull(ticket);
	}

	@Test
	void createTicket_saveGeneratedTicketWithCustomer() {
		when(jpaCustomerRepository.findById(any(TblCustomerPK.class))).thenReturn(Optional.of(new TblCustomer()));

		saveGeneratedTicket();
	}

	@Test
	void createTicket_saveGeneratedTicketWithoutCustomer() {
		when(jpaCustomerRepository.findById(any(TblCustomerPK.class))).thenReturn(Optional.empty());

		when(jpaCustomerRepository.save(any())).thenReturn(new TblCustomer());

		saveGeneratedTicket();
	}

	private void saveGeneratedTicket() {
		when(jpaTicketRepository.save(any())).thenReturn(new TblTicket());

		ticketRepository.saveGeneratedTicket(ticketRequestMap.get("generated_ticket"));
	}
}
