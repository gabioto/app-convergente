package com.tdp.ms.autogestion.autogestion.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.tdp.ms.autogestion.model.Ticket;

public class TicketTest {

	@Test
	void equals() {
		Ticket lTicket = new Ticket();
		Ticket oTicket = new Ticket();

		assertEquals(true, lTicket.equals(oTicket));
	}

	@Test
	void notEquals() {
		Ticket lTicket = new Ticket();
		Ticket oTicket = new Ticket(0, 0, "", "", null, "", "", "", null, "", 0, "", "", "", "", "", "", "", null, "",
				null, null, null);

		assertEquals(false, lTicket.equals(oTicket));
	}

	@Test
	void toStringTest() {
		Ticket lTicket = new Ticket();

		assertEquals(
				"Ticket(id=null, idTriage=null, href=null, description=null, "
						+ "creationDate=null, severity=null, type=null, status=null, "
						+ "statusChangeDate=null, statusChangeReason=null, priority=null, "
						+ "technology=null, useCaseId=null, subOperationCode=null, productIdentifier=null, "
						+ "involvement=null, channelId=null, channelName=null, customer=null, "
						+ "ticketStatus=null, modifiedDateTicket=null, additionalData=[], attachments=[])",
				String.valueOf(lTicket));
	}

	@Test
	void hashCodeTest() {
		Ticket lTicket = new Ticket();

		assertEquals(-1771112902, lTicket.hashCode());
	}
}
