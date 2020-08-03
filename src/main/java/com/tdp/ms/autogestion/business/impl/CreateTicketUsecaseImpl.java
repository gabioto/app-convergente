package com.tdp.ms.autogestion.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.business.CreateTicketUseCase;
import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.model.TicketCreateRequest;
import com.tdp.ms.autogestion.model.TicketCreateRequest.AdditionalData;
import com.tdp.ms.autogestion.model.TicketCreateResponse;
import com.tdp.ms.autogestion.repository.OAuthRepository;
import com.tdp.ms.autogestion.repository.TicketRepository;

@Service
public class CreateTicketUsecaseImpl implements CreateTicketUseCase {

	@Autowired
	private OAuthRepository oAuthRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Override
	public ResponseEntity<TicketCreateResponse> createTicket(TicketCreateRequest request) {
		OAuth oAuth;
		Ticket ticket;

		try {
			// Validación de campos
			String documentType = validateRequestAdditionalData(request.getAdditionalData(), "nationalIdType");

			String documentNumber = validateRequestAdditionalData(request.getAdditionalData(), "nationalId");

			String technology = validateRequestAdditionalData(request.getAdditionalData(), "technology");

			String useCaseId = validateRequestAdditionalData(request.getAdditionalData(), "use-case-id");

			String subOperationCode = validateRequestAdditionalData(request.getAdditionalData(), "sub-operation-code");

			boolean validCustomer = documentType.isEmpty() || documentNumber.isEmpty();
			boolean validParams = technology.isEmpty() || useCaseId.isEmpty() || subOperationCode.isEmpty();

			if (validCustomer || validParams) {
				return new ResponseEntity<>(new TicketCreateResponse(), HttpStatus.UNAUTHORIZED);
			}

			ticket = request.fromThis();
			ticket.setCustomer(new Customer(documentNumber, documentType, request.getRelatedObject().getReference()));
			ticket.setTechnology(technology);
			ticket.setUseCaseId(useCaseId);
			ticket.setSubOperationCode(subOperationCode);

			// Invocación a API generación de ticket
			oAuth = oAuthRepository.getOAuthValues();
			ticket = ticketRepository.generateTicket(oAuth, ticket);

			// Mapeo e inserción de datos de ticket y cliente
			ticketRepository.saveGeneratedTicket(ticket);

			return new ResponseEntity<>(TicketCreateResponse.from(ticket), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new TicketCreateResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String validateRequestAdditionalData(List<AdditionalData> data, String value) {
		AdditionalData field = data.stream().filter(item -> value.equals(item.getKey())).findFirst().orElse(null);

		return (field != null && field.getValue() != null) ? field.getValue() : "";
	}

}
