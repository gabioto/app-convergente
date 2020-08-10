package com.tdp.ms.autogestion.repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tdp.ms.autogestion.exception.ErrorCategory;
import com.tdp.ms.autogestion.exception.ResourceNotFoundException;
import com.tdp.ms.autogestion.model.AdditionalData;
import com.tdp.ms.autogestion.model.Attachment;
import com.tdp.ms.autogestion.model.Equivalence;
import com.tdp.ms.autogestion.model.EquivalenceNotification;
import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.model.TicketStatus;
import com.tdp.ms.autogestion.repository.datasource.api.TicketApi;
import com.tdp.ms.autogestion.repository.datasource.db.JpaAttachmentAdditionalDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaCustomerRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceNotificationRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaTicketRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachmentAdditionalData;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomer;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomerPK;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalence;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalenceNotification;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;
import com.tdp.ms.autogestion.util.Constants;

@Repository
public class TicketRepositoryImpl implements TicketRepository {

	private static final Log log = LogFactory.getLog(TicketRepositoryImpl.class);
	private static final String TAG = TicketRepositoryImpl.class.getCanonicalName();

	@Autowired
	private TicketApi ticketApi;

	@Autowired
	private JpaCustomerRepository jpaCustomerRepository;

	@Autowired
	private JpaTicketRepository jpaTicketRepository;

	@Autowired
	private JpaEquivalenceRepository jpaEquivalenceRepository;

	@Autowired
	private JpaEquivalenceNotificationRepository jpaEquivalenceNotificationRepository;

	@Autowired
	private JpaAttachmentAdditionalDataRepository attachmentAdditionalDataRepository;

	@Override
	public Ticket generateTicket(OAuth pOAuth, Ticket pTicket) {
		return ticketApi.generate(pOAuth, pTicket);
	}

