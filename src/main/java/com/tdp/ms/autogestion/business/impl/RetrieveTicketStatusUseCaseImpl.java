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
import com.tdp.ms.autogestion.repository.datasource.db.JpaAttachmentAdditionalDataRepository;
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
	JpaAttachmentAdditionalDataRepository attachmentAdditionalDataRepository;
	
	@Autowired
	JpaEquivalenceNotificationRepository equivalenceNotificationRepository;

	@Autowired
	FunctionsUtil functionsUtil;

	@Override
	public ResponseEntity<TicketStatusResponse> retrieveTicketStatus(String idTicket) {
		
		TicketStatusResponse ticketStatusResponse = null;
		try {			
			Optional<List<TblTicket>> tableTicket = ticketRepository.getTicketStatus(Integer.parseInt(idTicket));
			if (tableTicket.isPresent()) {
				List<AdditionalData> lstClienteData = new ArrayList<AdditionalData>();

				AdditionalData clienteData = new AdditionalData();
				clienteData.setKey("status");
				clienteData.setValue(tableTicket.get().get(0).getStatus());
				lstClienteData.add(clienteData);

				List<TblAttachment> lstAttachment = tableTicket.get().get(0).getTblAttachments();
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
									clienteData.setKey("monto");
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
										clienteData.setKey("codigo-averia");
										clienteData.setValue(tblAttachmentAdditionalData.getValueAttachmentAdditional());
										lstClienteData.add(clienteData);
									}
								}
							}
						}
					}
					
					// Equivalencias
					Optional<List<TblEquivalence>> tableEquivalence = equivalenceRepository
							.getEquivalence(tableTicket.get().get(0).getIdTicket());
					if (tableEquivalence.isPresent()) {
						int index = 1;
						List<TblEquivalence> lstEquivalence = tableEquivalence.get();
						for (TblEquivalence tblEquivalence : lstEquivalence) {
							clienteData = new AdditionalData();
							clienteData.setKey("Attachment-".concat(String.valueOf(index)));
							clienteData.setValue(tblEquivalence.getNameEquivalence());
							lstClienteData.add(clienteData);
							index++;
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
								
								clienteData = new AdditionalData();
								clienteData.setKey("button");
								clienteData.setValue(equivalence.getButton() != null ? equivalence.getButton() : "");
								lstClienteData.add(clienteData);
								
								clienteData = new AdditionalData();
								clienteData.setKey("image");
								clienteData.setValue(equivalence.getImage() != null ? equivalence.getImage() : "");
								lstClienteData.add(clienteData);
								
								clienteData = new AdditionalData();
								clienteData.setKey("actionbbutton");
								clienteData.setValue(equivalence.getActionbutton() != null ? equivalence.getActionbutton() : "");
								lstClienteData.add(clienteData);								
							}
						}
					}
				}

				TblTicket tblTicket = tableTicket.get().get(0);

				ticketStatusResponse = new TicketStatusResponse(tblTicket.getIdTicketTriage(), tblTicket.getDescription(),
						tblTicket.getCreationDate(), tblTicket.getTicketType(), tblTicket.getStatusChangeDate(),
						tblTicket.getStatusTicket(), tblTicket.getModifiedDateTicket(), lstClienteData);

				functionsUtil.saveLogData(tableTicket.get().get(0).getIdTicketTriage(),
						tableTicket.get().get(0).getTblCustomer().getId().getDocumentNumber(),
						tableTicket.get().get(0).getTblCustomer().getId().getDocumentType(), "Retrieve Ticket Status",
						"OK", null, ticketStatusResponse.toString(), "Retrieve Ticket Status");

				return new ResponseEntity<>(ticketStatusResponse, HttpStatus.OK);
			} else {
				functionsUtil.saveLogData(Integer.parseInt(idTicket), null, null, "Retrieve Ticket Status", "NOT_FOUND",
						null, "Ticket No Existe", "Retrieve Ticket Status");

				return new ResponseEntity<>(ticketStatusResponse, HttpStatus.NOT_FOUND);
			}			
		} catch (Exception exception) {
			functionsUtil.saveLogData(0, null, null, "Retrieve Ticket Status", "ERROR", null, "Ticket Nulo",
					"Retrieve Ticket Status");
			
			return new ResponseEntity<>(ticketStatusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}