package com.tdp.ms.autogestion.autogestion.repository;

import static org.assertj.core.api.Assertions.anyOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
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
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdp.ms.autogestion.model.AdditionalData;
import com.tdp.ms.autogestion.model.Attachment;
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
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachmentAdditionalData;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomer;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomerPK;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalence;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalenceNotification;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;
import com.tdp.ms.autogestion.util.Constants;


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
	
	private static Optional<List<TblAttachmentAdditionalData>> lstTblAttachmentAdditionalData;
	
	
	private static Optional<List<TblEquivalence>> lstTblEquivalence;
	private static Map<String, Ticket> ticketResponseMap = new HashMap<>();
	private static Map<String, Ticket> ticketRequestMap = new HashMap<>();
	private static OAuth oAuthResponseMap;
	private static Ticket ticketComplete, ticketInitial;
	
	private static Optional<List<AdditionalData>> lstClientData ;
	private static Optional<Ticket> ticket;
	
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
		ticketComplete = new Ticket(1116, 19406743, "/ticket/v2/tickets/19406743", "averia", actualDate, "minor",
				"TroubleTicket", "acknowledged", actualDate, "Ticket generado", 1, "", "20000032", "99", "serviceCode",
				"broadband", "3", null, new Customer("70981983", "DNI", "10368606"), null, actualDate,
				new ArrayList<>(), new ArrayList<>());

		ticketInitial = new Ticket(0, 0, null, "averia", null, "minor", "TroubleTicket", null, null, null, 1, "",
				"20000032", "99", "serviceCode", "broadband", "3", null, new Customer("70981983", "DNI", "10368606"),
				null, null, new ArrayList<>(), new ArrayList<>());

		oAuthResponseMap = new OAuth();
		
		List<AdditionalData> listClientData = new ArrayList<AdditionalData>();
		AdditionalData clientData = new AdditionalData();		
		clientData.setKey(Constants.LABEL_STATUS);
		clientData.setValue(ticket.getStatus());
		clientData.setCheck("True");
		listClientData.add(clientData);
		
		//Segundo Object		
		clientData = new AdditionalData();	
		clientData.setKey("sub-operation-code");
		clientData.setValue("99");
		clientData.setCheck("True");
		listClientData.add(clientData);
		//Tercer Object		
		clientData = new AdditionalData();	
		clientData.setKey("phone");
		clientData.setValue("17695421");
		clientData.setCheck("True");
		listClientData.add(clientData);
		//Cuarto Object		
		clientData = new AdditionalData();	
		clientData.setKey("use-case-id");
		clientData.setValue("20000032");
		clientData.setCheck("True");
		listClientData.add(clientData);
		//Quinto Object		
		clientData = new AdditionalData();	
		clientData.setKey("notification-id");
		clientData.setValue("20000032-035");
		clientData.setCheck("True");
		listClientData.add(clientData);
		
		List<Attachment> attachments=new ArrayList<Attachment>();
		Attachment attachment=new Attachment();
		attachment.setIdAttachment(665);
		attachment.setIdAttachmentKafka(529102);
		attachment.setCreationDate(LocalDateTime.now());
		attachment.setNameAttachment("ValidacionesInicialesInternet[{}]recupera-estado-telefono");
		
		List<AdditionalData> lstAdditionalDataDos=new ArrayList<>();
		AdditionalData additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("fecha-envio");
		additionalDataDos.setValue("2020-09-04T12:42:10.850-05:00");
		lstAdditionalDataDos.add(additionalDataDos);
		/*********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("fecha-respuesta");
		additionalDataDos.setValue("2020-09-04T12:42:14.826-05:00");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("flujo");
		additionalDataDos.setValue("FCRSM-FIJA-NO-NAVEGA");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("canal");
		additionalDataDos.setValue("APP");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("tipo");
		additionalDataDos.setValue("interno");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("estado");
		additionalDataDos.setValue("OK");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("codigo-respuesta");
		additionalDataDos.setValue("00000000");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("descripcion-respuesta");
		additionalDataDos.setValue("Servicio ejecutado");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("public-id");
		additionalDataDos.setValue("13013701");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("sistema-origen");
		additionalDataDos.setValue("1");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("customer-id");
		additionalDataDos.setValue("8689752");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("account-id-cms");
		additionalDataDos.setValue("10148226");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("customer-id-atis");
		additionalDataDos.setValue("006359100");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("codigo-servicio-cms");
		additionalDataDos.setValue("10368606");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("estado-migracion");
		additionalDataDos.setValue("2");
		lstAdditionalDataDos.add(additionalDataDos);
		/**********/
		additionalDataDos= new AdditionalData();
		additionalDataDos.setKey("account-id-atis");
		additionalDataDos.setValue("589083010");
		lstAdditionalDataDos.add(additionalDataDos);
		
		attachment.setAdditionalData(lstAdditionalDataDos);
		attachments.add(attachment);
		
		attachment=new Attachment();
		attachment.setIdAttachment(666);
		attachment.setIdAttachmentKafka(529103);
		attachment.setCreationDate(LocalDateTime.now());
		attachment.setNameAttachment("ValidacionesInicialesInternet[{}]recupera-identificacion-cliente-atis");
		
		List<AdditionalData> lstAdditionalDataUno=new ArrayList<>();
		AdditionalData additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("fecha-envio");
		additionalDataUno.setValue("2020-09-04T12:42:14.833-05:00");
		lstAdditionalDataUno.add(additionalDataUno);
		
		List<TblAttachmentAdditionalData> listTblAttachmentAdditionalData=new ArrayList<>();
		TblAttachmentAdditionalData tblAttachmentAdditionalData=new TblAttachmentAdditionalData();
		tblAttachmentAdditionalData.setKeyAttachmentAdditional("fecha-envio");
		tblAttachmentAdditionalData.setValueAttachmentAdditional("2020-09-04T12:42:14.833-05:00");
		listTblAttachmentAdditionalData.add(tblAttachmentAdditionalData);
		/***********/
		additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("fecha-respuesta");
		additionalDataUno.setValue("2020-09-04T12:42:16.506-05:00");
		lstAdditionalDataUno.add(additionalDataUno);
		
		tblAttachmentAdditionalData=new TblAttachmentAdditionalData();
		tblAttachmentAdditionalData.setKeyAttachmentAdditional("fecha-respuesta");
		tblAttachmentAdditionalData.setValueAttachmentAdditional("2020-09-04T12:42:16.506-05:00");
		listTblAttachmentAdditionalData.add(tblAttachmentAdditionalData);
		
		/***********/
		additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("flujo");
		additionalDataUno.setValue("FCRSM-FIJA-NO-NAVEGA");
		lstAdditionalDataUno.add(additionalDataUno);
		
		tblAttachmentAdditionalData=new TblAttachmentAdditionalData();
		tblAttachmentAdditionalData.setKeyAttachmentAdditional("flujo");
		tblAttachmentAdditionalData.setValueAttachmentAdditional("FCRSM-FIJA-NO-NAVEGA");
		listTblAttachmentAdditionalData.add(tblAttachmentAdditionalData);
		lstTblAttachmentAdditionalData = Optional.of(listTblAttachmentAdditionalData);
		
		/***********/
		additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("canal");
		additionalDataUno.setValue("APP");
		lstAdditionalDataUno.add(additionalDataUno);
		/***********/
		additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("tipo");
		additionalDataUno.setValue("interno");
		lstAdditionalDataUno.add(additionalDataUno);
		/***********/
		additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("estado");
		additionalDataUno.setValue("OK");
		lstAdditionalDataUno.add(additionalDataUno);
		/***********/
		additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("codigo-respuesta");
		additionalDataUno.setValue("00000000");
		lstAdditionalDataUno.add(additionalDataUno);
		/***********/
		additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("descripcion-respuesta");
		additionalDataUno.setValue("Servicio ejecutado");
		lstAdditionalDataUno.add(additionalDataUno);
		/***********/
		additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("nombre-identificacion");
		additionalDataUno.setValue("");
		lstAdditionalDataUno.add(additionalDataUno);
		/***********/
		additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("codigo-contacto");
		additionalDataUno.setValue("");
		lstAdditionalDataUno.add(additionalDataUno);
		/***********/
		additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("numero-identificacion");
		additionalDataUno.setValue("");
		lstAdditionalDataUno.add(additionalDataUno);
		/***********/
		additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("codigo-identificacion");
		additionalDataUno.setValue("");
		lstAdditionalDataUno.add(additionalDataUno);
		/***********/
		additionalDataUno= new AdditionalData();
		additionalDataUno.setKey("tipo-identificacion");
		additionalDataUno.setValue("");
		lstAdditionalDataUno.add(additionalDataUno);
		
		attachment.setAdditionalData(lstAdditionalDataUno);
		attachments.add(attachment);
		
		attachment=new Attachment();
		attachment.setIdAttachment(667);
		attachment.setIdAttachmentKafka(529104);
		attachment.setCreationDate(LocalDateTime.now());
		attachment.setNameAttachment("ValidacionesInicialesInternet[{}]recupera-info-telefono");
		
		List<AdditionalData> lstAdditionalData=new ArrayList<>();
		AdditionalData additionalData= new AdditionalData();
		additionalData.setKey("fecha-envio");
		additionalData.setValue("2020-09-04T12:42:16.514-05:00");
		lstAdditionalData.add(additionalData);
		/*******/
		additionalData= new AdditionalData();
		additionalData.setKey("fecha-respuesta");
		additionalData.setValue("2020-09-04T12:42:21.365-05:00");
		lstAdditionalData.add(additionalData);
		/*******/
		additionalData= new AdditionalData();
		additionalData.setKey("flujo");
		additionalData.setValue("FCRSM-FIJA-NO-NAVEGA");
		lstAdditionalData.add(additionalData);
		/*******/
		additionalData= new AdditionalData();
		additionalData.setKey("canal");
		additionalData.setValue("APP");
		lstAdditionalData.add(additionalData);
		/*******/
		additionalData= new AdditionalData();
		additionalData.setKey("tipo");
		additionalData.setValue("interno");
		lstAdditionalData.add(additionalData);
		/*******/
		additionalData= new AdditionalData();
		additionalData.setKey("estado");
		additionalData.setValue("OK");
		lstAdditionalData.add(additionalData);
		/*******/
		additionalData= new AdditionalData();
		additionalData.setKey("codigo-respuesta");
		additionalData.setValue("00000000");
		lstAdditionalData.add(additionalData);
		/*******/
		additionalData= new AdditionalData();
		additionalData.setKey("descripcion-respuesta");
		additionalData.setValue("Servicio ejecutado");
		lstAdditionalData.add(additionalData);
		/*******/
		additionalData= new AdditionalData();
		additionalData.setKey("estado-linea");
		additionalData.setValue("active");
		lstAdditionalData.add(additionalData);
		/*******/
		additionalData= new AdditionalData();
		additionalData.setKey("tecnologia-linea");
		additionalData.setValue("");
		lstAdditionalData.add(additionalData);		
		/*******/
		additionalData= new AdditionalData();
		additionalData.setKey("linea-voip");
		additionalData.setValue("");
		lstAdditionalData.add(additionalData);
		
		attachment.setAdditionalData(lstAdditionalData);
		attachments.add(attachment);
		
		attachment=new Attachment();
		attachment.setIdAttachment(668);
		attachment.setIdAttachmentKafka(529114);
		attachment.setCreationDate(LocalDateTime.now());
		attachment.setNameAttachment("RecomendacionesDespachoHFC[{}]realiza-sincronizacion-iwy2");
		
		List<AdditionalData> lstAdditionalDataTres=new ArrayList<>();
		AdditionalData additionalDataTres= new AdditionalData();
		additionalDataTres.setKey("estado-reset-modem-ok");
		additionalDataTres.setValue("SI");
		lstAdditionalDataTres.add(additionalDataTres);
		attachment.setAdditionalData(lstAdditionalDataTres);
		attachments.add(attachment);
		
		
		attachment=new Attachment();
		attachment.setIdAttachment(669);
		attachment.setIdAttachmentKafka(529115);
		attachment.setCreationDate(LocalDateTime.now());
		attachment.setNameAttachment("AveriaPendiente[{}]recupera-averia-pendiente-amdocs");
		
		List<AdditionalData> lstAdditionalDataCuatro=new ArrayList<>();
		AdditionalData additionalDataCuatro= new AdditionalData();
		additionalDataCuatro.setKey("codigo-averia");
		additionalDataCuatro.setValue("SI");
		lstAdditionalDataCuatro.add(additionalDataCuatro);
		attachment.setAdditionalData(lstAdditionalDataCuatro);
		attachments.add(attachment);
		
		attachment=new Attachment();
		attachment.setIdAttachment(670);
		attachment.setIdAttachmentKafka(529116);
		attachment.setCreationDate(LocalDateTime.now());
		attachment.setNameAttachment("ValidacionesInicialesInternet[{}]recupera-reconexion-pendiente-amdocs");

		List<AdditionalData> lstAdditionalDataCinco=new ArrayList<>();
		AdditionalData additionalDataCinco= new AdditionalData();
		additionalDataCinco.setKey("tiene-reconexion-pendiente");
		additionalDataCinco.setValue("SI");
		lstAdditionalDataCinco.add(additionalDataCinco);
		attachment.setAdditionalData(lstAdditionalDataCinco);
		attachments.add(attachment);
		//ValidacionesInicialesInternet[{}]recupera-deuda-amdocs
		
		attachment=new Attachment();
		attachment.setIdAttachment(671);
		attachment.setIdAttachmentKafka(529117);
		attachment.setCreationDate(LocalDateTime.now());
		attachment.setNameAttachment("ValidacionesInicialesInternet[{}]recupera-deuda-amdocs");

		List<AdditionalData> lstAdditionalDataInter=new ArrayList<>();
		AdditionalData additionalDataInter= new AdditionalData();
		additionalDataInter.setKey("tiene-reconexion-pendiente");
		additionalDataInter.setValue("monto");
		lstAdditionalDataInter.add(additionalDataInter);
		attachment.setAdditionalData(lstAdditionalDataInter);
		attachments.add(attachment);
		
		
		ticketComplete.setAttachments(attachments);
		ticketComplete.setAdditionalData(listClientData);
		//ticketComplete.getAttachments()
		lstClientData = Optional.of(listClientData);
		
		// TICKET RESPONSE
		ticketResponseMap.put("generated_ticket", ticketComplete);

		// TICKET REQUEST
		ticketRequestMap.put("ticket_to_generate", ticketInitial);
		ticketRequestMap.put("generated_ticket", ticketComplete);
		
		
		//List<AdditionalData> lstAdditionalData=new ArrayList<>();
