package com.tdp.ms.autogestion.business;

import org.springframework.http.ResponseEntity;

import com.tdp.ms.autogestion.model.TicketCreateRequest;
import com.tdp.ms.autogestion.model.TicketCreateResponse;

public interface CreateTicketUseCase {
	
	ResponseEntity<TicketCreateResponse> createTicket(TicketCreateRequest request);
}
