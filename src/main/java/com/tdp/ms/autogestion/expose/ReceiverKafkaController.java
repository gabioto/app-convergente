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
import com.tdp.ms.autogestion.repository.datasource.db.JpaAttachmentAdditionalDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaAttachmentRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceNotificationRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaStatusHistoryRepository;
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
public class ReceiverKafkaController {

	private static final Logger log = LoggerFactory.getLogger(ReceiverKafkaController.class);

	@Autowired
	JpaTicketRepository ticketRepository;

	@Autowired
	JpaStatusHistoryRepository statusHistoryRepository;

	@Autowired
	JpaAdditionalDataRepository additionalDataRepository;

	@Autowired
	JpaAttachmentRepository attachmentRepository;

	@Autowired
	JpaAttachmentAdditionalDataRepository attachmentAdditionalDataRepository;

	@Autowired
	JpaEquivalenceRepository equivalenceRepository;

	@Autowired
	JpaEquivalenceNotificationRepository equivalenceNotificationRepository;

	@Autowired
	FunctionsUtil functionsUtil;

	@Autowired
	EntityManager entityManager;

	@Transactional
	@KafkaListener(topics = "${app.topic.foo}")
	public void listen(@Payload String message) {
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

				List<Attachment> listAttachment = ticketKafkaResponse.getEvent().getTroubleTicket().getAttachment();
				List<AdditionalData> additionalDataLista = null;
				TblAttachment tblAttachment = null;
				TblAttachmentAdditionalData tblAttachmentAdditionalData = null;

				if (listAttachment != null && !listAttachment.isEmpty()) {
					for (Attachment attachment : listAttachment) {
						tblAttachment = new TblAttachment();
						tblAttachment.setIdAttachmentKafka(Integer.parseInt(attachment.getAttachmentId()));
						tblAttachment.setNameAttachment(attachment.getName());
						tblAttachment.setCreationDate(DateUtil.formatStringToLocalDateTime(attachment.getCreationDate()));
						tblAttachment.setTblTicket(tblTicket);
						tblAttachment = attachmentRepository.saveAndFlush(tblAttachment);
						additionalDataLista = attachment.getAdditionalData();

						for (int i = 0; i < additionalDataLista.size(); i++) {
							if (i > 0 && i % 5 == 0) {
								entityManager.flush();
								entityManager.clear();
							}
							tblAttachmentAdditionalData = new TblAttachmentAdditionalData();
							tblAttachmentAdditionalData.setKeyAttachmentAdditional(additionalDataLista.get(i).getKey());
							tblAttachmentAdditionalData.setValueAttachmentAdditional(additionalDataLista.get(i).getValue());
							tblAttachmentAdditionalData.setTblAttachment(tblAttachment);
							entityManager.persist(tblAttachmentAdditionalData);
						}
					}
				}
				List<AdditionalData> AdditionalDataList = ticketKafkaResponse.getEvent().getTroubleTicket()
						.getAdditionalData();
				TblAdditionalData tblAdditionalData = null;

				if (AdditionalDataList != null && !AdditionalDataList.isEmpty()) {
					for (AdditionalData ticketAdditional : AdditionalDataList) {
						tblAdditionalData = new TblAdditionalData();
						tblAdditionalData.setKeyAdditional(ticketAdditional.getKey());
						tblAdditionalData.setValueAdditional(ticketAdditional.getValue());
						tblAdditionalData.setTblTicket(tblTicket);
						additionalDataRepository.saveAndFlush(tblAdditionalData);
					}
				}
				
				// Logica:: Update status_ticket
				String status = TicketStatus.IN_PROGRESS.name();
				Boolean indicadorReset = Boolean.FALSE;

				Optional<List<TblEquivalence>> tableEquivalence = equivalenceRepository
						.getEquivalence(tblTicket.getIdTicket());

				if (tableEquivalence.isPresent()) {
					List<TblEquivalence> lstEquivalence = tableEquivalence.get();

					if (tblTicket.getInvolvement().equals(Constants.INTERNET) && !tblTicket.getTechnology().equals(Constants.TECHNOLOGY_GPON)) {					
						for (TblEquivalence tblEquivalence : lstEquivalence) {
							for (Attachment attachment : listAttachment) {
								// Validar si el attachment existe en la tabla de equivalencias
								if (attachment.getName().equals(tblEquivalence.getAttachmentName())) {
									List<AdditionalData> lstAttachmentAdditionalData = attachment.getAdditionalData();
									for (AdditionalData attachmentAdditionalData : lstAttachmentAdditionalData) {
										// Validamos si se realizo un reset
										if (attachmentAdditionalData.getKey().equals("estado-reset-modem-ok")) {
											status = TicketStatus.RESET.name();
											indicadorReset = Boolean.TRUE;
										}
									}
								}
							}
						}
					} else if (tblTicket.getInvolvement().equals(Constants.INTERNET) && tblTicket.getTechnology().equals(Constants.TECHNOLOGY_GPON)) {
						for (TblEquivalence tblEquivalence : lstEquivalence) {
							for (Attachment attachment : listAttachment) {
								// Validar si el attachment existe en la tabla de equivalencias
								if (attachment.getName().equals(tblEquivalence.getAttachmentName())) {
									List<AdditionalData> lstAttachmentAdditionalData = attachment.getAdditionalData();
									for (AdditionalData attachmentAdditionalData : lstAttachmentAdditionalData) {
										// Validamos si se realizo un reset
										if (attachmentAdditionalData.getKey().equals("sincronismo-modem-ont-ok")) {
											status = TicketStatus.RESET.name();
											indicadorReset = Boolean.TRUE;
										}
									}
								}
							}
						}
					}					
				}

				for (AdditionalData additionalData : AdditionalDataList) {
					if (additionalData.getKey().equals("notification-id")) {
						String usecase = "";
						if (tblTicket.getIdUseCase().equals(Constants.USE_CASE_INTERNET)) {
							usecase = Constants.TICKET_INTERNET_HFC;
						} else if (tblTicket.getIdUseCase().equals(Constants.USE_CASE_INTERNET_GPON)) {
							usecase = Constants.TICKET_INTERNET_GPON;
						} else if (tblTicket.getIdUseCase().equals(Constants.USE_CASE_CABLE)) {
							usecase = Constants.TICKET_TV;
						}
						Optional<TblEquivalenceNotification> tblEquivalenceNotification = equivalenceNotificationRepository
								.getEquivalence(tblAdditionalData.getValueAdditional(), usecase);
						if (tblEquivalenceNotification.isPresent()) {
							TblEquivalenceNotification equivalence = tblEquivalenceNotification.get();
							
							// Validar el estado del notification_id
							if (tblTicket.getInvolvement().equals(Constants.CABLE)) {
								if (equivalence.getCode().equals(Constants.CODE_REFRESH_OK)) {
									status = TicketStatus.REFRESH.toString();
								} else if (equivalence.getAction().equals(TicketStatus.FAULT.name())) {
									status = TicketStatus.FAULT.toString();
								} else if (equivalence.getAction().equals(TicketStatus.WHATSAPP.name())) {
									status = TicketStatus.WHATSAPP.toString();
								} else if (equivalence.getAction().equals(TicketStatus.GENERIC.name())) {
									status = TicketStatus.GENERIC.toString();
								}
							} else if (tblTicket.getInvolvement().equals(Constants.INTERNET)) {							
								if (equivalence.getAction().equals(TicketStatus.RESET_SOLVED.name()) && Boolean.TRUE.equals(indicadorReset)) {
									status = TicketStatus.RESET_SOLVED.toString();
								} else if (equivalence.getAction().equals(TicketStatus.RESET_SOLVED.name())
										&& Boolean.FALSE.equals(indicadorReset)) {
									status = TicketStatus.SOLVED.toString();
								} else if (equivalence.getAction().equals(TicketStatus.FAULT.name())) {
									status = TicketStatus.FAULT.toString();
								} else if (equivalence.getAction().equals(TicketStatus.WHATSAPP.name())) {
									status = TicketStatus.WHATSAPP.toString();
								} else if (equivalence.getAction().equals(TicketStatus.GENERIC.name())) {
									status = TicketStatus.GENERIC.toString();
								}
							}
						}
					}
				}

				LocalDateTime sysDate = LocalDateTime.now(ZoneOffset.of(Constants.ZONE_OFFSET));
				tblTicket.setStatusTicket(status);
				tblTicket.setModifiedDateTicket(sysDate);
				ticketRepository.save(tblTicket);
				LogData logdata = new LogData();
				logdata.setActionLog("Kafka listener");
				logdata.setChannel("Insert Ticket Fcr");
				logdata.setIdTicketTriaje(tblTicket.getIdTicketTriage());
				logdata.setDocumentType(tblTicket.getTblCustomer().getId().getDocumentType());
				logdata.setDocumentNumber(tblTicket.getTblCustomer().getId().getDocumentNumber());
				logdata.setRequest(message);
				logdata.setTypeLog("insertTicketFcr");
				functionsUtil.saveLogData(logdata);
			}
		} catch (Exception e) {
			log.error(this.getClass().getName().concat(" - Exception: ").concat(e.getLocalizedMessage()));
			
			LogData logdata = new LogData();
			logdata.setActionLog("Kafka listener");
			logdata.setIdTicketTriaje(Integer.parseInt(ticketKafkaResponse.getEvent().getTroubleTicket().getId()));			
			logdata.setRequest(message);
			logdata.setRequest(e.getLocalizedMessage());
			logdata.setChannel("Insert Ticket Fcr");
			logdata.setTypeLog("insertTicketFcr");
			functionsUtil.saveLogData(logdata);
		}
	}

}
