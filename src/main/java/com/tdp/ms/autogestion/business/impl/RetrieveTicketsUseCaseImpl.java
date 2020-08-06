package com.tdp.ms.autogestion.business.impl;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.tdp.ms.autogestion.repository.datasource.db.JpaAttachmentAdditionalDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaCustomerRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceNotificationRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAdditionalData;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachment;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachmentAdditionalData;
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

	@Autowired
	TicketApi ticketApi;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	JpaAdditionalDataRepository additionalDataRepository;

	@Autowired
	JpaAttachmentAdditionalDataRepository attachmentAdditionalDataRepository;
	
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
		if (lstAttachment != null && lstAttachment.size() > 0) {
			for (TblAttachment tblAttachment : lstAttachment) {
				// Obtener el monto adeudado por el cliente
				if (tblAttachment.getNameAttachment().equals("ValidacionesInicialesInternet[{}]recupera-deuda-amdocs") ||
					tblAttachment.getNameAttachment().equals("ValidacionesInicialesInternet[{}]recupera-deuda-cms") ||
					tblAttachment.getNameAttachment().equals("ValidacionesInicialesInternet[{}]recupera-deuda-atis")) {
					
					Optional<List<TblAttachmentAdditionalData>> tableAttachmentAdditionalData = attachmentAdditionalDataRepository
							.getMontoDeuda(tblAttachment.getIdAttachment(), "monto");
					if (tableAttachmentAdditionalData.isPresent()) {
						List<TblAttachmentAdditionalData> lstAttachmentAdditionalData = tableAttachmentAdditionalData.get();
						for (TblAttachmentAdditionalData tblAttachmentAdditionalData : lstAttachmentAdditionalData) {
							clienteData = new AdditionalData();
							clienteData.setKey(Constants.LABEL_MONTO);
							clienteData.setValue(tblAttachmentAdditionalData.getValueAttachmentAdditional());
							lstClienteData.add(clienteData);
						}
					}							
				}
				if (tblAttachment.getNameAttachment().equals("AveriaPendiente[{}]recupera-averia-pendiente-amdocs") ||
					tblAttachment.getNameAttachment().equals("AveriaPendiente[{}]recupera-averia-pendiente-cms") ||
					tblAttachment.getNameAttachment().equals("AveriaPendiente[{}]recupera-averia-pendiente-gestel")) {
					
					Optional<List<TblAttachmentAdditionalData>> tableAttachmentAdditionalData = attachmentAdditionalDataRepository
							.getInfoAveria(tblAttachment.getIdAttachment());
					if (tableAttachmentAdditionalData.isPresent()) {
						List<TblAttachmentAdditionalData> lstAttachmentAdditionalData = tableAttachmentAdditionalData.get();
						for (TblAttachmentAdditionalData tblAttachmentAdditionalData : lstAttachmentAdditionalData) {									
							if (tblAttachmentAdditionalData.getKeyAttachmentAdditional().equals("codigo_averia")) {									
								clienteData = new AdditionalData();
								clienteData.setKey(Constants.LABEL_COD_AVERIA);
								clienteData.setValue(tblAttachmentAdditionalData.getValueAttachmentAdditional());
								lstClienteData.add(clienteData);
							}
						}
					}
				}
			}
			
			// Equivalencias
			Optional<List<TblEquivalence>> tableEquivalence = equivalenceRepository
					.getEquivalence(tableTicket.getIdTicket());
			if (tableEquivalence.isPresent()) {
				int index = 1;
				List<TblEquivalence> lstEquivalence = tableEquivalence.get();
				for (TblEquivalence tblEquivalence : lstEquivalence) {
					clienteData = new AdditionalData();					
					clienteData.setKey(Constants.LABEL_FRONT_END.concat(String.valueOf(index)));
					clienteData.setValue(tblEquivalence.getNameEquivalence());
					lstClienteData.add(clienteData);
					index++;
				}
			}
		}
		List<TblAdditionalData> lstAdditionalData = tableTicket.getTblAdditionalData();
		if (lstAdditionalData != null && lstAdditionalData.size() > 0) {
			for (TblAdditionalData tblAdditionalData : lstAdditionalData) {
				if (tblAdditionalData.getKeyAdditional().equals("notification-id")) {
					Optional<TblEquivalenceNotification> tableEquivalence = equivalenceNotificationRepository
							.getEquivalence(tblAdditionalData.getValueAdditional());
					if (tableEquivalence.isPresent()) {
						TblEquivalenceNotification equivalence = tableEquivalence.get();

						clienteData = new AdditionalData();
						clienteData.setKey(Constants.LABEL_ACTION);
						clienteData.setValue(equivalence.getAction() != null ? equivalence.getAction() : "");
						lstClienteData.add(clienteData);

						clienteData = new AdditionalData();
						clienteData.setKey(Constants.LABEL_TITLE);
						clienteData.setValue(equivalence.getTitle() != null ? equivalence.getTitle() : "");
						lstClienteData.add(clienteData);

						clienteData = new AdditionalData();

						clienteData.setKey(Constants.LABEL_TITLE_DESC);
						clienteData.setValue(
								equivalence.getDescription() != null ? equivalence.getDescription() : "");
						lstClienteData.add(clienteData);

						clienteData = new AdditionalData();
						clienteData.setKey(Constants.LABEL_BODY);
						clienteData.setValue(equivalence.getBody() != null ? equivalence.getBody() : "");
						lstClienteData.add(clienteData);

						clienteData = new AdditionalData();
						clienteData.setKey(Constants.LABEL_FOOTER);
						clienteData.setValue(equivalence.getFooter() != null ? equivalence.getFooter() : "");
						lstClienteData.add(clienteData);

						clienteData = new AdditionalData();
						clienteData.setKey(Constants.LABEL_ICON);
						clienteData.setValue(equivalence.getIcon() != null ? equivalence.getIcon() : "");
						lstClienteData.add(clienteData);
						
						clienteData = new AdditionalData();
						clienteData.setKey(Constants.LABEL_BUTTON);
						clienteData.setValue(equivalence.getButton() != null ? equivalence.getButton() : "");
						lstClienteData.add(clienteData);
						
						clienteData = new AdditionalData();
						clienteData.setKey(Constants.LABEL_IMAGE);
						clienteData.setValue(equivalence.getImage() != null ? equivalence.getImage() : "");
						lstClienteData.add(clienteData);
						
						clienteData = new AdditionalData();
						clienteData.setKey(Constants.LABEL_ACTION_BUTTON);
						clienteData.setValue(equivalence.getActionbutton() != null ? equivalence.getActionbutton() : "");
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

			List<Integer> lstId = new ArrayList<Integer>();
			if (tableTicket != null && tableTicket.size() > 0) {
				String idTicketTriage = "";				
				for (TblTicket tblTicket : tableTicket) {
					if (idTicketTriage.equals("")) {
						idTicketTriage = tblTicket.getIdTicketTriage().toString();
						lstId.add(new Integer(tblTicket.getIdTicketTriage()));
						log.info("1 - Id Ticket: " + idTicketTriage);
					} else {
						if (!idTicketTriage.equals(tblTicket.getIdTicketTriage().toString())) {
							idTicketTriage = tblTicket.getIdTicketTriage().toString();
							lstId.add(new Integer(tblTicket.getIdTicketTriage()));
							log.info("2 - Id Ticket: " + idTicketTriage);
						}
					}
				}
			}
			
			if (lstId.size() > 0) {
				ticketStatusResponse = new TicketStatusResponse();
				if (lstId.size() == 1) {
					TblTicket tblTicket = ticketRepository.getTicket(lstId.get(0));
					
					// Cuando solo tiene un ticket
					if (!tblTicket.getStatusTicket().equalsIgnoreCase(TicketStatus.SOLVED.name()) &&
						!tblTicket.getStatusTicket().equalsIgnoreCase(TicketStatus.WA_SOLVED.name()) &&
						!tblTicket.getStatusTicket().equalsIgnoreCase(TicketStatus.FAULT_SOLVED.name()) &&
						!tblTicket.getStatusTicket().equalsIgnoreCase(TicketStatus.GENERIC_SOLVED.name())) {

						ticketStatusResponse = new TicketStatusResponse(tblTicket.getIdTicketTriage(),
								tblTicket.getDescription(), tblTicket.getCreationDate(), tblTicket.getTicketType(),
								tblTicket.getStatusChangeDate(), tblTicket.getStatusTicket(), tblTicket.getModifiedDateTicket(), fillTicket(tblTicket));

						return new ResponseEntity<>(ticketStatusResponse, HttpStatus.OK);
					} else {
						// Puede crear Ticket
						return new ResponseEntity<>(ticketStatusResponse, HttpStatus.NO_CONTENT);
					}
				} else {
					TblTicket tblTicket = ticketRepository.getTicket(lstId.get(1));
					
					if (!tblTicket.getStatusTicket().equalsIgnoreCase(TicketStatus.SOLVED.name()) &&
						!tblTicket.getStatusTicket().equalsIgnoreCase(TicketStatus.WA_SOLVED.name()) &&
						!tblTicket.getStatusTicket().equalsIgnoreCase(TicketStatus.FAULT_SOLVED.name()) &&
						!tblTicket.getStatusTicket().equalsIgnoreCase(TicketStatus.GENERIC_SOLVED.name())) {

						ticketStatusResponse = new TicketStatusResponse(tblTicket.getIdTicketTriage(),
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