package com.tdp.ms.autogestion.expose;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.tdp.ms.autogestion.expose.entities.TicketKafkaResponse;
import com.tdp.ms.autogestion.expose.entities.TicketKafkaResponse.Event.TroubleTicket.AdditionalData;
import com.tdp.ms.autogestion.expose.entities.TicketKafkaResponse.Event.TroubleTicket.Attachment;
import com.tdp.ms.autogestion.model.LogData;
import com.tdp.ms.autogestion.model.TicketStatus;
import com.tdp.ms.autogestion.repository.datasource.db.JpaAdditionalDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaAttachmentRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceNotificationRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaTicketRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAdditionalData;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachment;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachmentAdditionalData;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomer;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomerPK;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalence;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalenceNotification;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;
import com.tdp.ms.autogestion.util.Constants;
import com.tdp.ms.autogestion.util.DateUtil;
import com.tdp.ms.autogestion.util.FunctionsUtil;

@EnableAsync
@Component
public class KafkaController {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaController.class);

	@Autowired
	private JpaTicketRepository ticketRepository;

	@Autowired
	private JpaAdditionalDataRepository additionalDataRepository;

	@Autowired
	private JpaAttachmentRepository attachmentRepository;

	@Autowired
	private JpaEquivalenceRepository equivalenceRepository;

	@Autowired
	private JpaEquivalenceNotificationRepository equivalenceNotificationRepository;

	@Autowired
	private FunctionsUtil functionsUtil;

	@Autowired
	private EntityManager entityManager;

	private String status = TicketStatus.IN_PROGRESS.name();
	private Boolean indicatorReset = false;

	//@Transactional
	//@KafkaListener(topics = "${app.topic.foo}")
	public void listen(@Payload String message) {
		LOG.info("Receiver.listen()  ==>  queue-notification-tickets");
		LOG.info(message);

		TicketKafkaResponse ticketKafkaResponse = new Gson().fromJson(message, TicketKafkaResponse.class);

		try {
			Optional<List<TblTicket>> listTblTicket = ticketRepository
					.findByIdTicketTriage(Integer.parseInt(ticketKafkaResponse.getEvent().getTroubleTicket().getId()));

			if (listTblTicket.isPresent()) {
				TblTicket tblTicket = new TblTicket();
				tblTicket.setEventTimeKafka(
						DateUtil.formatStringToLocalDateTimeKafka(ticketKafkaResponse.getEventTime()));
				tblTicket
						.setIdTicketTriage(Integer.parseInt(ticketKafkaResponse.getEvent().getTroubleTicket().getId()));
				tblTicket.setStatusChangeDate(DateUtil.formatStringToLocalDateTime(
						ticketKafkaResponse.getEvent().getTroubleTicket().getStatusChangeDate()));
				tblTicket.setStatusChangeReason(
						ticketKafkaResponse.getEvent().getTroubleTicket().getStatusChangeReason());
				tblTicket.setStatus(ticketKafkaResponse.getEvent().getTroubleTicket().getStatus());
				tblTicket.setDescription(ticketKafkaResponse.getEvent().getTroubleTicket().getDescription());
				tblTicket.setCreationDate(DateUtil.formatStringToLocalDateTime(
						ticketKafkaResponse.getEvent().getTroubleTicket().getCreationDate()));

				tblTicket.setCreationDateTicket(LocalDateTime.now(ZoneOffset.of(Constants.ZONE_OFFSET)));
				tblTicket.setPriority(ticketKafkaResponse.getEvent().getTroubleTicket().getPriority());
				tblTicket.setSeverity(ticketKafkaResponse.getEvent().getTroubleTicket().getSeverity());
				tblTicket.setStatusTicket(TicketStatus.CREATED.toString());
				tblTicket.setTicketType(ticketKafkaResponse.getEvent().getTroubleTicket().getType());

				AdditionalData ticketAdditionalData = ticketKafkaResponse.getEvent().getTroubleTicket()
						.getAdditionalData().stream().filter(x -> x.getKey().equals("use-case-id")).findAny()
						.orElse(null);
				tblTicket.setIdUseCase(ticketAdditionalData.getValue());
				TblCustomer tblCustomer = new TblCustomer();
				TblCustomerPK tblCustomerPK = new TblCustomerPK();
				tblCustomerPK
						.setDocumentNumber(listTblTicket.get().get(0).getTblCustomer().getId().getDocumentNumber());
				tblCustomerPK.setDocumentType(listTblTicket.get().get(0).getTblCustomer().getId().getDocumentType());
				tblCustomerPK.setServiceCode(listTblTicket.get().get(0).getTblCustomer().getId().getServiceCode());
				tblTicket.setInvolvement(listTblTicket.get().get(0).getInvolvement());
				tblTicket.setTechnology(listTblTicket.get().get(0).getTechnology());
				tblTicket.setProductIdentifier(listTblTicket.get().get(0).getProductIdentifier());
				tblCustomer.setId(tblCustomerPK);
				tblTicket.setTblCustomer(tblCustomer);

				tblTicket = ticketRepository.saveAndFlush(tblTicket);
				LOG.info("tblTicket idTicket:: " + tblTicket.getIdTicket());
				LOG.info("tblTicket idTicketTriaje:: " + tblTicket.getIdTicketTriage());

				List<Attachment> listAttachment = ticketKafkaResponse.getEvent().getTroubleTicket().getAttachment();

				// Validación de attachments
				fillAttachment(tblTicket, listAttachment);

				// Validación additional data
				List<AdditionalData> additionalDataList = ticketKafkaResponse.getEvent().getTroubleTicket()
						.getAdditionalData();
				TblAdditionalData tblAdditionalData = fillAdditionalData(tblTicket, additionalDataList);

				setProgressTicketStatus(tblTicket, listAttachment);

				setSolvedTicketStatus(tblAdditionalData, additionalDataList);

				LOG.info("status ticket:: " + status);
				LocalDateTime sysDate = LocalDateTime.now(ZoneOffset.of(Constants.ZONE_OFFSET));
				tblTicket.setStatusTicket(status);
				tblTicket.setModifiedDateTicket(sysDate);

				ticketRepository.save(tblTicket);

				functionsUtil.saveLogData(new LogData(tblTicket.getIdTicketTriage(),
						tblTicket.getTblCustomer().getId().getDocumentNumber(),
						tblTicket.getTblCustomer().getId().getDocumentType(), "Kafka listener", "event", message, "Ok",
						"Insert Ticket Fcr"));
			}

		} catch (Exception e) {
			LOG.info("Error::: " + e.getMessage());
			functionsUtil.saveLogData(
					new LogData(Integer.parseInt(ticketKafkaResponse.getEvent().getTroubleTicket().getId()), "", "",
							"Kafka listener", "event", message, e.getMessage(), "Insert Ticket Fcr"));
		}

	}

	private void fillAttachment(TblTicket tblTicket, List<Attachment> listAttachment) {
		if (listAttachment != null) {
			for (Attachment attachment : listAttachment) {
				TblAttachment tblAttachment = new TblAttachment();
				tblAttachment.setIdAttachmentKafka(Integer.parseInt(attachment.getAttachmentId()));
				tblAttachment.setNameAttachment(attachment.getName());
				tblAttachment.setCreationDate(DateUtil.formatStringToLocalDateTime(attachment.getCreationDate()));
				tblAttachment.setTblTicket(tblTicket);
				tblAttachment = attachmentRepository.saveAndFlush(tblAttachment);
				List<AdditionalData> additionalDataLista = attachment.getAdditionalData();

				for (int i = 0; i < additionalDataLista.size(); i++) {
					if (i > 0 && i % 5 == 0) {
						entityManager.flush();
						entityManager.clear();
					}
					TblAttachmentAdditionalData tblAttachmentAdditionalData = new TblAttachmentAdditionalData();
					tblAttachmentAdditionalData.setKeyAttachmentAdditional(additionalDataLista.get(i).getKey());
					tblAttachmentAdditionalData.setValueAttachmentAdditional(additionalDataLista.get(i).getValue());
					tblAttachmentAdditionalData.setTblAttachment(tblAttachment);
					entityManager.persist(tblAttachmentAdditionalData);
				}
			}
		}
	}

	private TblAdditionalData fillAdditionalData(TblTicket tblTicket, List<AdditionalData> additionalDataList) {
		TblAdditionalData tblAdditionalData = null;

		if (additionalDataList != null) {
			for (AdditionalData ticketAdditional : additionalDataList) {
				tblAdditionalData = new TblAdditionalData();
				tblAdditionalData.setKeyAdditional(ticketAdditional.getKey());
				tblAdditionalData.setValueAdditional(ticketAdditional.getValue());
				tblAdditionalData.setTblTicket(tblTicket);
				additionalDataRepository.saveAndFlush(tblAdditionalData);
			}
		}

		return tblAdditionalData;
	}

	private void setProgressTicketStatus(TblTicket tblTicket, List<Attachment> listAttachment) {
		Optional<List<TblEquivalence>> tableEquivalence = equivalenceRepository.getEquivalence(tblTicket.getIdTicket());

		if (tableEquivalence.isPresent()) {
			List<TblEquivalence> lstEquivalence = tableEquivalence.get();

			for (TblEquivalence tblEquivalence : lstEquivalence) {
				for (Attachment attachment : listAttachment) {
					validateReset(attachment, tblEquivalence);
				}
			}
		}
	}

	private void validateReset(Attachment attachment, TblEquivalence tblEquivalence) {
		// Validar si el attachment existe en la tabla de equivalencias
		if (attachment.getName().equals(tblEquivalence.getAttachmentName())) {
			List<AdditionalData> lstAttachmentAdditionalData = attachment.getAdditionalData();
			for (AdditionalData attachmentAdditionalData : lstAttachmentAdditionalData) {
				// Validamos si se realizo un reset
				if (attachmentAdditionalData.getKey().equals("estado-reset-modem-ok")) {
					status = TicketStatus.RESET.name();
					indicatorReset = true;
				}
			}
		}
	}

	private void setSolvedTicketStatus(TblAdditionalData tblAdditionalData, List<AdditionalData> additionalDataList) {

		for (AdditionalData additionalData : additionalDataList) {
			if (additionalData.getKey().equals("notification-id")) {
				Optional<TblEquivalenceNotification> tblEquivalenceNotification = equivalenceNotificationRepository
						.getEquivalence(tblAdditionalData.getValueAdditional());

				if (tblEquivalenceNotification.isPresent()) {
					TblEquivalenceNotification tblEquivalence = tblEquivalenceNotification.get();
					status = setStatus(tblEquivalence);
				}
			}
		}
	}

	private String setStatus(TblEquivalenceNotification equivalence) {
		// Validar el estado del notification_id
		if (equivalence.getAction().equals(TicketStatus.RESET_SOLVED.name()) && indicatorReset) {
			return TicketStatus.RESET_SOLVED.toString();
		} else if (equivalence.getAction().equals(TicketStatus.RESET_SOLVED.name()) && !indicatorReset) {
			return TicketStatus.SOLVED.toString();
		} else if (equivalence.getAction().equals(TicketStatus.FAULT.name())) {
			return TicketStatus.FAULT.toString();
		} else if (equivalence.getAction().equals(TicketStatus.WHATSAPP.name())) {
			return TicketStatus.WHATSAPP.toString();
		} else if (equivalence.getAction().equals(TicketStatus.GENERIC.name())) {
			return TicketStatus.GENERIC.toString();
		} else if (equivalence.getAction().equals(TicketStatus.FAULT_TRAZA.name())) {
			return TicketStatus.FAULT_TRAZA.toString();
		}

		return "";
	}

}
