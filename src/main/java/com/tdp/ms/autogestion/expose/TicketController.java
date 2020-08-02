package com.tdp.ms.autogestion.expose;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tdp.ms.autogestion.business.CreateTicketUseCase;
import com.tdp.ms.autogestion.model.TicketCreateRequest;
import com.tdp.ms.autogestion.model.TicketCreateResponse;
import com.tdp.ms.autogestion.model.TicketRetrieveRequest;
import com.tdp.ms.autogestion.model.TicketStatusResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/trazabilidad/v1/tickets")
public class TicketController {

	@Autowired
	private CreateTicketUseCase createTicketUseCase;

	// Creación de ticket
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<TicketCreateResponse> createTicket(@RequestBody TicketCreateRequest request) {
		return createTicketUseCase.createTicket(request);
	}

	// Consulta de bandeja
	@ResponseStatus(HttpStatus.OK)
	@PostMapping("/retrieveTickets")
	public ResponseEntity<TicketStatusResponse> retrieveTickets(@RequestBody TicketRetrieveRequest request) {
		return createTicketUseCase.pendingTicket(request);
		// return Mono.justOrEmpty(ticketService.pendingTicket(request));

		// return
		// ResponseEntity.status(status).body(ticketService.pendingTicket(request));
	}
}
