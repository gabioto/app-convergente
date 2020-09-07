package com.tdp.ms.autogestion.autogestion.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdp.ms.autogestion.model.AdditionalData;
import com.tdp.ms.autogestion.model.Equivalence;
import com.tdp.ms.autogestion.model.EquivalenceNotification;
import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.OAuth;
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


@ExtendWith(MockitoExtension.class)
public class TicketRepositoryTest {

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

	
	private static Optional<List<TblTicket>> optLstTicket;
	
	private static Optional<List<TblEquivalence>> optLstEquivalence;
	
	private static Optional<TblEquivalenceNotification> equivalenceNotification;
	
	private static Map<String, Ticket> ticketResponseMap = new HashMap<>();
	private static Map<String, Ticket> ticketRequestMap = new HashMap<>();
	private static OAuth oAuthResponseMap;
	private static Ticket ticketComplete, ticketInitial;
	
	@BeforeAll
	public static void setup() throws JsonProcessingException {
		TblTicket ticket = new TblTicket();
		ticket.setIdTicket(1);
		ticket.setIdTicketTriage(19406791);
		ticket.setDescription("averia");
		ticket.setCreationDate(LocalDateTime.now(ZoneOffset.of("-05:00")));		
		ticket.setStatusChangeDate(LocalDateTime.now(ZoneOffset.of("-05:00")));
		ticket.setStatus("WHATSAPP");
		ticket.setModifiedDateTicket(LocalDateTime.now(ZoneOffset.of("-05:00")));
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
		//ticket.setTblAdditionalData(new ArrayList<TblAdditionalData>());
		//ticket.setTblAttachments(new ArrayList<TblAttachment>());
		
		List<TblTicket> lstTicket = new ArrayList<TblTicket>();
		lstTicket.add(ticket);
		
		optLstTicket = Optional.of(lstTicket);
		
		List<TblEquivalence> lstTblEquivalence = new ArrayList<>();
		TblEquivalence tblEquivalence = new TblEquivalence();	
		tblEquivalence.setIdEquivalence(1);
		tblEquivalence.setNameEquivalence("Servicio Activo");
		tblEquivalence.setAttachmentName("ValidacionesInicialesInternet[{}]recupera-info-telefono");
		lstTblEquivalence.add(tblEquivalence);
		//Primer Objeto
		tblEquivalence = new TblEquivalence();
		tblEquivalence.setIdEquivalence(2);
		tblEquivalence.setNameEquivalence("Sin Deuda Pendiente");
		tblEquivalence.setAttachmentName("ValidacionesInicialesInternet[{}]recupera-deuda-amdocs");
		lstTblEquivalence.add(tblEquivalence);
		//Tercer Objeto
		tblEquivalence = new TblEquivalence();		
		tblEquivalence.setIdEquivalence(3);
		tblEquivalence.setNameEquivalence("Sin Deuda Pendiente");
		tblEquivalence.setAttachmentName("ValidacionesInicialesInternet[{}]recupera-deuda-cms");
		lstTblEquivalence.add(tblEquivalence);
		//Cuarto Objeto
		tblEquivalence = new TblEquivalence();
		tblEquivalence.setIdEquivalence(4);
		tblEquivalence.setNameEquivalence("Sin Deuda Pendiente");
		tblEquivalence.setAttachmentName("ValidacionesInicialesInternet[{}]recupera-deuda-atis");
		lstTblEquivalence.add(tblEquivalence);
		//Quinto Objeto
		tblEquivalence = new TblEquivalence();
		tblEquivalence.setIdEquivalence(5);
		tblEquivalence.setNameEquivalence("Sin Orden de Reconexión");
		tblEquivalence.setAttachmentName("ValidacionesInicialesInternet[{}]recupera-reconexion-pendiente-amdocs");
		lstTblEquivalence.add(tblEquivalence);
		optLstEquivalence = Optional.of(lstTblEquivalence);
		
		TblEquivalenceNotification tblEquivalenceNotification= new TblEquivalenceNotification();
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
		tblEquivalenceNotification.setActionbutton("https://api.whatsapp.com/send?phone=51999955555&text=%C2%A1Hola!%20Me%20derivaron%20de%20la%20app%20para%20que%20me%20ayuden%20a%20resolver%20la%20avería%20de%20internet%20en%20mi%20hogar.%20Mi%20código%20de%20atención%20es%20FCR101");
		equivalenceNotification = Optional.of(tblEquivalenceNotification);
		
		LocalDateTime actualDate = LocalDateTime.now(ZoneOffset.of("-05:00"));
		ticketComplete = new Ticket(0, 19406743, "/ticket/v2/tickets/19406743", "averia", actualDate, "minor",
				"TroubleTicket", "acknowledged", actualDate, "Ticket generado", 1, "", "20000032", "99", "serviceCode",
				"broadband", "3", null, new Customer("70981983", "DNI", "10368606"), null, actualDate,
				new ArrayList<>(), new ArrayList<>());

		ticketInitial = new Ticket(0, 0, null, "averia", null, "minor", "TroubleTicket", null, null, null, 1, "",
				"20000032", "99", "serviceCode", "broadband", "3", null, new Customer("70981983", "DNI", "10368606"),
				null, null, new ArrayList<>(), new ArrayList<>());

		oAuthResponseMap = new OAuth();

		// TICKET RESPONSE
		ticketResponseMap.put("generated_ticket", ticketComplete);

		// TICKET REQUEST
		ticketRequestMap.put("ticket_to_generate", ticketInitial);
		ticketRequestMap.put("generated_ticket", ticketComplete);
	}

	@Test
	void createTicket_generateTicket() {
		when(ticketApi.generate(any(OAuth.class), any(Ticket.class)))
				.thenReturn(ticketResponseMap.get("generated_ticket"));

		Ticket ticket = ticketRepository.generateTicket(oAuthResponseMap, ticketRequestMap.get("ticket_to_generate"));

		assertNotNull(ticket);
	}

	@Test
	void createTicket_saveGeneratedTicketWithCustomer() {
		when(jpaCustomerRepository.findById(any(TblCustomerPK.class))).thenReturn(Optional.of(new TblCustomer()));

		saveGeneratedTicket();
	}

	@Test
	void ticketRepository_getTicket() throws Exception {
		when(ticketRepository.getTicket(anyInt())).thenReturn(optLstTicket.get().get(0).fromThis());
		Ticket ticket = ticketRepository.getTicket(19406791);
		assertNotNull(ticket);		
	}
	
	void createTicket_saveGeneratedTicketWithoutCustomer() {
		when(jpaCustomerRepository.findById(any(TblCustomerPK.class))).thenReturn(Optional.empty());

		when(jpaCustomerRepository.save(any())).thenReturn(new TblCustomer());

		saveGeneratedTicket();
	}

	private void saveGeneratedTicket() {
		when(jpaTicketRepository.save(any())).thenReturn(new TblTicket());

		ticketRepository.saveGeneratedTicket(ticketRequestMap.get("generated_ticket"));
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
	
	
}
