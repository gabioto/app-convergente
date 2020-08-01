package com.tdp.ms.autogestion.repository;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.datasource.api.TicketApi;
import com.tdp.ms.autogestion.repository.datasource.db.JpaCustomerRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaTicketRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomer;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblCustomerPK;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;

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
		tableTicket = TblTicket.from(pTicket, tableCustomer);

		optCustomer = jpaCustomerRepository.findById(tableCustomerPk);

		if (!optCustomer.isPresent()) {
			jpaCustomerRepository.save(tableCustomer);
		}

		tableTicket = jpaTicketRepository.save(tableTicket);
		log.info(TAG + "createTicket: " + tableTicket.getIdTicket());
	}

	@Override
	public Ticket getTicketStatus(String idTicket) {
//		jpaTicketRepository.getTicketStatus(Integer.parseInt(idTicket));
		return null;
	}

}
