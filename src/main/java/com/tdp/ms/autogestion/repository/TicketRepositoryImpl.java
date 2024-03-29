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
import com.tdp.ms.autogestion.util.StringUtil;

@Repository
public class TicketRepositoryImpl implements TicketRepository {

	private static final Log log = LogFactory.getLog(TicketRepositoryImpl.class);
	
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
		} catch (Exception e) {
			log.error(this.getClass().getName() + " - Exception: " + e.getMessage());
			
			throw e;
		}
	}

	@Override
	public Ticket updateTicketStatus(int idTicket, String status) {
		LocalDateTime sysDate = LocalDateTime.now(ZoneOffset.of(Constants.ZONE_OFFSET));
		Optional<List<TblTicket>> list = jpaTicketRepository.getTicketStatus(idTicket);

		if (list.isPresent()) { 
			TblTicket tblTicket = null;
			if (list.get().size() == 1) {
				tblTicket = list.get().get(0);
			} else if (list.get().size() <= 2) {
				if (list.get().get(0).getStatusTicket().equals(TicketStatus.WA_DEFAULT.name())) {
					tblTicket = list.get().get(0);
				} else if (list.get().get(1).getStatusTicket().equals(TicketStatus.WA_DEFAULT.name())) {
					tblTicket = list.get().get(1);
				} else {
					tblTicket = list.get().get(list.get().size() == 1 ? 0 : 1);
				}
			} else if (list.get().size() <= 3) {
				if (list.get().get(0).getStatusTicket().equals(TicketStatus.WA_DEFAULT.name())) {
					tblTicket = list.get().get(0);
				} else if (list.get().get(1).getStatusTicket().equals(TicketStatus.WA_DEFAULT.name())) {
					tblTicket = list.get().get(1);
				} else if (list.get().get(2).getStatusTicket().equals(TicketStatus.WA_DEFAULT.name())) {
					tblTicket = list.get().get(2);					
				} else {
					tblTicket = list.get().get(1);
				}
			} else if (list.get().size() <= 4) {
				if (list.get().get(0).getStatusTicket().equals(TicketStatus.WA_DEFAULT.name())) {
					tblTicket = list.get().get(0);
				} else if (list.get().get(1).getStatusTicket().equals(TicketStatus.WA_DEFAULT.name())) {
					tblTicket = list.get().get(1);
				} else if (list.get().get(2).getStatusTicket().equals(TicketStatus.WA_DEFAULT.name())) {
					tblTicket = list.get().get(2);					
				} else if (list.get().get(3).getStatusTicket().equals(TicketStatus.WA_DEFAULT.name())) {
					tblTicket = list.get().get(3);					
				} else {
					tblTicket = list.get().get(1);
				}
			} else {
				int total = list.get().size();				
				if (list.get().get(total-1).getStatusTicket().equals(TicketStatus.WA_DEFAULT.name())) {
					tblTicket = list.get().get(total-1);
				} else if (list.get().get(total-2).getStatusTicket().equals(TicketStatus.WA_DEFAULT.name())) {
					tblTicket = list.get().get(total-2);
				} else if (list.get().get(total-3).getStatusTicket().equals(TicketStatus.WA_DEFAULT.name())) {
					tblTicket = list.get().get(total-3);
				} else {
					tblTicket = list.get().get(1);
				}
			}
			tblTicket.setStatusTicket(status);
			tblTicket.setModifiedDateTicket(sysDate);			
			tblTicket = jpaTicketRepository.save(tblTicket);

			return tblTicket.fromThis();
		} else {
			throw new ResourceNotFoundException(ErrorCategory.RESOURCE_NOT_FOUND.getExceptionText(),
					String.valueOf(idTicket));
		}
	}

	@Override
	public Ticket updateTicketStatusTimeout(int idTicket, String status) {
		LocalDateTime sysDate = LocalDateTime.now(ZoneOffset.of(Constants.ZONE_OFFSET));
		Optional<List<TblTicket>> list = jpaTicketRepository.getTicket(idTicket);

		if (list.isPresent()) {
			TblTicket tblTicket = list.get().get(0);
			tblTicket.setStatusTicket(status);
			tblTicket.setModifiedDateTicket(sysDate);			
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
	public List<AdditionalData> getValue(Integer attachmentId, String field) {
		Optional<List<TblAttachmentAdditionalData>> optAdditionalData = attachmentAdditionalDataRepository
				.getValue(attachmentId, field);

		return optAdditionalData.isPresent() ? TblAttachmentAdditionalData.listFromThis(optAdditionalData.get())
				: new ArrayList<>();
	}

	@Override
	public List<Equivalence> getAttachmentEquivalence(Integer ticketId) {
		Optional<List<TblEquivalence>> optEquivalence = jpaEquivalenceRepository.getEquivalence(ticketId);

		return optEquivalence.isPresent() ? TblEquivalence.listFromThis(optEquivalence.get()) : new ArrayList<>();
	}

	@Override
	public EquivalenceNotification getNotificationEquivalence(String code, String usecase) {
		Optional<TblEquivalenceNotification> optEquivalenceNot = jpaEquivalenceNotificationRepository
				.getEquivalence(code, usecase);

		return optEquivalenceNot.isPresent() ? optEquivalenceNot.get().fromThis() : null;
	}

	@Override
	public List<AdditionalData> getAdditionalData(Ticket ticket, int minutes) {
		List<AdditionalData> lstClientData = new ArrayList<AdditionalData>();
		AdditionalData clientData = new AdditionalData();
		clientData.setKey(Constants.LABEL_STATUS);
		clientData.setValue(ticket.getStatus());
		lstClientData.add(clientData);

		if (minutes > 0) {
			clientData = new AdditionalData();
			clientData.setKey(Constants.LABEL_TIME);
			clientData.setValue(String.valueOf(minutes));
			lstClientData.add(clientData);
		}
		
		// Validaciones de attachments
		lstClientData = fillAttachmentsTicket(ticket, lstClientData);

		// Validaciones de notificationId
		lstClientData = fillNotificationTicket(ticket, lstClientData);

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

	private List<AdditionalData> fillAttachmentsTicket(Ticket ticket, List<AdditionalData> lstClientData) {

		List<Attachment> attachments = ticket.getAttachments();

		if (attachments != null && !attachments.isEmpty()) {
			String result = "";

			for (Attachment attachment : attachments) {
				// Estado Comercial Linea Fija e Internet
				result = getCommercialStatus(attachment, result);

				// Sin Deuda Pendiente
				result = getNoDebt(attachment, result, lstClientData);

				// Sin Orden de Reconexión
				result = getNoOrder(attachment, result);

				// Ninguna Avería Pendiente
				if (ticket.getInvolvement().equals(Constants.INTERNET)) {
					result = getNoFaultsInternet(attachment, result, lstClientData);
					
					// Registro Avería
					result = getNoFaultsReg(attachment, result, lstClientData);
				} else if (ticket.getInvolvement().equals(Constants.CABLE)) {
					result = getNoFaultsCable(attachment, result, lstClientData);
				}
				
				// Masiva DMPE
				result = getMassiveDMPE(attachment, result);
				
				// Problemas técnicos
				result = getTechTroubles(attachment, result);
			}

			// Equivalencias de Attachments
			attachEquivalence(result, lstClientData, ticket);
		}

		return lstClientData;
	}

	private String getMassiveDMPE(Attachment attachment, String result) {
		if (attachment.getNameAttachment().equals("ValidaMasiva[{}]recupera-masiva-dmpe")) {
			List<AdditionalData> attachAddDataList = getValue(attachment.getIdAttachment(), "tiene-masiva-dmpe");
			for (AdditionalData attachAddData : attachAddDataList) {
				Boolean check = Boolean.FALSE;
				if (attachAddData.getValue().equals("SI")) {
					check = Boolean.TRUE;
				}
				result += attachment.getNameAttachment().concat(";").concat(check.toString()).concat(",");
			}
		}

		return result;
	}
	
	private String getCommercialStatus(Attachment attachment, String result) {
		if (attachment.getNameAttachment().equals("ValidacionesInicialesInternet[{}]recupera-info-telefono")) {
			List<AdditionalData> attachAddDataList = getValue(attachment.getIdAttachment(), "estado-linea");
			for (AdditionalData attachAddData : attachAddDataList) {
				Boolean check = Boolean.FALSE;
				if (attachAddData.getValue().equals("active")) {
					check = Boolean.TRUE;
				}
				result += attachment.getNameAttachment().concat(";").concat(check.toString()).concat(",");
			}
		}

		return result;
	}

	private String getNoDebt(Attachment attachment, String result, List<AdditionalData> lstClientData) {
		if (attachment.getNameAttachment().equals("ValidacionesInicialesInternet[{}]recupera-deuda-amdocs")
				|| attachment.getNameAttachment().equals("ValidacionesInicialesInternet[{}]recupera-deuda-cms")
				|| attachment.getNameAttachment().equals("ValidacionesInicialesInternet[{}]recupera-deuda-atis")) {

			List<AdditionalData> attachAddDataList = getValue(attachment.getIdAttachment(), "monto");
			for (AdditionalData attachAddData : attachAddDataList) {
				if (!attachAddData.getValue().equals("") && !attachAddData.getValue().equals("0")) {
					AdditionalData clientData = new AdditionalData();
					clientData.setKey(Constants.LABEL_MONTO);
					clientData.setValue(attachAddData.getValue());
					lstClientData.add(clientData);

					result += attachment.getNameAttachment().concat(";").concat(Boolean.FALSE.toString()).concat(",");
				} else {
					result += attachment.getNameAttachment().concat(";").concat(Boolean.TRUE.toString()).concat(",");
				}
			}
		}

		return result;
	}

	private String getNoOrder(Attachment attachment, String result) {
		if (attachment.getNameAttachment()
				.equals("ValidacionesInicialesInternet[{}]recupera-reconexion-pendiente-amdocs")
				|| attachment.getNameAttachment()
						.equals("ValidacionesInicialesInternet[{}]recupera-reconexion-pendiente-cms")
				|| attachment.getNameAttachment()
						.equals("ValidacionesInicialesInternet[{}]recupera-reconexion-pendiente-atis-hfc")
				|| attachment.getNameAttachment()
						.equals("ValidacionesInicialesInternet[{}]recupera-reconexion-pendiente-atis-adsl")
				|| attachment.getNameAttachment()
						.equals("PeticionesPendiente[{}]recupera-peticion-pendiente")) {

			List<AdditionalData> attachAddDataList = getValue(attachment.getIdAttachment(),
					"tiene-reconexion-pendiente");
			for (AdditionalData attachAddData : attachAddDataList) {
				Boolean check = Boolean.TRUE;
				if (attachAddData.getValue().equals("SI")) {
					check = Boolean.FALSE;
				}
				result += attachment.getNameAttachment().concat(";").concat(check.toString()).concat(",");
			}
		}

		return result;
	}

	private String getNoFaultsInternet(Attachment attachment, String result, List<AdditionalData> lstClientData) {
		if (attachment.getNameAttachment().equals("AveriaPendiente[{}]recupera-averia-pendiente-amdocs")
				|| attachment.getNameAttachment().equals("AveriaPendiente[{}]recupera-averia-pendiente-cms")
				|| attachment.getNameAttachment().equals("AveriaPendiente[{}]recupera-averia-pendiente-gestel")) {

			Boolean indicador = Boolean.FALSE;
			List<AdditionalData> attachAddDataList = getValue(attachment.getIdAttachment(), "codigo-averia");
			for (AdditionalData attachAddData : attachAddDataList) {
				if (!attachAddData.getValue().equals("")) {
					AdditionalData clientData = new AdditionalData();
					clientData.setKey(Constants.LABEL_COD_AVERIA_PEND);
					clientData.setValue(attachAddData.getValue());
					lstClientData.add(clientData);
					result += attachment.getNameAttachment().concat(";").concat(Boolean.FALSE.toString()).concat(",");
				} else {
					result += attachment.getNameAttachment().concat(";").concat(Boolean.TRUE.toString()).concat(",");
				}
				indicador = Boolean.TRUE;
			}
			if (!indicador) {
				result += attachment.getNameAttachment().concat(";").concat(Boolean.TRUE.toString()).concat(",");
			}
		}

		return result;
	}

	private String getNoFaultsReg(Attachment attachment, String result, List<AdditionalData> lstClientData) {
		if (attachment.getNameAttachment().equals("ValidaMasiva[{}]registra-averia-cms")
				|| attachment.getNameAttachment().equals("AveriaPendiente[{}]registra-averia-cms")
				|| attachment.getNameAttachment().equals("SolucionNoNavega[{}]registra-averia-cms")
				|| attachment.getNameAttachment().equals("RecomendacionesDespachoHFC[{}]registra-averia-cms")) {

			Boolean indicador = Boolean.FALSE;
			List<AdditionalData> attachAddDataList = getValue(attachment.getIdAttachment(), "numero-requerimiento-averia-cms");
			for (AdditionalData attachAddData : attachAddDataList) {
				if (!attachAddData.getValue().equals("")) {
					AdditionalData clientData = new AdditionalData();
					clientData.setKey(Constants.LABEL_COD_AVERIA);
					clientData.setValue(attachAddData.getValue());
					lstClientData.add(clientData);
					result += attachment.getNameAttachment().concat(";").concat(Boolean.FALSE.toString()).concat(",");
				} else {
					result += attachment.getNameAttachment().concat(";").concat(Boolean.TRUE.toString()).concat(",");
				}
				indicador = Boolean.TRUE;
			}
			if (!indicador) {
				result += attachment.getNameAttachment().concat(";").concat(Boolean.TRUE.toString()).concat(",");
			}
		} else if (attachment.getNameAttachment().equals("AveriaPendiente[{}]registrar-averia-gestel")
				|| attachment.getNameAttachment().equals("RecomendacionesDespachoADSL[{}]registrar-averia-gestel")
				|| attachment.getNameAttachment().equals("SolucionNoNavega[{}]registrar-averia-gestel")) {
			Boolean indicador = Boolean.FALSE;
			List<AdditionalData> attachAddDataList = getValue(attachment.getIdAttachment(), "codigo-averia-gestel");
			for (AdditionalData attachAddData : attachAddDataList) {
				if (!attachAddData.getValue().equals("")) {
					AdditionalData clientData = new AdditionalData();
					clientData.setKey(Constants.LABEL_COD_AVERIA);
					clientData.setValue(attachAddData.getValue());
					lstClientData.add(clientData);
					result += attachment.getNameAttachment().concat(";").concat(Boolean.FALSE.toString()).concat(",");
				} else {
					result += attachment.getNameAttachment().concat(";").concat(Boolean.TRUE.toString()).concat(",");
				}
				indicador = Boolean.TRUE;
			}
			if (!indicador) {
				result += attachment.getNameAttachment().concat(";").concat(Boolean.TRUE.toString()).concat(",");
			}
		}

		return result;
	}
	
	private String getNoFaultsCable(Attachment attachment, String result, List<AdditionalData> lstClientData) {
		if (attachment.getNameAttachment().equals("AveriasPendientes[{}]registrar-averia-cms")
				|| attachment.getNameAttachment().equals("AveriasPendientes[{}]registrar-averia-amdocs")
				|| attachment.getNameAttachment().equals("ValidaMasiva[{}]registrar-averia-cms")
				|| attachment.getNameAttachment().equals("ValidaMasiva[{}]registrar-averia-amdocs")) {

			Boolean indicador = Boolean.FALSE;
			List<AdditionalData> attachAddDataList = getValue(attachment.getIdAttachment(), "numero-requerimiento-averia-cms");
			for (AdditionalData attachAddData : attachAddDataList) {
				if (!attachAddData.getValue().equals("")) {
					AdditionalData clientData = new AdditionalData();
					clientData.setKey(Constants.LABEL_COD_AVERIA);
					clientData.setValue(attachAddData.getValue());
					lstClientData.add(clientData);
					result += attachment.getNameAttachment().concat(";").concat(Boolean.FALSE.toString()).concat(",");
				} else {
					result += attachment.getNameAttachment().concat(";").concat(Boolean.TRUE.toString()).concat(",");
				}
				indicador = Boolean.TRUE;
			}
			if (!indicador) {
				result += attachment.getNameAttachment().concat(";").concat(Boolean.TRUE.toString()).concat(",");
			}
		}

		return result;
	}
	
	private String getTechTroubles(Attachment attachment, String result) {
		if (attachment.getNameAttachment().equals("RecomendacionesDespachoHFC[{}]realiza-sincronizacion-iwy2")
				|| attachment.getNameAttachment()
						.equals("RecomendacionesDespachoHFC[{}]realiza-sincronizacion-multiconsulta")
				|| attachment.getNameAttachment().equals("RecomendacionesDespachoHFC[{}]realiza-reset-modem-iwy2")
				|| attachment.getNameAttachment()
						.equals("RecomendacionesDespachoHFC[{}]realiza-sincronizacion-iwy2-despues-reset-modem")) {

			List<AdditionalData> attachAddDataList = getValue(attachment.getIdAttachment(), "estado-reset-modem-ok");
			for (AdditionalData attachAddData : attachAddDataList) {
				Boolean check = Boolean.TRUE;
				if (attachAddData.getValue().equals("SI")) {
					check = Boolean.FALSE;
				}
				result += attachment.getNameAttachment().concat(";").concat(check.toString()).concat(",");
			}
		}

		return result;
	}

	private void attachEquivalence(String result, List<AdditionalData> lstClientData, Ticket ticket) {
		List<Equivalence> equivalenceList = getAttachmentEquivalence(ticket.getId());
		int index = 1;

		String[] list = result.split(",");

		for (Equivalence equivalence : equivalenceList) {
			String success = "false";
			for (int indice = 0; indice < list.length; indice++) {
				String[] value = list[indice].split(";");
				if (value[0].equals(equivalence.getAttachmentName())) {
					success = value[1];
				}
			}

			AdditionalData clientData = new AdditionalData();
			clientData.setKey(Constants.LABEL_FRONT_END.concat(String.valueOf(index)));
			clientData.setValue(equivalence.getNameEquivalence());
			clientData.setCheck(success);
			lstClientData.add(clientData);

			index++;
		}
	}

	private List<AdditionalData> fillNotificationTicket(Ticket ticket, List<AdditionalData> lstClientData) {
		AdditionalData clientData = null;
		List<AdditionalData> lstAdditionalData = ticket.getAdditionalData();

		if (lstAdditionalData != null) {
			for (AdditionalData additionalData : lstAdditionalData) {
				if (additionalData.getKey().equals("notification-id")) {
					String usecase = "";
					if (ticket.getUseCaseId().equals(Constants.USE_CASE_INTERNET)) {
						usecase = Constants.TICKET_INTERNET_HFC;
					} else if (ticket.getUseCaseId().equals(Constants.USE_CASE_INTERNET_GPON)) {
						usecase = Constants.TICKET_INTERNET_GPON;
					} else if (ticket.getUseCaseId().equals(Constants.USE_CASE_CABLE)) {
						usecase = Constants.TICKET_TV;
					}					
					EquivalenceNotification equivalence = getNotificationEquivalence(additionalData.getValue(), usecase);

					if (equivalence != null) {
						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_ACTION);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getAction()));
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_TITLE);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getTitle()));
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_BODY);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getBody()));
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_FOOTER);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getFooter()));
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_ICON);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getIcon()));
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_BUTTON);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getButton()));
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_IMAGE);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getImage()));
						lstClientData.add(clientData);

						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_ACTION_BUTTON);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getActionbutton()));
						lstClientData.add(clientData);
						
						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_WINDOW_ID);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getWindows()));
						lstClientData.add(clientData);
						
						// Campos adicionales para Averia Individual - Averia Individual asociada a una masiva
						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_SUBTITLE);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getSubtitle()));
						lstClientData.add(clientData);
						
						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_ICON2);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getIcon2()));
						lstClientData.add(clientData);
						
						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_SUBTITLE2);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getSubtitle2()));
						lstClientData.add(clientData);
						
						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_WINDOW_ID2);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getWindows2()));
						lstClientData.add(clientData);

						// Campos adicionales para pase a Whastaspp
						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_BUTTON2);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getButton2()));
						lstClientData.add(clientData);
						
						clientData = new AdditionalData();
						clientData.setKey(Constants.LABEL_ACTION_BUTTON2);
						clientData.setValue(StringUtil.validateEmptyField(equivalence.getActionbutton2()));
						lstClientData.add(clientData);
					}
				}
			}
		}
		return lstClientData;
	}
}