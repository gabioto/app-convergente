package com.tdp.ms.autogestion.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.model.TicketStatusResponse;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalence;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalenceNotification;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;

public interface TicketRepository {

	Ticket generateTicket(OAuth pOAuth, Ticket pTicket);

	void saveGeneratedTicket(Ticket pTicket);

	List<TblTicket> findByCustomerAndUseCase(String docType, String docNumber, String reference, String involvement,
			LocalDateTime creationDate, LocalDateTime endDate);

	Ticket getTicketStatus(String idTicket);

	List<TblEquivalence> getEquivalence(int idTicket);

	TblEquivalenceNotification getEquivalenceNotification(String code);
	
	ResponseEntity<TicketStatusResponse> updateTicketStatus(int idTicket, String status);

	ResponseEntity<TicketStatusResponse> retrieveTicketStatus(String idTicket);
}
