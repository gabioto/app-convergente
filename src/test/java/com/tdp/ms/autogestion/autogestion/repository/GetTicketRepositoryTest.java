package com.tdp.ms.autogestion.autogestion.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdp.ms.autogestion.model.AdditionalData;
import com.tdp.ms.autogestion.model.Attachment;
import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.Equivalence;
import com.tdp.ms.autogestion.model.EquivalenceNotification;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.TicketRepositoryImpl;
import com.tdp.ms.autogestion.repository.datasource.api.TicketApi;
import com.tdp.ms.autogestion.repository.datasource.db.JpaAttachmentAdditionalDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaCustomerRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceNotificationRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaTicketRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomer;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomerPK;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalence;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalenceNotification;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;
import com.tdp.ms.autogestion.util.Constants;

@ExtendWith(MockitoExtension.class)
public class GetTicketRepositoryTest {

	@InjectMocks
	private TicketRepositoryImpl ticketRepository;

	@Mock
	private TicketApi ticketApi;

	@Mock
	private JpaCustomerRepository jpaCustomerRepository;

	@Mock
	private JpaTicketRepository jpaTicketRepository;

	@Mock
	private JpaEquivalenceRepository jpaEquivalenceRepository;

	@Mock
	private JpaEquivalenceNotificationRepository jpaEquivalenceNotificationRepository;

	@Mock
	private JpaAttachmentAdditionalDataRepository attachmentAdditionalDataRepository;

	private static Map<String, Ticket> ticketRequestMap = new HashMap<>();
	private static Optional<List<TblTicket>> optLstTicket;
	private static Optional<List<TblEquivalence>> optLstEquivalence;
	private static Optional<TblEquivalenceNotification> equivalenceNotification;
	private static Optional<List<AdditionalData>> lstClientData;
	private static Ticket ticketComplete;

