package com.tdp.ms.autogestion.autogestion.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.tdp.ms.autogestion.exception.ResourceNotFoundException;
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
public class UpdateTicketRepositoryTest {

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

	private static List<TblTicket> listTickets = new ArrayList<TblTicket>();

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
	}

	@Test
	void updateTicket_success() {
		when(jpaTicketRepository.getTicketStatus(anyInt())).thenReturn(Optional.of(listTickets));

		when(jpaTicketRepository.save(any(TblTicket.class))).thenAnswer(new Answer<TblTicket>() {
			@Override
			public TblTicket answer(InvocationOnMock invocation) throws Throwable {
				return (TblTicket) invocation.getArguments()[0];
			}
		});

		Ticket ticket = ticketRepository.updateTicketStatus(1, "WA_SOLVED");

		assertNotNull(ticket);
	}

	@Test
	void updateTicket_resourceException() {
		when(jpaTicketRepository.getTicketStatus(anyInt())).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			ticketRepository.updateTicketStatus(1, "WA_SOLVED");
		});

		assertEquals(ex.getMessage(), "Resource 1 does not exist. Resource Identifier");
	}
}
