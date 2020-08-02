package com.tdp.ms.autogestion.business;

import org.springframework.http.ResponseEntity;

import com.tdp.ms.autogestion.model.TicketCreateRequest;
import com.tdp.ms.autogestion.model.TicketCreateResponse;
import com.tdp.ms.autogestion.model.TicketRetrieveRequest;
import com.tdp.ms.autogestion.model.TicketStatusResponse;

public interface CreateTicketUseCase {
	
	ResponseEntity<TicketCreateResponse> createTicket(TicketCreateRequest request);
	ResponseEntity<TicketStatusResponse> pendingTicket(TicketRetrieveRequest request);
}
