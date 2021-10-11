package com.tdp.ms.autogestion.expose;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.tdp.ms.autogestion.business.CreateTicketUseCase;
import com.tdp.ms.autogestion.business.RetrieveTicketStatusUseCase;
import com.tdp.ms.autogestion.business.RetrieveTicketsUseCase;
import com.tdp.ms.autogestion.business.UpdateTicketStatusUseCase;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest;
import com.tdp.ms.autogestion.expose.entities.TicketCreateResponse;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse;
import com.tdp.ms.autogestion.util.Constants;

@RestController
@EnableWebMvc
@Validated
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
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<TicketCreateResponse> createTicket(@Valid @RequestBody TicketCreateRequest request)
			throws Exception {
		return createTicketUseCase.createTicket(request);
	}

	// Consulta de bandeja
	@GetMapping("/retrieveTickets")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<TicketStatusResponse> retrieveTickets(@NotEmpty @RequestParam String type,
			@NotEmpty @RequestParam String involvement, @NotEmpty @RequestParam String reference,
			@NotEmpty @RequestParam String nationalIdType, @NotEmpty @RequestParam String nationalId) {
		return retrieveTicketsUseCase.pendingTicket(type, involvement, reference, nationalIdType, nationalId);
	}

	// Datos del ticket
	@GetMapping("/{id}/status")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<TicketStatusResponse> retrieveTicketStatus(@PathVariable @Min(value = 1, message = Constants.MSG_NOT_EMPTY) int id) {
		return retrieveTicketStatusUseCase.retrieveTicketStatus(id);
	}

	// Actualización de estado de tickets
	@PatchMapping("/{id}/{status}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<TicketStatusResponse> updateTicketStatus(
			@PathVariable @Min(value = 1, message = Constants.MSG_NOT_EMPTY) int id,
			@PathVariable @NotEmpty String status) throws Exception {
		return updateTicketStatusUseCase.updateTicketStatus(id, status);
	}

}