//		AdditionalData additionalData=new AdditionalData();
//		additionalData.setCheck("");
//
//		lstAdditionalData.add(additionalData);
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

//	@Test
//	void ticketRepository_getTicket() throws Exception {
//		when(ticketRepository.getTicket(anyInt())).thenReturn(optLstTicket.get().get(0).fromThis());
//		Ticket ticket = ticketRepository.getTicket(19406791);
//		assertNotNull(ticket);		
//	}
	
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
	
	@Test
	void ticketRepository_getAdditionalData() {
		when(jpaEquivalenceNotificationRepository.getEquivalence(anyString())).thenReturn(equivalenceNotification);
		//JpaAttachmentAdditionalDataRepository
		//attachmentAdditionalDataRepository
		when(jpaEquivalenceRepository.getEquivalence(anyInt())).thenReturn(optLstEquivalence);
		when(attachmentAdditionalDataRepository.getValue(anyInt(), anyString())).thenReturn(lstTblAttachmentAdditionalData);
		 //jpaEquivalenceRepository.getEquivalence(ticketId)
//		TblTicket tblTicket= jpaTicketRepository.findById(1095).get();
//		Ticket ticket=tblTicket.fromThis();
		//ticketRequestMap.get("generated_ticket")
		List<AdditionalData> lstAdditionalData = ticketRepository.getAdditionalData(ticketRequestMap.get("generated_ticket"));
		assertNotNull(lstAdditionalData);
	}
	
	@Test
	void ticketRepository_findByCustomerAndUseCase() {
		
		when(jpaTicketRepository.findByCustomerAndUseCase(anyString(),anyString(),anyString(),anyString(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(optLstTicket.get());
		//new Customer("70981983", "DNI", "10368606")
		List<Ticket> listTicket= ticketRepository.findByCustomerAndUseCase("DNI", "70981983", "10368606", "broadband", LocalDateTime.now(ZoneOffset.of("-05:00")), LocalDateTime.now(ZoneOffset.of("-05:00")));
		//List<AdditionalData> lstAdditionalData = ticketRepository.getAdditionalData(ticketRequestMap.get("generated_ticket"));
		assertNotNull(listTicket);
	}
	
	@Test
	void ticketRepository_findByCustomerAndUseCasePast() {
		
		when(jpaTicketRepository.findByCustomerAndUseCasePast(anyString(),anyString(),anyString(),anyString())).thenReturn(optLstTicket.get());
		//new Customer("70981983", "DNI", "10368606")
		List<Ticket> listTicket= ticketRepository.findByCustomerAndUseCasePast("DNI", "70981983", "10368606", "broadband");
		//List<AdditionalData> lstAdditionalData = ticketRepository.getAdditionalData(ticketRequestMap.get("generated_ticket"));
		assertNotNull(listTicket);
	}
	
	@Test
	void ticketRepository_getTicketStatus() {
		
		when(jpaTicketRepository.getTicketStatus(anyInt())).thenReturn(optLstTicket);
		//new Customer("70981983", "DNI", "10368606")
		List<Ticket> listTicket= ticketRepository.getTicketStatus(1116);
		//List<AdditionalData> lstAdditionalData = ticketRepository.getAdditionalData(ticketRequestMap.get("generated_ticket"));
		assertNotNull(listTicket);
	}
	
}
