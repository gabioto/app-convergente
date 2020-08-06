package com.tdp.ms.autogestion.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalence;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalenceNotification;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;

public interface TicketRepository {

	Ticket generateTicket(OAuth pOAuth, Ticket pTicket);

	void saveGeneratedTicket(Ticket pTicket);
	
	Ticket updateTicketStatus(int idTicket, String status) throws Exception;

	List<TblTicket> findByCustomerAndUseCase(String docType, String docNumber, String reference, String involvement,
			LocalDateTime creationDate, LocalDateTime endDate);

	TblTicket getTicket(int idTicket);

	List<TblEquivalence> getEquivalence(int idTicket);

	TblEquivalenceNotification getEquivalenceNotification(String code);
	
}
