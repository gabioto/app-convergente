package com.tdp.ms.autogestion.autogestion.expose;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.tdp.ms.autogestion.expose.KafkaController;
import com.tdp.ms.autogestion.model.LogData;
import com.tdp.ms.autogestion.model.TicketStatus;
import com.tdp.ms.autogestion.repository.datasource.db.JpaAdditionalDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaAttachmentRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceNotificationRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaTicketRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAdditionalData;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachment;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomer;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomerPK;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalence;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblEquivalenceNotification;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;
import com.tdp.ms.autogestion.util.FunctionsUtil;

@ExtendWith(MockitoExtension.class)
public class KafkaControllerTest {

	@InjectMocks
	private KafkaController kafkaController;

	@Mock
	private JpaTicketRepository ticketRepository;

	@Mock
	private JpaAdditionalDataRepository additionalDataRepository;

	@Mock
	private JpaAttachmentRepository attachmentRepository;

	@Mock
	private JpaEquivalenceRepository equivalenceRepository;

	@Mock
	private JpaEquivalenceNotificationRepository equivalenceNotificationRepository;

	@Mock
	private FunctionsUtil functionsUtil;

	@Mock
	private EntityManager entityManager;

	private static List<TblEquivalence> lstTblEquivalence = new ArrayList<>();
	private static List<TblTicket> listTickets = new ArrayList<TblTicket>();
	private static TblEquivalenceNotification tblEquivalenceNotification;

