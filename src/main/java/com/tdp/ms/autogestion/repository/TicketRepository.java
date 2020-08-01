package com.tdp.ms.autogestion.repository;

import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;

public interface TicketRepository {

	Ticket generateTicket(OAuth pOAuth, Ticket pTicket);
	
	void saveGeneratedTicket(Ticket pTicket);
	
	Ticket getTicketStatus(String idTicket);
}
