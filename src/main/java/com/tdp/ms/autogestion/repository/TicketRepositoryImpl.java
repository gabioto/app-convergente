package com.tdp.ms.autogestion.repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.model.TicketStatus;
import com.tdp.ms.autogestion.repository.datasource.api.TicketApi;
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

@Repository
public class TicketRepositoryImpl implements TicketRepository {

	private static final Log log = LogFactory.getLog(TicketRepositoryImpl.class);
	private static final String TAG = TicketRepositoryImpl.class.getCanonicalName();

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

	@Override
	public Ticket generateTicket(OAuth pOAuth, Ticket pTicket) {
		return ticketApi.generate(pOAuth, pTicket);
	}

	@Override
	public void saveGeneratedTicket(Ticket pTicket) {
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
		log.info(TAG + "createTicket: " + tableTicket.getIdTicket());
	}

	@Override
	public Ticket updateTicketStatus(int idTicket, String status) throws Exception {
		LocalDateTime sysDate = LocalDateTime.now(ZoneOffset.of(Constants.ZONE_OFFSET));
		Optional<List<TblTicket>> list = jpaTicketRepository.getTicketStatus(idTicket);
		if (list.isPresent()) {
			TblTicket tblTicket;
			if (list.get().size() == 1) {				
				tblTicket = list.get().get(0);
			} else {
				tblTicket = list.get().get(1);
			}			
			tblTicket.setStatusTicket(status);
			tblTicket.setModifiedDateTicket(sysDate);
			tblTicket.setEventTimeKafka(sysDate);
			tblTicket = jpaTicketRepository.save(tblTicket);

			return tblTicket.fromThis();
		} else {
			throw new Exception();
		}
	}

	@Override
	public TblTicket getTicket(int idTicket) {
		Optional<List<TblTicket>> list = jpaTicketRepository.getTicket(idTicket);
		if (list.isPresent()) {
			TblTicket tblTicket = list.get().get(0);
			
			return tblTicket;
		}
		return null;
	}

	@Override
	public List<TblTicket> findByCustomerAndUseCase(String docType, String docNumber, String reference,
			String involvement, LocalDateTime creationDate, LocalDateTime endDate) {
		return jpaTicketRepository.findByCustomerAndUseCase(docType, docNumber, reference, involvement, creationDate,
				endDate);
	}

	@Override
	public List<TblTicket> findByCustomerAndUseCasePast(String docType, String docNumber, String reference, String involvement) {
		return jpaTicketRepository.findByCustomerAndUseCasePast(docType, docNumber, reference, involvement);
	}
	
	@Override
	public List<TblEquivalence> getEquivalence(int idTicket) {
		Optional<List<TblEquivalence>> list = jpaEquivalenceRepository.getEquivalence(idTicket);		
		return list.get();
	}

	@Override
	public TblEquivalenceNotification getEquivalenceNotification(String code) {
		Optional<TblEquivalenceNotification> tblEquivalenceNotification = jpaEquivalenceNotificationRepository
				.getEquivalence(code);
		return tblEquivalenceNotification.get();
	}

}