	private final String message = "{\"eventId\":\"d796680b-56d5-4010-b83d-0c6ccd40e95e\",\"eventTime\":\"2020-07-21T22:40:52-05:00\",\"eventType\":"
			+ "\"notifyTroubleTicketAttributeValueChangeEvent\",\"event\":{\"troubleTicket\":{\"id\":\"19215307\",\"href\":\"\\/ticket\\/v2\\/tickets"
			+ "\\/19215307\",\"correlationId\":null,\"subject\":null,\"description\":\"SolucionAPP\",\"creationDate\":\"2020-07-21T22:41:33.870-05:00\""
			+ ",\"severity\":\"catastrophic\",\"priority\":3,\"type\":\"TroubleTicket\",\"status\":\"in progress\",\"channel\":{\"id\":\"3\",\"href\":\""
			+ "\\/channel\\/v2\\/channels\\/3\",\"name\":\"APP\"},\"statusChangeDate\":\"2020-07-21T22:41:33.870-05:00\",\"statusChangeReason\":"
			+ "\"Ejecutando Flujo\",\"statusHistory\":[{\"statusChangeDate\":\"2020-07-21T22:40:49.364-05:00\",\"statusChangeReason\":\"Ejecutando Flujo\""
			+ ",\"ticketStatus\":\"in progress\"},{\"statusChangeDate\":\"2020-07-21T22:40:49.398-05:00\",\"statusChangeReason\":\"Ejecutando Flujo\""
			+ ",\"ticketStatus\":\"in progress\"},{\"statusChangeDate\":\"2020-07-21T22:40:51.971-05:00\",\"statusChangeReason\":\"Ejecutando Flujo\""
			+ ",\"ticketStatus\":\"in progress\"},{\"statusChangeDate\":\"2020-07-21T22:40:51.994-05:00\",\"statusChangeReason\":\"Ejecutando Flujo\""
			+ ",\"ticketStatus\":\"in progress\"},{\"statusChangeDate\":\"2020-07-21T22:40:52.019-05:00\",\"statusChangeReason\":\"Ejecutando Flujo\""
			+ ",\"ticketStatus\":\"in progress\"},{\"statusChangeDate\":\"2020-07-21T22:40:52.040-05:00\",\"statusChangeReason\":\"Ejecutando Flujo\""
			+ ",\"ticketStatus\":\"in progress\"},{\"statusChangeDate\":\"2020-07-21T22:40:52.352-05:00\",\"statusChangeReason\":\"Ejecutando Flujo\""
			+ ",\"ticketStatus\":\"in progress\"},{\"statusChangeDate\":\"2020-07-21T22:40:52.376-05:00\",\"statusChangeReason\":\"Ejecutando Flujo\""
			+ ",\"ticketStatus\":\"in progress\"},{\"statusChangeDate\":\"2020-07-21T22:40:52.827-05:00\",\"statusChangeReason\":\"Ejecutando Flujo\""
			+ ",\"ticketStatus\":\"in progress\"},{\"statusChangeDate\":\"2020-07-21T22:40:52.838-05:00\",\"statusChangeReason\":\"Ejecutando Flujo\""
			+ ",\"ticketStatus\":\"in progress\"},{\"statusChangeDate\":\"2020-07-21T22:40:52.899-05:00\",\"statusChangeReason\":\"Ejecutando Flujo\""
			+ ",\"ticketStatus\":\"in progress\"},{\"statusChangeDate\":\"2020-07-21T22:40:52.923-05:00\",\"statusChangeReason\":\"Ejecutando Flujo\""
			+ ",\"ticketStatus\":\"in progress\"},{\"statusChangeDate\":\"2020-07-21T22:40:48.913-05:00\",\"statusChangeReason\":\"Ticket recibido\""
			+ ",\"ticketStatus\":\"acknowledged\"},{\"statusChangeDate\":\"2020-07-21T22:41:33.875-05:00\",\"statusChangeReason\":\"Ticket generado\""
			+ ",\"ticketStatus\":\"acknowledged\"}],\"attachment\":[{\"attachmentId\":\"382919\",\"creationDate\":\"2020-07-21T22:40:50.035-05:00\","
			+ "\"name\":\"ValidacionesInicialesInternet[{}]recupera-identificacion-cliente-atis\",\"additionalData\":[{\"key\":\"fecha-envio\",\"value\""
			+ ":\"2020-07-21T22:40:49.608-05:00\"},{\"key\":\"fecha-respuesta\",\"value\":\"2020-07-21T22:40:50.007-05:00\"},{\"key\":\"flujo\",\"value\""
			+ ":\"FCRSM-FIJA-NO-NAVEGA\"},{\"key\":\"canal\",\"value\":\"APP\"},{\"key\":\"tipo\",\"value\":\"interno\"},{\"key\":\"estado\",\"value\":\"OK\"}"
			+ ",{\"key\":\"codigo-respuesta\",\"value\":\"00000000\"},{\"key\":\"descripcion-respuesta\",\"value\":\"Servicio ejecutado\"},{\"key\":"
			+ "\"nombre-identificacion\",\"value\":\"\"},{\"key\":\"codigo-contacto\",\"value\":\"\"},{\"key\":\"numero-identificacion\",\"value\":\"\"},"
			+ "{\"key\":\"codigo-identificacion\",\"value\":\"\"},{\"key\":\"tipo-identificacion\",\"value\":\"\"}]},{\"attachmentId\":\"382918\","
			+ "\"creationDate\":\"2020-07-21T22:40:49.624-05:00\",\"name\":\"ValidacionesInicialesInternet[{}]recupera-estado-telefono\",\"additionalData\""
			+ ":[{\"key\":\"fecha-envio\",\"value\":\"2020-07-21T22:40:49.415-05:00\"},{\"key\":\"fecha-respuesta\",\"value\":\"2020-07-21T22:40:49.582-05:00\"}"
			+ ",{\"key\":\"flujo\",\"value\":\"FCRSM-FIJA-NO-NAVEGA\"},{\"key\":\"canal\",\"value\":\"APP\"},{\"key\":\"tipo\",\"value\":\"interno\"},{\"key\":"
			+ "\"estado\",\"value\":\"OK\"},{\"key\":\"codigo-respuesta\",\"value\":\"00000000\"},{\"key\":\"descripcion-respuesta\",\"value\":"
			+ "\"Servicio ejecutado\"},{\"key\":\"public-id\",\"value\":\"12799238\"},{\"key\":\"sistema-origen\",\"value\":\"1\"},{\"key\":\"customer-id\","
			+ "\"value\":\"5377691\"},{\"key\":\"account-id-cms\",\"value\":\"6308130\"},{\"key\":\"customer-id-atis\",\"value\":\"847032241\"},{\"key\":"
			+ "\"codigo-servicio-cms\",\"value\":\"6286010\"},{\"key\":\"estado-migracion\",\"value\":\"2\"},{\"key\":\"account-id-atis\",\"value\":\"483632104\"}]}"
			+ ",{\"attachmentId\":\"382921\",\"creationDate\":\"2020-07-21T22:40:50.681-05:00\",\"name\":\"ValidacionesInicialesInternet[{}]valida-RED\""
			+ ",\"additionalData\":[{\"key\":\"fecha-envio\",\"value\":\"2020-07-21T22:40:50.534-05:00\"},{\"key\":\"fecha-respuesta\",\"value\":"
			+ "\"2020-07-21T22:40:52.004-05:00\"},{\"key\":\"flujo\",\"value\":\"FCRSM-FIJA-NO-NAVEGA\"},{\"key\":\"canal\",\"value\":\"APP\"},{\"key\":\"tipo\""
			+ ",\"value\":\"interno\"},{\"key\":\"estado\",\"value\":\"OK\"},{\"key\":\"codigo-respuesta\",\"value\":\"00000000\"},{\"key\":\"descripcion-respuesta\""
			+ ",\"value\":\"Servicio ejecutado\"},{\"key\":\"recupera-info-previa\",\"value\":\"SI\"},{\"key\":\"estado-actual\",\"value\":\"activo\"}]},"
			+ "{\"attachmentId\":\"382920\",\"creationDate\":\"2020-07-21T22:40:50.549-05:00\",\"name\":\"ValidacionesInicialesInternet[{}]recupera-info-telefono\""
			+ ",\"additionalData\":[{\"key\":\"fecha-envio\",\"value\":\"2020-07-21T22:40:50.022-05:00\"},{\"key\":\"fecha-respuesta\",\"value\":\"2020-07-21T22:40:50.510-05:00\"}"
			+ ",{\"key\":\"flujo\",\"value\":\"FCRSM-FIJA-NO-NAVEGA\"},{\"key\":\"canal\",\"value\":\"APP\"},{\"key\":\"tipo\",\"value\":\"interno\"},"
			+ "{\"key\":\"estado\",\"value\":\"OK\"},{\"key\":\"codigo-respuesta\",\"value\":\"00000000\"},{\"key\":\"descripcion-respuesta\",\"value\""
			+ ":\"Servicio ejecutado\"},{\"key\":\"estado-linea\",\"value\":\"active\"},{\"key\":\"tecnologia-linea\",\"value\":\"hfc\"},{\"key\":\"linea-voip\""
			+ ",\"value\":\"\"}]},{\"attachmentId\":\"382918\",\"creationDate\":\"2020-07-21T22:40:49.624-05:00\",\"name\":\"Reset\",\"additionalData\":[{\"key\":\"estado-reset-modem-ok\",\"value\":\"2020-07-21T22:40:49.415-05:00\"},{\"key\":\"fecha-respuesta\",\"value\":\"2020-07-21T22:40:49.582-05:00\"},{\"key\":\"flujo\",\"value\":\"FCRSM-FIJA-NO-NAVEGA\"},{\"key\":\"canal\",\"value\":\"APP\"},{\"key\":\"tipo\",\"value\":\"interno\"},{\"key\":\"estado\",\"value\":\"OK\"},{\"key\":\"codigo-respuesta\",\"value\":\"00000000\"},{\"key\":\"descripcion-respuesta\",\"value\":\"Servicio ejecutado\"},{\"key\":\"public-id\",\"value\":\"12799238\"},{\"key\":\"sistema-origen\",\"value\":\"1\"},{\"key\":\"customer-id\",\"value\":\"5377691\"},{\"key\":\"account-id-cms\",\"value\":\"6308130\"},{\"key\":\"customer-id-atis\",\"value\":\"847032241\"},{\"key\":\"codigo-servicio-cms\",\"value\":\"6286010\"},{\"key\":\"estado-migracion\",\"value\":\"2\"},{\"key\":\"account-id-atis\",\"value\":\"483632104\"}]},{\"attachmentId\":\"382925\",\"creationDate\":\"2020-07-21T22:40:52.870-05:00\",\"name\":\"ValidaMasiva[{}]recupera-masiva-dmpe\""
			+ ",\"additionalData\":[{\"key\":\"fecha-envio\",\"value\":\"2020-07-21T22:40:52.410-05:00\"},{\"key\":\"fecha-respuesta\",\"value\":\"2020-07-21T22:40:52.851-05:00\"}"
			+ ",{\"key\":\"flujo\",\"value\":\"FCRSM-FIJA-NO-NAVEGA\"},{\"key\":\"canal\",\"value\":\"APP\"},{\"key\":\"tipo\",\"value\":\"interno\"},{\"key\":"
			+ "\"estado\",\"value\":\"OK\"},{\"key\":\"codigo-respuesta\",\"value\":\"00000000\"},{\"key\":\"descripcion-respuesta\",\"value\":\"Servicio ejecutado\"}"
			+ ",{\"key\":\"tiene-masiva-dmpe\",\"value\":\"NO\"},{\"key\":\"descripcion-masiva-dmpe\",\"value\":\"\"}]},{\"attachmentId\":\"382926\",\"creationDate\""
			+ ":\"2020-07-21T22:40:53.085-05:00\",\"name\":\"RecomendacionesDespachoHFC[{}]realiza-sincronizacion-iwy2\",\"additionalData\":[{\"key\":\"fecha-envio\""
			+ ",\"value\":\"2020-07-21T22:40:52.933-05:00\"},{\"key\":\"fecha-respuesta\",\"value\":\"2020-07-21T22:40:53.062-05:00\"},{\"key\":\"flujo\",\"value\""
			+ ":\"FCRSM-FIJA-NO-NAVEGA\"},{\"key\":\"canal\",\"value\":\"APP\"},{\"key\":\"tipo\",\"value\":\"interno\"},{\"key\":\"estado\",\"value\":\"Pendiente\"}"
			+ ",{\"key\":\"codigo-respuesta\",\"value\":\"00000000\"},{\"key\":\"descripcion-respuesta\",\"value\":\"Servicio ejecutado\"}]},{\"attachmentId\":\"382923\""
			+ ",\"creationDate\":\"2020-07-21T22:40:52.360-05:00\",\"name\":\"AveriaPendiente[{}]recupera-averia-pendiente-cms\",\"additionalData\":[{\"key\":\"fecha-envio\""
			+ ",\"value\":\"2020-07-21T22:40:52.059-05:00\"},{\"key\":\"fecha-respuesta\",\"value\":\"2020-07-21T22:40:52.303-05:00\"},{\"key\":\"flujo\",\"value\""
			+ ":\"FCRSM-FIJA-NO-NAVEGA\"},{\"key\":\"canal\",\"value\":\"APP\"},{\"key\":\"tipo\",\"value\":\"interno\"},{\"key\":\"estado\",\"value\":\"OK\"},{\"key\""
			+ ":\"codigo-respuesta\",\"value\":\"00000000\"},{\"key\":\"descripcion-respuesta\",\"value\":\"Servicio ejecutado\"},{\"key\":\"fecha-creacion-masiva-averia\""
			+ ",\"value\":\"\"},{\"key\":\"codigo-averia\",\"value\":\"\"},{\"key\":\"tipo-averia\",\"value\":\"\"},{\"key\":\"codigo-masiva-averia\",\"value\":\"\"}"
			+ ",{\"key\":\"fecha-creacion-averia\",\"value\":\"\"}]}],\"additionalData\":[{\"key\":\"sub-operation-code\",\"value\":\"99\"},{\"key\":\"phone\",\"value\""
			+ ":\"6286010\"},{\"key\":\"use-case-id\",\"value\":\"20000032\"},{\"key\":\"notification-id\",\"value\":\"20000032-001\"}]}}}";

