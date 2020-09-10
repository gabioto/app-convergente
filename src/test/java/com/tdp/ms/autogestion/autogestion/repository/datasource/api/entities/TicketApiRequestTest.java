package com.tdp.ms.autogestion.autogestion.repository.datasource.api.entities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.datasource.api.entities.TicketApiRequest;

public class TicketApiRequestTest {

	private static Ticket ticket, oTicket, lTicket;

	@BeforeAll
	public static void setup() {
		ticket = new Ticket(2, 141586, "/ref", "TroubleTicket", null, "minor", "averia", "", null, "", 1, "HFC", "99",
				"", "serviceCode", "", "3", "", new Customer("70707070", "DNI", "08000"), "", null, null, null);

		oTicket = new Ticket(2, 141586, "/ref", "TroubleTicket", null, "minor", "averia", "", null, "", 1, "HFC", "99",
				"1245", "phone", "", null, "", new Customer("70707070", "DNI", "08000"), "", null, null, null);
		
		lTicket = new Ticket(2, 141586, "/ref", "TroubleTicket", null, "minor", "averia", "", null, "", 1, "HFC", "99",
				"1245", "", "", null, "", new Customer("70707070", "DNI", "08000"), "", null, null, null);
	}

	@Test
	void generateTicketServiceCode() {
		TicketApiRequest ticketApiRequest = new TicketApiRequest();

		ticketApiRequest.generateRequest(ticket);
	}

	@Test
	void generateTicketPhone() {

		TicketApiRequest ticketApiRequest = new TicketApiRequest();

		ticketApiRequest.generateRequest(oTicket);
	}
	
	@Test
	void generateTicket() {

		TicketApiRequest ticketApiRequest = new TicketApiRequest();

		ticketApiRequest.generateRequest(lTicket);
	}
}
