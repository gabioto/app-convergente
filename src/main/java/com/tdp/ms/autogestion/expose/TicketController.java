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
import com.tdp.ms.autogestion.business.RetrieveTicketStatusUseCase;
import com.tdp.ms.autogestion.business.RetrieveTicketsUseCase;
import com.tdp.ms.autogestion.business.UpdateTicketStatusUseCase;
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

	@Autowired
	private RetrieveTicketsUseCase retrieveTicketsUseCase;
	
	@Autowired
	private RetrieveTicketStatusUseCase retrieveTicketStatusUseCase;
	
	@Autowired
	private UpdateTicketStatusUseCase updateTicketStatusUseCase;

	// Creación de ticket
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<TicketCreateResponse> createTicket(@RequestBody TicketCreateRequest request) {
		return createTicketUseCase.createTicket(request);
	}

	// Consulta de bandeja
	@PostMapping("/retrieveTickets")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<TicketStatusResponse> retrieveTickets(@RequestBody TicketRetrieveRequest request) {
		return retrieveTicketsUseCase.pendingTicket(request);
	}

	// Datos del ticket
	@GetMapping("/{id}/status")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<TicketStatusResponse> retrieveTicketStatus(@PathVariable String id) {
		return retrieveTicketStatusUseCase.retrieveTicketStatus(id);
	}

	// Actualización de estado de tickets
	@PatchMapping("/{id}/{status}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<TicketStatusResponse> updateTicketStatus(@PathVariable int id, @PathVariable String status) {
		return updateTicketStatusUseCase.updateTicketStatus(id, status);
	}

}
