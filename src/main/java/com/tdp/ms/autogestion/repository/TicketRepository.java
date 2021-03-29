package com.tdp.ms.autogestion.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.tdp.ms.autogestion.model.AdditionalData;
import com.tdp.ms.autogestion.model.Equivalence;
import com.tdp.ms.autogestion.model.EquivalenceNotification;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;

public interface TicketRepository {

	Ticket generateTicket(OAuth pOAuth, Ticket pTicket);

	void saveGeneratedTicket(Ticket pTicket);

	Ticket updateTicketStatus(int idTicket, String status);

	List<Ticket> findByCustomerAndUseCase(String docType, String docNumber, String reference, String involvement,
			LocalDateTime creationDate, LocalDateTime endDate);

	List<Ticket> findByCustomerAndUseCasePast(String docType, String docNumber, String reference, String involvement);

	Ticket getTicket(int idTicket);

	List<AdditionalData> getValue(Integer attachmentId, String field);	
	
	List<AdditionalData> getAdditionalData(Ticket ticket, int minutes);

	List<Equivalence> getAttachmentEquivalence(Integer ticketId);

	EquivalenceNotification getNotificationEquivalence(String code, String usecase);
	
	List<Ticket> getTicketStatus(Integer ticketId);
}
