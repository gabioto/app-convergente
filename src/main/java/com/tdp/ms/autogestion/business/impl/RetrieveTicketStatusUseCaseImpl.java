package com.tdp.ms.autogestion.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.business.RetrieveTicketStatusUseCase;
import com.tdp.ms.autogestion.exception.DomainException;
import com.tdp.ms.autogestion.exception.ResourceNotFoundException;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.TicketRepository;

/**
 * Class: TrazabilidadpruebaServiceImpl. <br/>
 * <b>Copyright</b>: &copy; 2019 Telef&oacute;nica del Per&uacute;<br/>
 * <b>Company</b>: Telef&oacute;nica del Per&uacute;<br/>
 *
 * @author Telef&oacute;nica del Per&uacute; (TDP) <br/>
 *         <u>Service Provider</u>: Everis Per&uacute; SAC (EVE) <br/>
 *         <u>Developed by</u>: <br/>
 *         <ul>
 *         <li>Developer name</li>
 *         </ul>
 *         <u>Changes</u>:<br/>
 *         <ul>
 *         <li>YYYY-MM-DD Creaci&oacute;n del proyecto.</li>
 *         </ul>
 * @version 1.0
 */
@Service
public class RetrieveTicketStatusUseCaseImpl implements RetrieveTicketStatusUseCase {

	@Autowired
	TicketRepository ticketRepository;

	@Override
	public ResponseEntity<TicketStatusResponse> retrieveTicketStatus(String idTicket) {

		try {
			List<Ticket> tickets = ticketRepository.getTicketStatus(Integer.parseInt(idTicket));
			Ticket ticket = tickets.get(tickets.size() == 1 ? 0 : 1);

			return new ResponseEntity<>(TicketStatusResponse.from(ticket, ticketRepository.getAdditionalData(ticket)),
					HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (DomainException e) {
			throw e;
		} catch (Exception exception) {
			throw exception;
		}
	}

}