	@BeforeAll
	public static void setup() throws JsonProcessingException {
		LocalDateTime actualDate = LocalDateTime.now(ZoneOffset.of("-05:00"));

		ticketComplete = new Ticket(0, 19406743, "/ticket/v2/tickets/19406743", "averia", actualDate, "minor",
				"TroubleTicket", "acknowledged", actualDate, "Ticket generado", 1, "", "20000032", "99", "serviceCode",
				"broadband", "3", null, new Customer("70981983", "DNI", "10368606"), null, actualDate,
				new ArrayList<>(), new ArrayList<>());

		ticketRequestMap.put("generated_ticket", ticketComplete);

		TblTicket ticket = new TblTicket();
		ticket.setIdTicket(1);
		ticket.setIdTicketTriage(19406791);
		ticket.setDescription("averia");
		ticket.setCreationDate(actualDate);
		ticket.setStatusChangeDate(actualDate);
		ticket.setStatus("WHATSAPP");
		ticket.setModifiedDateTicket(actualDate);
		ticket.setSeverity("");
		ticket.setTicketType("");
		ticket.setStatusChangeReason("");
		ticket.setPriority(1);
		ticket.setTechnology("");
		ticket.setIdUseCase("");
		ticket.setProductIdentifier("");
		ticket.setInvolvement("");

		TblCustomer tblCustomer = new TblCustomer();
		TblCustomerPK tblCustomerPK = new TblCustomerPK();
		tblCustomerPK.setDocumentNumber("40504339");
		tblCustomerPK.setDocumentType("DNI");
		tblCustomerPK.setServiceCode("10000");
		tblCustomer.setId(tblCustomerPK);
		ticket.setTblCustomer(tblCustomer);
		ticket.setStatusTicket("");
		// ticket.setTblAdditionalData(new ArrayList<TblAdditionalData>());
		// ticket.setTblAttachments(new ArrayList<TblAttachment>());

		List<TblTicket> lstTicket = new ArrayList<TblTicket>();
		lstTicket.add(ticket);

		optLstTicket = Optional.of(lstTicket);

		List<TblEquivalence> lstTblEquivalence = new ArrayList<>();
		TblEquivalence tblEquivalence = new TblEquivalence();
		tblEquivalence.setIdEquivalence(1);
		tblEquivalence.setNameEquivalence("Servicio Activo");
		tblEquivalence.setAttachmentName("ValidacionesInicialesInternet[{}]recupera-info-telefono");
		lstTblEquivalence.add(tblEquivalence);
		// Primer Objeto
		tblEquivalence = new TblEquivalence();
		tblEquivalence.setIdEquivalence(2);
		tblEquivalence.setNameEquivalence("Sin Deuda Pendiente");
		tblEquivalence.setAttachmentName("ValidacionesInicialesInternet[{}]recupera-deuda-amdocs");
		lstTblEquivalence.add(tblEquivalence);
		// Tercer Objeto
		tblEquivalence = new TblEquivalence();
		tblEquivalence.setIdEquivalence(3);
		tblEquivalence.setNameEquivalence("Sin Deuda Pendiente");
		tblEquivalence.setAttachmentName("ValidacionesInicialesInternet[{}]recupera-deuda-cms");
		lstTblEquivalence.add(tblEquivalence);
		// Cuarto Objeto
		tblEquivalence = new TblEquivalence();
		tblEquivalence.setIdEquivalence(4);
		tblEquivalence.setNameEquivalence("Sin Deuda Pendiente");
		tblEquivalence.setAttachmentName("ValidacionesInicialesInternet[{}]recupera-deuda-atis");
		lstTblEquivalence.add(tblEquivalence);
		// Quinto Objeto
		tblEquivalence = new TblEquivalence();
		tblEquivalence.setIdEquivalence(5);
		tblEquivalence.setNameEquivalence("Sin Orden de Reconexión");
		tblEquivalence.setAttachmentName("ValidacionesInicialesInternet[{}]recupera-reconexion-pendiente-amdocs");
		lstTblEquivalence.add(tblEquivalence);
		optLstEquivalence = Optional.of(lstTblEquivalence);

		TblEquivalenceNotification tblEquivalenceNotification = new TblEquivalenceNotification();
		tblEquivalenceNotification.setIdEquivalenceNotification(1);
		tblEquivalenceNotification.setCode("20000032-001");
		tblEquivalenceNotification.setDescription("No se pudo consultar al cliente");
		tblEquivalenceNotification.setTitle("Encontramos una incidencia que no podemos solucionar en linea");
		tblEquivalenceNotification.setBody("Un técnico especializado te atenderá a través de Whatsapp");
		tblEquivalenceNotification.setFooter("");
		tblEquivalenceNotification.setIcon("error");
		tblEquivalenceNotification.setAction("WHATSAPP");
		tblEquivalenceNotification.setButton("Ir a whastapp");
		tblEquivalenceNotification.setImage("whastapp");
		tblEquivalenceNotification.setActionbutton(
				"https://api.whatsapp.com/send?phone=51999955555&text=%C2%A1Hola!%20Me%20derivaron%20de%20la%20app%20para%20que%20me%20ayuden%20a%20resolver%20la%20avería%20de%20internet%20en%20mi%20hogar.%20Mi%20código%20de%20atención%20es%20FCR101");
		equivalenceNotification = Optional.of(tblEquivalenceNotification);

		List<AdditionalData> listClientData = new ArrayList<AdditionalData>();
		AdditionalData clientData = new AdditionalData();
		clientData.setKey(Constants.LABEL_STATUS);
		clientData.setValue(ticket.getStatus());
		clientData.setCheck("True");
		listClientData.add(clientData);
		List<Attachment> attachments = new ArrayList<Attachment>();
		Attachment attachment = new Attachment();
		attachment.setIdAttachment(665);
		attachment.setIdAttachmentKafka(529102);
		attachment.setCreationDate(LocalDateTime.now());
		attachment.setNameAttachment("ValidacionesInicialesInternet[{}]recupera-estado-telefono");
		attachments.add(attachment);

		attachment = new Attachment();
		attachment.setIdAttachment(666);
		attachment.setIdAttachmentKafka(529103);
		attachment.setCreationDate(LocalDateTime.now());
		attachment.setNameAttachment("ValidacionesInicialesInternet[{}]recupera-identificacion-cliente-atis");
		attachments.add(attachment);

		attachment = new Attachment();
		attachment.setIdAttachment(667);
		attachment.setIdAttachmentKafka(529104);
		attachment.setCreationDate(LocalDateTime.now());
		attachment.setNameAttachment("ValidacionesInicialesInternet[{}]recupera-info-telefono");
		attachments.add(attachment);

		ticketComplete.setAttachments(attachments);
		ticketComplete.setAdditionalData(listClientData);
		lstClientData = Optional.of(listClientData);
		// List<AdditionalData> lstAdditionalData=new ArrayList<>();
//				AdditionalData additionalData=new AdditionalData();
//				additionalData.setCheck("");
		//
//				lstAdditionalData.add(additionalData);
	}

	@Test
	void ticketRepository_getTicket() throws Exception {
		when(jpaTicketRepository.getTicket(anyInt())).thenReturn(optLstTicket);
		Ticket ticket = ticketRepository.getTicket(19406791);
		assertNotNull(ticket);
	}

	@Test
	void ticketRepository_getAttachmentEquivalence() throws Exception {
		when(jpaEquivalenceRepository.getEquivalence(anyInt())).thenReturn(optLstEquivalence);
		List<Equivalence> listEquivalence = ticketRepository.getAttachmentEquivalence(19406791);
		assertNotNull(listEquivalence);
	}

	@Test
	void ticketRepository_getNotificationEquivalence() throws Exception {
		when(jpaEquivalenceNotificationRepository.getEquivalence(anyString())).thenReturn(equivalenceNotification);
		EquivalenceNotification equivalenceNotification = ticketRepository.getNotificationEquivalence("20000032-001");
		assertNotNull(equivalenceNotification);
	}

	@Test
	void ticketRepository_getAdditionalData() {
//		when(ticketRepository.fillAttachmentsTicket(ticketRequestMap.get("generated_ticket"), lstClientData.get()))
//				.thenReturn(lstClientData.get());
		
//		when(methodCall)

		List<AdditionalData> lstAdditionalData = ticketRepository.getAdditionalData(new Ticket());
		
//		List<AdditionalData> lstAdditionalData = ticketRepository
//				.fillAttachmentsTicket(ticketRequestMap.get("generated_ticket"), lstClientData.get());
		assertNotNull(lstAdditionalData);
		assertEquals(2, lstAdditionalData.size());
	}

	@Test
	void ticketRepository_fillAttachmentsTicket() throws Exception {

	}

	@Test
	void ticketRepository_getCommercialStatus() throws Exception {

	}
}
