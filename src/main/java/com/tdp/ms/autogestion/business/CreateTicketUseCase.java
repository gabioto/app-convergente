package com.tdp.ms.autogestion.business;

import org.springframework.http.ResponseEntity;

import com.tdp.ms.autogestion.exception.GenericDomainException;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest;
import com.tdp.ms.autogestion.expose.entities.TicketCreateResponse;

public interface CreateTicketUseCase {
	
	ResponseEntity<TicketCreateResponse> createTicket(TicketCreateRequest request) throws GenericDomainException;
}
