package com.tdp.ms.autogestion.expose;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.tdp.ms.autogestion.repository.TicketRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/trazabilidad/v1/tickets")
public class TicketController {

	@Autowired
	private CreateTicketUseCase createTicketUseCase;
	
	@Autowired
	private TicketRepository ticketService;

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
	
	// Datos del ticket
	@GetMapping("/{id}/status")	
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<TicketStatusResponse> retrieveTicketStatus(@PathVariable String id) {
		return ticketService.retrieveTicketStatus(id);
	}	
	
	@PatchMapping("/{id}/{status}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<TicketStatusResponse> updateTicketStatus(@PathVariable int id, @PathVariable String status) {
		return ticketService.updateTicketStatus(id, status);
	}

}