	@BeforeAll
	public static void setup() {
		LocalDateTime actualDate = LocalDateTime.now(ZoneOffset.of("-05:00"));

		TblCustomerPK customerPk = new TblCustomerPK();
		customerPk.setDocumentNumber("70981983");
		customerPk.setDocumentType("DNI");
		customerPk.setServiceCode("");

		TblCustomer tblCustomer = new TblCustomer();
		tblCustomer.setId(customerPk);

		TblTicket tblTicket = new TblTicket();
		tblTicket.setCreationDate(actualDate);
		tblTicket.setCreationDateTicket(actualDate);
		tblTicket.setDescription("Ticket generado");
		tblTicket.setEventTimeKafka(actualDate);
		tblTicket.setIdTicket(1);
		tblTicket.setIdTicketTriage(19406743);
		tblTicket.setIdUseCase("99");
		tblTicket.setInvolvement("");
		tblTicket.setModifiedDateTicket(actualDate);
		tblTicket.setPriority(1);
		tblTicket.setProductIdentifier("serviceCode");
		tblTicket.setSeverity("minor");
		tblTicket.setStatus("in-progress");
		tblTicket.setStatusChangeDate(actualDate);
		tblTicket.setStatusChangeReason("");
		tblTicket.setStatusTicket("WA_SOLVED");
		tblTicket.setTblCustomer(tblCustomer);

		listTickets.add(tblTicket);

		// Equivalence
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

		// Quinto Objeto
		tblEquivalence = new TblEquivalence();
		tblEquivalence.setIdEquivalence(6);
		tblEquivalence.setNameEquivalence("Reset");
		tblEquivalence.setAttachmentName("Reset");
		lstTblEquivalence.add(tblEquivalence);

		// Equivalence Notification
		tblEquivalenceNotification = new TblEquivalenceNotification();
		tblEquivalenceNotification.setIdEquivalenceNotification(1);
		tblEquivalenceNotification.setCode("20000032-001");
		tblEquivalenceNotification.setDescription("No se pudo consultar al cliente");
		tblEquivalenceNotification.setTitle("Encontramos una incidencia que no podemos solucionar en linea");
		tblEquivalenceNotification.setBody("Un técnico especializado te atenderá a través de Whatsapp");
		tblEquivalenceNotification.setFooter("");
		tblEquivalenceNotification.setIcon("error");
		tblEquivalenceNotification.setButton("Ir a whastapp");
		tblEquivalenceNotification.setImage("whastapp");
		tblEquivalenceNotification.setActionbutton(
				"https://api.whatsapp.com/send?phone=51999955555&text=%C2%A1Hola!%20Me%20derivaron%20de%20la%20app%20para%20que%20me%20ayuden%20a%20resolver%20la%20avería%20de%20internet%20en%20mi%20hogar.%20Mi%20código%20de%20atención%20es%20FCR101");

	}

