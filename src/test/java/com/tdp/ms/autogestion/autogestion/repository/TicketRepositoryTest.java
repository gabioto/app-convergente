package com.tdp.ms.autogestion.autogestion.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdp.ms.autogestion.model.AdditionalData;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.TicketRepositoryImpl;
import com.tdp.ms.autogestion.repository.datasource.api.TicketApi;
import com.tdp.ms.autogestion.repository.datasource.db.JpaAttachmentAdditionalDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaCustomerRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceNotificationRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaTicketRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAdditionalData;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachment;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomer;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomerPK;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;

@ExtendWith(MockitoExtension.class)
public class TicketRepositoryTest {

	@InjectMocks
	TicketRepositoryImpl ticketRepository;
	
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
		ticket.setTblAdditionalData(new ArrayList<TblAdditionalData>());
		ticket.setTblAttachments(new ArrayList<TblAttachment>());
		
		List<TblTicket> lstTicket = new ArrayList<TblTicket>();
		lstTicket.add(ticket);
		
		optLstTicket = Optional.of(lstTicket);
	}
	
	@Test
	void ticketRepository_getTicket() throws Exception {
		when(ticketRepository.getTicket(anyInt())).thenReturn(optLstTicket.get().get(0).fromThis());
		
		Ticket ticket = ticketRepository.getTicket(19406791);
		
		assertNotNull(ticket);		
	}
}