	@Override
	public void saveGeneratedTicket(Ticket pTicket) {
		try {
			Optional<TblCustomer> optCustomer;
			TblCustomer tableCustomer = new TblCustomer();
			TblCustomerPK tableCustomerPk;
			TblTicket tableTicket;

			tableCustomerPk = TblCustomerPK.from(pTicket);
			tableCustomer.setId(tableCustomerPk);
			tableTicket = TblTicket.from(pTicket, tableCustomer, TicketStatus.CREATED.name());

			optCustomer = jpaCustomerRepository.findById(tableCustomerPk);

			if (!optCustomer.isPresent()) {
				jpaCustomerRepository.save(tableCustomer);
			}

			tableTicket = jpaTicketRepository.save(tableTicket);
			log.info(TAG + "createTicket: " + tableTicket.getIdTicket());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Ticket updateTicketStatus(int idTicket, String status) {
		LocalDateTime sysDate = LocalDateTime.now(ZoneOffset.of(Constants.ZONE_OFFSET));
		Optional<List<TblTicket>> list = jpaTicketRepository.getTicketStatus(idTicket);

		if (list.isPresent()) {
			TblTicket tblTicket = list.get().get(list.get().size() == 1 ? 0 : 1);

			tblTicket.setStatusTicket(status);
			tblTicket.setModifiedDateTicket(sysDate);
			tblTicket.setEventTimeKafka(sysDate);
			tblTicket = jpaTicketRepository.save(tblTicket);

			return tblTicket.fromThis();
		} else {
			throw new ResourceNotFoundException(ErrorCategory.RESOURCE_NOT_FOUND.getExceptionText(),
					String.valueOf(idTicket));
		}
	}

	@Override
	public Ticket getTicket(int idTicket) {
		Optional<List<TblTicket>> list = jpaTicketRepository.getTicket(idTicket);
		if (list.isPresent()) {
			return list.get().get(0).fromThis();
		} else {
			throw new ResourceNotFoundException(ErrorCategory.RESOURCE_NOT_FOUND.getExceptionText(),
					String.valueOf(idTicket));
		}
	}

	@Override
	public List<Ticket> findByCustomerAndUseCase(String docType, String docNumber, String reference, String involvement,
			LocalDateTime creationDate, LocalDateTime endDate) {
		List<TblTicket> tblTickets = jpaTicketRepository.findByCustomerAndUseCase(docType, docNumber, reference,
				involvement, creationDate, endDate);

		return TblTicket.listFromThis(tblTickets);
	}

	@Override
	public List<Ticket> findByCustomerAndUseCasePast(String docType, String docNumber, String reference,
			String involvement) {
		List<TblTicket> tblTickets = jpaTicketRepository.findByCustomerAndUseCasePast(docType, docNumber, reference,
				involvement);

		return TblTicket.listFromThis(tblTickets);
	}

	@Override
	public List<AdditionalData> getDebtAmount(Integer attachmentId) {
		Optional<List<TblAttachmentAdditionalData>> optAdditionalData = attachmentAdditionalDataRepository
				.getMontoDeuda(attachmentId, "monto");

		return optAdditionalData.isPresent() ? TblAttachmentAdditionalData.listFromThis(optAdditionalData.get())
				: new ArrayList<>();
	}

	@Override
	public List<Equivalence> getAttachmentEquivalence(Integer ticketId) {
		Optional<List<TblEquivalence>> optEquivalence = jpaEquivalenceRepository.getEquivalence(ticketId);

		return optEquivalence.isPresent() ? TblEquivalence.listFromThis(optEquivalence.get()) : new ArrayList<>();
	}

	@Override
	public EquivalenceNotification getNotificationEquivalence(String code) {
		Optional<TblEquivalenceNotification> optEquivalenceNot = jpaEquivalenceNotificationRepository
				.getEquivalence(code);

		return optEquivalenceNot.isPresent() ? optEquivalenceNot.get().fromThis() : null;
	}

	@Override
	public List<AdditionalData> getAdditionalData(Ticket ticket) {
		List<AdditionalData> lstClientData = new ArrayList<AdditionalData>();
		AdditionalData clientData = new AdditionalData();
		clientData.setKey(Constants.LABEL_STATUS);
		clientData.setValue(ticket.getStatus());
		lstClientData.add(clientData);

		// Validaciones de attachments
		lstClientData = fillAttachmentsTicket(ticket, lstClientData, clientData);

		// Validaciones de notificationId
		lstClientData = fillNotificationTicket(ticket, lstClientData, clientData);

		return lstClientData;
	}

	@Override
	public List<Ticket> getTicketStatus(Integer idTicket) {
		Optional<List<TblTicket>> optTickets = jpaTicketRepository.getTicketStatus(idTicket);

		if (optTickets.isPresent()) {
			return TblTicket.listFromThis(optTickets.get());
		} else {
			throw new ResourceNotFoundException(ErrorCategory.RESOURCE_NOT_FOUND.getExceptionText(),
					String.valueOf(idTicket));
		}
	}

	private List<AdditionalData> fillAttachmentsTicket(Ticket ticket, List<AdditionalData> lstClientData,
			AdditionalData clientData) {

		List<Attachment> attachments = ticket.getAttachments();

		if (attachments != null && attachments.size() > 0) {
			for (Attachment attachment : attachments) {
				// Obtener el monto adeudado por el cliente
				if (attachment.getNameAttachment().equals("ValidacionesInicialesInternet[{}]recupera-deuda-amdocs")
						|| attachment.getNameAttachment().equals("ValidacionesInicialesInternet[{}]recupera-deuda-cms")
						|| attachment.getNameAttachment()
								.equals("ValidacionesInicialesInternet[{}]recupera-deuda-atis")) {

					List<AdditionalData> attachAddDataList = getDebtAmount(attachment.getIdAttachment());

					for (AdditionalData attachAddData : attachAddDataList) {
						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_MONTO);
						clientData.setValue(attachAddData.getValue());
						lstClientData.add(clientData);
					}

				}

				if (attachment.getNameAttachment().equals("AveriaPendiente[{}]recupera-averia-pendiente-amdocs")
						|| attachment.getNameAttachment().equals("AveriaPendiente[{}]recupera-averia-pendiente-cms")
						|| attachment.getNameAttachment()
								.equals("AveriaPendiente[{}]recupera-averia-pendiente-gestel")) {

					List<AdditionalData> attachAddDataList = getDebtAmount(attachment.getIdAttachment());

					for (AdditionalData attachAddData : attachAddDataList) {
						if (attachAddData.getKey().equals("codigo_averia")) {
							clientData = new AdditionalData();
							clientData.setKey(Constants.LABEL_COD_AVERIA);
							clientData.setValue(attachAddData.getValue());
							lstClientData.add(clientData);
						}
					}
				}
			}

			// Equivalencias de Attachments
			List<Equivalence> equivalenceList = getAttachmentEquivalence(ticket.getId());
			int index = 1;

			for (Equivalence equivalence : equivalenceList) {
				clientData = new AdditionalData();
				clientData.setKey(Constants.LABEL_FRONT_END.concat(String.valueOf(index)));
				clientData.setValue(equivalence.getNameEquivalence());
				lstClientData.add(clientData);
				index++;
			}
		}

		return lstClientData;
	}

	private List<AdditionalData> fillNotificationTicket(Ticket ticket, List<AdditionalData> lstClientData,
			AdditionalData clientData) {

		List<AdditionalData> lstAdditionalData = ticket.getAdditionalData();
		if (lstAdditionalData != null && lstAdditionalData.size() > 0) {
			for (AdditionalData additionalData : lstAdditionalData) {
				if (additionalData.getKey().equals("notification-id")) {
					EquivalenceNotification equivalence = getNotificationEquivalence(additionalData.getValue());

					if (equivalence != null) {
						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_ACTION);
						clientData.setValue(equivalence.getAction() != null ? equivalence.getAction() : "");
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_TITLE);
						clientData.setValue(equivalence.getTitle() != null ? equivalence.getTitle() : "");
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_TITLE_DESC);
						clientData.setValue(equivalence.getDescription() != null ? equivalence.getDescription() : "");
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_BODY);
						clientData.setValue(equivalence.getBody() != null ? equivalence.getBody() : "");
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_FOOTER);
						clientData.setValue(equivalence.getFooter() != null ? equivalence.getFooter() : "");
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_ICON);
						clientData.setValue(equivalence.getIcon() != null ? equivalence.getIcon() : "");
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_BUTTON);
						clientData.setValue(equivalence.getButton() != null ? equivalence.getButton() : "");
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_IMAGE);
						clientData.setValue(equivalence.getImage() != null ? equivalence.getImage() : "");
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_ACTION_BUTTON);
						clientData.setValue(equivalence.getActionbutton() != null ? equivalence.getActionbutton() : "");
						lstClientData.add(clientData);
					}
				}
			}
		}

		return lstClientData;
	}
}