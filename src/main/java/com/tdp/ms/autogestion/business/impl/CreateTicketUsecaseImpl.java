package com.tdp.ms.autogestion.business.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
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
	
	private static final Log log = LogFactory.getLog(CreateTicketUsecaseImpl.class);
	
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
			switch (request.getRelatedObject().getInvolvement()) {
			case Constants.INTERNET:
				if (technology.equals(Constants.TECHNOLOGY_HFC)) {
					ticket.setUseCaseId(Constants.USE_CASE_INTERNET);
				} else if (technology.equals(Constants.TECHNOLOGY_GPON)) {
					ticket.setUseCaseId(Constants.USE_CASE_INTERNET_GPON);
				} else {
					ticket.setUseCaseId(Constants.USE_CASE_INTERNET);
				}
				break;
			case Constants.FIJA:
				ticket.setUseCaseId(Constants.USE_CASE_FIJA);
				break;
			case Constants.CABLE:
				ticket.setUseCaseId(Constants.USE_CASE_CABLE);
				break;
			case Constants.MOVIL:
				ticket.setUseCaseId(Constants.USE_CASE_MOVIL);				
				break;
			default:
				ticket.setUseCaseId("");
				break;
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
			log.error(this.getClass().getName() + " - Exception: " + e.getLocalizedMessage());
			log.error(this.getClass().getName() + new Gson().toJson(request));
			
			throw e;
		} catch (Exception e) {
			log.error(this.getClass().getName() + " - Exception: " + e.getLocalizedMessage());
			log.error(this.getClass().getName() + new Gson().toJson(request));
			
			throw new GenericDomainException(ErrorCategory.UNEXPECTED, e.getLocalizedMessage());
		}
	}

	private String getAdditionalData(List<AdditionalData> data, String value) {
		AdditionalData field = data.stream().filter(item -> value.equals(item.getKey())).findFirst().orElse(null);
		return (field != null) ? field.getValue() : "";
	}
}