package com.tdp.ms.autogestion.business.impl;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.business.RetrieveTicketsUseCase;
import com.tdp.ms.autogestion.expose.entities.TicketRetrieveRequest;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse.AdditionalData;
import com.tdp.ms.autogestion.model.TicketStatus;
import com.tdp.ms.autogestion.repository.TicketRepository;
import com.tdp.ms.autogestion.repository.datasource.api.TicketApi;
import com.tdp.ms.autogestion.repository.datasource.db.JpaAdditionalDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaCustomerRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceNotificationRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAdditionalData;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachment;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalence;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalenceNotification;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;
import com.tdp.ms.autogestion.util.Constants;

/**
 * Class: TrazabilidadpruebaServiceImpl. <br/>
 * <b>Copyright</b>: &copy; 2019 Telef&oacute;nica del Per&uacute;<br/>
 * <b>Company</b>: Telef&oacute;nica del Per&uacute;<br/>
 *
 * @author Telef&oacute;nica del Per&uacute; (TDP) <br/>
 *         <u>Service Provider</u>: Everis Per&uacute; SAC (EVE) <br/>
 *         <u>Developed by</u>: <br/>
 *         <ul>
 *         <li>Developer name</li>
 *         </ul>
 *         <u>Changes</u>:<br/>
 *         <ul>
 *         <li>YYYY-MM-DD Creaci&oacute;n del proyecto.</li>
 *         </ul>
 * @version 1.0
 */
@Service
public class RetrieveTicketsUseCaseImpl implements RetrieveTicketsUseCase {

	private static final Log log = LogFactory.getLog(RetrieveTicketsUseCaseImpl.class);
	private static final String TAG = RetrieveTicketsUseCaseImpl.class.getCanonicalName();

	@Autowired
	TicketApi ticketApi;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	JpaAdditionalDataRepository additionalDataRepository;

	@Autowired
	JpaEquivalenceRepository equivalenceRepository;

	@Autowired
	JpaEquivalenceNotificationRepository equivalenceNotificationRepository;

	@Autowired
	JpaCustomerRepository customerRepository;

	private List<AdditionalData> fillTicket(TblTicket tableTicket) {

		List<AdditionalData> lstClienteData = new ArrayList<AdditionalData>();

		AdditionalData clienteData = new AdditionalData();
		clienteData.setKey("status");
		clienteData.setValue(tableTicket.getStatus());
		lstClienteData.add(clienteData);

		List<TblAttachment> lstAttachment = tableTicket.getTblAttachments();
		if (lstAttachment.size() > 0 && lstAttachment.size() > 0) {
			List<TblEquivalence> tableEquivalence = ticketRepository.getEquivalence(tableTicket.getIdTicket());
			if (tableEquivalence != null) {
				List<TblEquivalence> lstEquivalence = tableEquivalence;
				for (TblEquivalence tblEquivalence : lstEquivalence) {
					clienteData = new AdditionalData();
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

						clienteData = new AdditionalData();
						clienteData.setKey("action");
						clienteData.setValue(equivalence.getAction() != null ? equivalence.getAction() : "");
						lstClienteData.add(clienteData);

						clienteData = new AdditionalData();
						clienteData.setKey("title");
						clienteData.setValue(equivalence.getTitle() != null ? equivalence.getTitle() : "");
						lstClienteData.add(clienteData);
						
						clienteData = new AdditionalData();
						clienteData.setKey("body");
						clienteData.setValue(equivalence.getBody() != null ? equivalence.getBody() : "");
						lstClienteData.add(clienteData);

						clienteData = new AdditionalData();
						clienteData.setKey("footer");
						clienteData.setValue(equivalence.getFooter() != null ? equivalence.getFooter() : "");
						lstClienteData.add(clienteData);

						clienteData = new AdditionalData();
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

			List<TblTicket> tableTicket = ticketRepository.findByCustomerAndUseCase(request.getNationalIdType(),
					request.getNationalId(), request.getRelatedObject().getReference(),
					request.getRelatedObject().getInvolvement(), today.atStartOfDay(),
					today.atStartOfDay().plusDays(1));

			if (tableTicket.size() > 0) {

				ticketStatusResponse = new TicketStatusResponse();
				if (tableTicket.size() == 1) {
					// Cuando solo tiene un ticket
					if (!tableTicket.get(0).getStatus().equalsIgnoreCase(TicketStatus.SOLVED.name())
							|| !tableTicket.get(0).getStatus().equalsIgnoreCase(TicketStatus.WA_SOLVED.name())
							|| !tableTicket.get(0).getStatus().equalsIgnoreCase(TicketStatus.FAULT_SOLVED.name())
							|| !tableTicket.get(0).getStatus().equalsIgnoreCase(TicketStatus.GENERIC_SOLVED.name())) {

						TblTicket tblTicket = tableTicket.get(0);

						ticketStatusResponse = new TicketStatusResponse(tblTicket.getIdTicket(),
								tblTicket.getDescription(), tblTicket.getCreationDate(), tblTicket.getTicketType(),
								tblTicket.getStatusChangeDate(), tblTicket.getStatusTicket(), tblTicket.getModifiedDateTicket(), fillTicket(tblTicket));

						return new ResponseEntity<>(ticketStatusResponse, HttpStatus.OK);
					} else {
						// Puede crear Ticket
						return new ResponseEntity<>(ticketStatusResponse, HttpStatus.NO_CONTENT);

					}

				} else {
					if (!tableTicket.get(1).getStatus().equalsIgnoreCase(TicketStatus.SOLVED.name())
							|| !tableTicket.get(1).getStatus().equalsIgnoreCase(TicketStatus.WA_SOLVED.name())
							|| !tableTicket.get(1).getStatus().equalsIgnoreCase(TicketStatus.FAULT_SOLVED.name())
							|| !tableTicket.get(1).getStatus().equalsIgnoreCase(TicketStatus.GENERIC_SOLVED.name())) {

						TblTicket tblTicket = tableTicket.get(1);

						ticketStatusResponse = new TicketStatusResponse(tblTicket.getIdTicket(),
								tblTicket.getDescription(), tblTicket.getCreationDate(), tblTicket.getTicketType(),
								tblTicket.getStatusChangeDate(), tblTicket.getStatusTicket(), tblTicket.getModifiedDateTicket(), fillTicket(tblTicket));

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