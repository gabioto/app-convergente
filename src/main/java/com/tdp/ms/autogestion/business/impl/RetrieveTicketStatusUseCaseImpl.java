package com.tdp.ms.autogestion.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.business.RetrieveTicketStatusUseCase;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse.AdditionalData;
import com.tdp.ms.autogestion.model.TicketStatus;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceNotificationRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaTicketRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAdditionalData;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachment;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachmentAdditionalData;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalence;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalenceNotification;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;
import com.tdp.ms.autogestion.util.FunctionsUtil;

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
public class RetrieveTicketStatusUseCaseImpl implements RetrieveTicketStatusUseCase {

	@Autowired
	JpaTicketRepository ticketRepository;

	@Autowired
	JpaEquivalenceRepository equivalenceRepository;

	@Autowired
	JpaEquivalenceNotificationRepository equivalenceNotificationRepository;

	@Autowired
	FunctionsUtil functionsUtil;

	@Override
	public ResponseEntity<TicketStatusResponse> retrieveTicketStatus(String idTicket) {
		
		TicketStatusResponse ticketStatusResponse = null;
		
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (idTicket != null) {
			Optional<List<TblTicket>> tableTicket = ticketRepository.getTicketStatus(Integer.parseInt(idTicket));
			if (tableTicket.isPresent()) {
				List<AdditionalData> lstClienteData = new ArrayList<AdditionalData>();

				AdditionalData clienteData = new AdditionalData();
				clienteData.setKey("status");
				clienteData.setValue(tableTicket.get().get(0).getStatus());
				lstClienteData.add(clienteData);

				List<TblAttachment> lstAttachment = tableTicket.get().get(0).getTblAttachments();
				if (lstAttachment != null && lstAttachment.size() > 0) {
					Optional<List<TblEquivalence>> tableEquivalence = equivalenceRepository
							.getEquivalence(tableTicket.get().get(0).getIdTicket());
					if (tableEquivalence.isPresent()) {
						List<TblEquivalence> lstEquivalence = tableEquivalence.get();
						for (TblEquivalence tblEquivalence : lstEquivalence) {
							clienteData = new AdditionalData();
							clienteData.setKey(tblEquivalence.getAttachmentName());
							clienteData.setValue(tblEquivalence.getNameEquivalence());
							lstClienteData.add(clienteData);
						}
					}
				}
				List<TblAdditionalData> lstAdditionalData = tableTicket.get().get(0).getTblAdditionalData();
				if (lstAdditionalData != null && lstAdditionalData.size() > 0) {
					for (TblAdditionalData tblAdditionalData : lstAdditionalData) {
						if (tblAdditionalData.getKeyAdditional().equals("notification-id")) {
							Optional<TblEquivalenceNotification> tableEquivalence = equivalenceNotificationRepository
									.getEquivalence(tblAdditionalData.getValueAdditional());
							if (tableEquivalence.isPresent()) {
								TblEquivalenceNotification equivalence = tableEquivalence.get();

								clienteData = new AdditionalData();
								clienteData.setKey("action");
								clienteData.setValue(equivalence.getAction() != null ? equivalence.getAction() : "");
								lstClienteData.add(clienteData);

								clienteData = new AdditionalData();
								clienteData.setKey("title");
								clienteData.setValue(equivalence.getTitle() != null ? equivalence.getTitle() : "");
								lstClienteData.add(clienteData);

								clienteData = new AdditionalData();
								clienteData.setKey("description_title");
								clienteData.setValue(
										equivalence.getDescriptiontitle() != null ? equivalence.getDescriptiontitle()
												: "");
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

				TblTicket tblTicket = tableTicket.get().get(0);

				ticketStatusResponse = new TicketStatusResponse(tblTicket.getIdTicket(), tblTicket.getDescription(),
						tblTicket.getCreationDate(), tblTicket.getTicketType(), tblTicket.getStatusChangeDate(),
						tblTicket.getStatusTicket(), tblTicket.getModifiedDateTicket(), lstClienteData);

				functionsUtil.saveLogData(tableTicket.get().get(0).getIdTicketTriage(),
						tableTicket.get().get(0).getTblCustomer().getId().getDocumentNumber(),
						tableTicket.get().get(0).getTblCustomer().getId().getDocumentType(), "Retrieve Ticket Status",
						"event", null, ticketStatusResponse.toString(), "Retrieve Ticket Status");

				return new ResponseEntity<>(ticketStatusResponse, HttpStatus.OK);
			} else {
				functionsUtil.saveLogData(Integer.parseInt(idTicket), null, null, "Retrieve Ticket Status", "event",
						null, "Ticket No Existe", "Retrieve Ticket Status");

				return new ResponseEntity<>(ticketStatusResponse, HttpStatus.NO_CONTENT);
			}
		} else {
			functionsUtil.saveLogData(0, null, null, "Retrieve Ticket Status", "event", null, "Ticket Nulo",
					"Retrieve Ticket Status");
			return new ResponseEntity<>(ticketStatusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public String getTicketStatus(int idTicket) {
		String status = TicketStatus.IN_PROGRESS.toString();
		Boolean indicadorReset = Boolean.FALSE;

		Optional<List<TblTicket>> tableTicket = ticketRepository.getTicketStatus(idTicket);
		if (tableTicket.isPresent()) {
			List<TblAttachment> lstAttachment = tableTicket.get().get(0).getTblAttachments();
			if (lstAttachment != null && lstAttachment.size() > 0) {
				// Obtener los attachments mapeados para el sistema
				Optional<List<TblEquivalence>> tableEquivalence = equivalenceRepository
						.getEquivalence(tableTicket.get().get(0).getIdTicket());
				if (tableEquivalence.isPresent()) {
					List<TblEquivalence> lstEquivalence = tableEquivalence.get();
					for (TblEquivalence tblEquivalence : lstEquivalence) {
						for (TblAttachment tblAttachment : lstAttachment) {
							// Validar si el attachment existe en la tabla de equivalencias
							if (tblAttachment.getNameAttachment().equals(tblEquivalence.getAttachmentName())) {
								List<TblAttachmentAdditionalData> lstAttachmentAdditionalData = tblAttachment
										.getTblAttachmentAdditionalData();
								for (TblAttachmentAdditionalData tblAttachmentAdditionalData : lstAttachmentAdditionalData) {
									// Validamos si se realizo un reset
									if (tblAttachmentAdditionalData.getKeyAttachmentAdditional()
											.equals("estado-reset-modem-ok")) {
										status = TicketStatus.RESET.toString();
										indicadorReset = Boolean.TRUE;
									}
								}
							}
						}
					}
				}
			}

			List<TblAdditionalData> lstAdditionalData = tableTicket.get().get(0).getTblAdditionalData();
			if (lstAdditionalData != null && lstAdditionalData.size() > 0) {
				for (TblAdditionalData tblAdditionalData : lstAdditionalData) {
					// Validar si existe un notification_id
					if (tblAdditionalData.getKeyAdditional().equals("notification-id")) {
						Optional<TblEquivalenceNotification> tblEquivalenceNotification = equivalenceNotificationRepository
								.getEquivalence(tblAdditionalData.getValueAdditional());
						if (tblEquivalenceNotification.isPresent()) {
							TblEquivalenceNotification equivalence = tblEquivalenceNotification.get();

							// Validar el estado del notification_id
							if (equivalence.getAction().equals("ok") && indicadorReset) {
								status = TicketStatus.RESET_SOLVED.toString();
							} else if (equivalence.getAction().equals("ok") && !indicadorReset) {
								status = TicketStatus.SOLVED.toString();
							} else if (equivalence.getAction().equals("averia")) {
								status = TicketStatus.FAULT.toString();
							} else if (equivalence.getAction().equals("whastapp")) {
								status = TicketStatus.WHATSAPP.toString();
							} else if (equivalence.getAction().equals("generico")) {
								status = TicketStatus.GENERIC.toString();
							}
						}
					}
				}
			}
		}
		return status;
	}
}