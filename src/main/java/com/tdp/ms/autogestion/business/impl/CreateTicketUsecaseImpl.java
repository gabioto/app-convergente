package com.tdp.ms.autogestion.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.business.CreateTicketUseCase;
import com.tdp.ms.autogestion.dao.ServiceDao;
import com.tdp.ms.autogestion.exception.DomainException;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.AdditionalData;
import com.tdp.ms.autogestion.expose.entities.TicketCreateResponse;
import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.OAuthRepository;
import com.tdp.ms.autogestion.repository.TicketRepository;

@Service
public class CreateTicketUsecaseImpl implements CreateTicketUseCase {

	@Autowired
	private OAuthRepository oAuthRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	ServiceDao serviceDao;

	@Override
	public ResponseEntity<TicketCreateResponse> createTicket(TicketCreateRequest request) throws Exception {
		OAuth oAuth;
		Ticket ticket;

		try {
			String documentType = getAdditionalData(request.getAdditionalData(), "nationalIdType");

			String documentNumber = getAdditionalData(request.getAdditionalData(), "nationalId");

			String technology = getAdditionalData(request.getAdditionalData(), "technology");

			String useCaseId = getAdditionalData(request.getAdditionalData(), "use-case-id");

			String subOperationCode = getAdditionalData(request.getAdditionalData(), "sub-operation-code");

			ticket = request.fromThis();
			ticket.setCustomer(new Customer(documentNumber, documentType, request.getRelatedObject().getReference()));
			ticket.setTechnology(technology);
			ticket.setUseCaseId(useCaseId);
			ticket.setSubOperationCode(subOperationCode);

			// Invocación a API generación de ticket
			// oAuth = serviceDao.getOauth(1);
			oAuth = oAuthRepository.getOAuthValues();
			ticket = ticketRepository.generateTicket(oAuth, ticket);

			// Mapeo e inserción de datos de ticket y cliente
			ticketRepository.saveGeneratedTicket(ticket);

			return new ResponseEntity<>(TicketCreateResponse.from(ticket), HttpStatus.OK);
		} catch (DomainException e) {
			throw e;
		} catch (Exception e) {
			throw e;
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