package com.tdp.ms.autogestion.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.business.CreateTicketUseCase;
import com.tdp.ms.autogestion.exception.DomainException;
import com.tdp.ms.autogestion.exception.ErrorCategory;
import com.tdp.ms.autogestion.exception.GenericDomainException;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.AdditionalData;
import com.tdp.ms.autogestion.expose.entities.TicketCreateResponse;
import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.OAuthRepository;
import com.tdp.ms.autogestion.repository.TicketRepository;
import com.tdp.ms.autogestion.util.Constants;

@Service
public class CreateTicketUsecaseImpl implements CreateTicketUseCase {

	@Autowired
	private OAuthRepository oAuthRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Override
	public ResponseEntity<TicketCreateResponse> createTicket(TicketCreateRequest request)
			throws GenericDomainException {
		OAuth oAuth;
		Ticket ticket;

		try {
			String documentType = getAdditionalData(request.getAdditionalData(), "nationalIdType");

			String documentNumber = getAdditionalData(request.getAdditionalData(), "nationalId");

			String technology = getAdditionalData(request.getAdditionalData(), "technology");

			String productIdentifier = getAdditionalData(request.getAdditionalData(), "productIdentifier");

			ticket = request.fromThis();
			ticket.setCustomer(new Customer(documentNumber, documentType, request.getRelatedObject().getReference()));
			ticket.setTechnology(technology);

			if (request.getRelatedObject().getInvolvement().equals(Constants.INTERNET)) {
				ticket.setUseCaseId(Constants.USE_CASE);
			} else if (request.getRelatedObject().getInvolvement().equals(Constants.FIJA)) {
				ticket.setUseCaseId("");
			} else if (request.getRelatedObject().getInvolvement().equals(Constants.CABLE)) {
				ticket.setUseCaseId("");
			} else if (request.getRelatedObject().getInvolvement().equals(Constants.MOVIL)) {
				ticket.setUseCaseId("");
			}
			ticket.setSubOperationCode(Constants.SUB_OPERATION_CODE);
			ticket.setProductIdentifier(productIdentifier);

			// Invocación a API generación de ticket
			oAuth = oAuthRepository.getOAuthValues();
			ticket = ticketRepository.generateTicket(oAuth, ticket);

			// Mapeo e inserción de datos de ticket y cliente
			ticketRepository.saveGeneratedTicket(ticket);

			return new ResponseEntity<>(TicketCreateResponse.from(ticket), HttpStatus.OK);
		} catch (DomainException e) {
			throw e;
		} catch (Exception e) {
			throw new GenericDomainException(ErrorCategory.UNEXPECTED, e.getLocalizedMessage());
		}
	}

	private String getAdditionalData(List<AdditionalData> data, String value) {
		if (!value.equals("technology")) {
			AdditionalData field = data.stream().filter(item -> value.equals(item.getKey())).findFirst().orElse(null);
			return field.getValue();
		} else {
			AdditionalData field = data.stream().filter(item -> value.equals(item.getKey())).findFirst().orElse(null);
			if (field != null) {
				return field.getValue();
			} else {
				return "";
			}
		}
	}

}