	@Test
	void kafkaWhatsAppMessage() {
		tblEquivalenceNotification.setAction(TicketStatus.WHATSAPP.name());
		saveKafkaMessage();
	}

	@Test
	void kafkaFaultTrazaMessage() {
		tblEquivalenceNotification.setAction(TicketStatus.FAULT_TRAZA.name());
		saveKafkaMessage();
	}

	@Test
	void kafkaGenericMessage() {
		tblEquivalenceNotification.setAction(TicketStatus.GENERIC.name());
		saveKafkaMessage();
	}

	@Test
	void kafkaResetMessage() {
		tblEquivalenceNotification.setAction(TicketStatus.RESET_SOLVED.name());
		saveKafkaMessage();
	}

	@Test
	void kafkaFaultMessage() {
		tblEquivalenceNotification.setAction(TicketStatus.FAULT.name());
		saveKafkaMessage();
	}

	private void saveKafkaMessage() {
		when(ticketRepository.findByIdTicketTriage(anyInt())).thenReturn(Optional.of(listTickets));

		when(ticketRepository.saveAndFlush(any(TblTicket.class))).thenAnswer(new Answer<TblTicket>() {
			@Override
			public TblTicket answer(InvocationOnMock invocation) throws Throwable {
				TblTicket tblTicket = invocation.getArgument(0);
				tblTicket.setIdTicket(2);
				return tblTicket;
			}
		});

		when(attachmentRepository.saveAndFlush(any(TblAttachment.class))).thenAnswer(new Answer<TblAttachment>() {
			@Override
			public TblAttachment answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArgument(0);
			}
		});

		doNothing().when(entityManager).flush();
		doNothing().when(entityManager).clear();
		doNothing().when(entityManager).persist(any());

		when(additionalDataRepository.saveAndFlush(any(TblAdditionalData.class)))
				.thenAnswer(new Answer<TblAdditionalData>() {
					@Override
					public TblAdditionalData answer(InvocationOnMock invocation) throws Throwable {
						return invocation.getArgument(0);
					}
				});

		when(equivalenceRepository.getEquivalence(anyInt())).thenReturn(Optional.of(lstTblEquivalence));

		when(equivalenceNotificationRepository.getEquivalence(anyString()))
				.thenReturn(Optional.of(tblEquivalenceNotification));

		when(ticketRepository.save(any(TblTicket.class))).thenReturn(new TblTicket());

		doNothing().when(functionsUtil).saveLogData(any(LogData.class));

		kafkaController.listen(message);
	}

}
