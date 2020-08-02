package com.tdp.ms.autogestion.business.impl;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
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
import com.tdp.ms.autogestion.model.TicketCreateResponse;
import com.tdp.ms.autogestion.model.TicketRetrieveRequest;
import com.tdp.ms.autogestion.model.TicketStatusResponse;
import com.tdp.ms.autogestion.model.TicketCreateRequest.AdditionalData;
import com.tdp.ms.autogestion.repository.OAuthRepository;
import com.tdp.ms.autogestion.repository.TicketRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAdditionalData;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachment;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalence;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalenceNotification;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;
import com.tdp.ms.autogestion.util.Constants;
import com.tdp.ms.autogestion.util.TicketStatus;
import com.tdp.ms.autogestion.model.TicketStatusResponse.ClienteData;




@Service
public class CreateTicketUsecaseImpl implements CreateTicketUseCase  {
	
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
				// TODO: Trabajar en la excepción
//				GenesisExceptionBuilder builder = GenesisException.builder();
//				throw builder.exceptionId("SVC1000").category(ErrorCategory.INVALID_REQUEST).build();
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
//		} catch (GenesisException e) {
//			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	
	private String validateRequestAdditionalData(List<AdditionalData> data, String value) {
		AdditionalData field = data.stream().filter(item -> value.equals(item.getKey())).findFirst().orElse(null);

		return (field != null && field.getValue() != null) ? field.getValue() : "";
	}
	private List<ClienteData> fillTicket(TblTicket tableTicket) {

		List<ClienteData> lstClienteData = new ArrayList<ClienteData>();

		ClienteData clienteData = new ClienteData();
		clienteData.setKey("status");
		clienteData.setValue(tableTicket.getStatus());
		lstClienteData.add(clienteData);

		List<TblAttachment> lstAttachment = tableTicket.getTblAttachments();
		if (lstAttachment.size() > 0 && lstAttachment.size() > 0) {
			List<TblEquivalence> tableEquivalence = ticketRepository
					.getEquivalence(tableTicket.getIdTicket());
			if (tableEquivalence != null) {
				List<TblEquivalence> lstEquivalence = tableEquivalence;
				for (TblEquivalence tblEquivalence : lstEquivalence) {
					clienteData = new ClienteData();
					clienteData.setKey(tblEquivalence.getAttachmentName());
					clienteData.setValue(tblEquivalence.getNameEquivalence());
					lstClienteData.add(clienteData);
				}
			}
		}
		List<TblAdditionalData> lstAdditionalData = tableTicket.getTblAdditionalData();
		if (lstAdditionalData.size() > 0 && lstAdditionalData.size() > 0) {
			for (TblAdditionalData tblAdditionalData : lstAdditionalData) {
				if (tblAdditionalData.getKeyAdditional().equals("notification-id")) {
					TblEquivalenceNotification tableEquivalence = ticketRepository
							.getEquivalenceNotification(tblAdditionalData.getValueAdditional());
					if (tableEquivalence != null) {
						TblEquivalenceNotification equivalence = tableEquivalence;

						clienteData = new ClienteData();
						clienteData.setKey("action");
						clienteData.setValue(equivalence.getAction() != null ? equivalence.getAction() : "");
						lstClienteData.add(clienteData);

						clienteData = new ClienteData();
						clienteData.setKey("title");
						clienteData.setValue(equivalence.getTitle() != null ? equivalence.getTitle() : "");
						lstClienteData.add(clienteData);

						clienteData = new ClienteData();
						clienteData.setKey("description_title");
						clienteData.setValue(
								equivalence.getDescriptiontitle() != null ? equivalence.getDescriptiontitle() : "");
						lstClienteData.add(clienteData);

						clienteData = new ClienteData();
						clienteData.setKey("body");
						clienteData.setValue(equivalence.getBody() != null ? equivalence.getBody() : "");
						lstClienteData.add(clienteData);

						clienteData = new ClienteData();
						clienteData.setKey("footer");
						clienteData.setValue(equivalence.getFooter() != null ? equivalence.getFooter() : "");
						lstClienteData.add(clienteData);

						clienteData = new ClienteData();
						clienteData.setKey("icon");
						clienteData.setValue(equivalence.getIcon() != null ? equivalence.getIcon() : "");
						lstClienteData.add(clienteData);
					}

				}
			}
		}

		return lstClienteData;
	}
	@Override
	public ResponseEntity<TicketStatusResponse> pendingTicket(TicketRetrieveRequest request) {

		LocalDate today = LocalDate.now(ZoneOffset.of(Constants.ZONE_OFFSET));

		TicketStatusResponse ticketStatusResponse = null;
		
		try {

			List<TblTicket> tableTicket = ticketRepository.findByCustomerAndUseCase(request.getNationalIdType(), request.getNationalId(),
					request.getReference(), request.getInvolvement(), today.atStartOfDay(), today.atStartOfDay().plusDays(1));

			if (tableTicket.size() > 0) {

				ticketStatusResponse = new TicketStatusResponse();
				if (tableTicket.size() == 1) {
					// Cuando solo tiene un ticket
					if (!tableTicket.get(0).getStatus().equalsIgnoreCase(TicketStatus.SOLVED.name())
							|| !tableTicket.get(0).getStatus().equalsIgnoreCase(TicketStatus.WA_SOLVED.name())
							|| !tableTicket.get(0).getStatus().equalsIgnoreCase(TicketStatus.FAULT_SOLVED.name())
							|| !tableTicket.get(0).getStatus().equalsIgnoreCase(TicketStatus.GENERIC_SOLVED.name())) {

						ticketStatusResponse = new TicketStatusResponse(tableTicket.get(0).getStatusChangeDate(),
								tableTicket.get(0).getStatus(), tableTicket.get(0).getStatus(),
								fillTicket(tableTicket.get(0)));

						return new ResponseEntity<>(ticketStatusResponse, HttpStatus.OK);
					} else {
						//Puede crear Ticket
						return new ResponseEntity<>(ticketStatusResponse, HttpStatus.NO_CONTENT);

					}

				} else {
					if (!tableTicket.get(1).getStatus().equalsIgnoreCase(TicketStatus.SOLVED.name())
							|| !tableTicket.get(1).getStatus().equalsIgnoreCase(TicketStatus.WA_SOLVED.name())
							|| !tableTicket.get(1).getStatus().equalsIgnoreCase(TicketStatus.FAULT_SOLVED.name())
							|| !tableTicket.get(1).getStatus().equalsIgnoreCase(TicketStatus.GENERIC_SOLVED.name())) {

						ticketStatusResponse = new TicketStatusResponse(tableTicket.get(1).getStatusChangeDate(),
								tableTicket.get(1).getStatus(), tableTicket.get(1).getStatus(),
								fillTicket(tableTicket.get(1)));

						return new ResponseEntity<>(ticketStatusResponse, HttpStatus.OK);
					} else {
						// Ya no puede crear Ticket
						return new ResponseEntity<>(ticketStatusResponse, HttpStatus.UNAUTHORIZED);
					}

				}

			}
			return new ResponseEntity<>(ticketStatusResponse, HttpStatus.NO_CONTENT);
			
		} catch (Exception e) {
			return new ResponseEntity<>(ticketStatusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}

}
