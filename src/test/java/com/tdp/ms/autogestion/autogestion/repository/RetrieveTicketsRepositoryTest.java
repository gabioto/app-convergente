package com.tdp.ms.autogestion.autogestion.repository;

import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;

@ExtendWith(MockitoExtension.class)
public class RetrieveTicketsRepositoryTest {

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

	private static List<TblTicket> lstTblTicket = new ArrayList<TblTicket>();

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

		lstTblTicket.add(tblTicket);
	}

	@Test
	void findCustomerAndUseCase() {

		when(jpaTicketRepository.findByCustomerAndUseCase(anyString(), anyString(), anyString(), anyString(),
				any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(lstTblTicket);

		List<Ticket> listTickets = ticketRepository.findByCustomerAndUseCase("DNI", "70707070", "", "",
				LocalDateTime.now(), LocalDateTime.now());

		assertEquals(1, listTickets.size());
		assertEquals(19406743, listTickets.get(0).getIdTriage());
	}

	@Test
	void findByCustomerAndUseCasePast() {

		when(jpaTicketRepository.findByCustomerAndUseCasePast(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(lstTblTicket);

		List<Ticket> listTickets = ticketRepository.findByCustomerAndUseCasePast("DNI", "70707070", "", "");

		assertEquals(1, listTickets.size());
		assertEquals(19406743, listTickets.get(0).getIdTriage());
	}
}
