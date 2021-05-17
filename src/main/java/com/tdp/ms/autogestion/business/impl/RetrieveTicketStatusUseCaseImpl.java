package com.tdp.ms.autogestion.business.impl;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.business.RetrieveTicketStatusUseCase;
import com.tdp.ms.autogestion.exception.DomainException;
import com.tdp.ms.autogestion.exception.ErrorCategory;
import com.tdp.ms.autogestion.exception.GenericDomainException;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse;
import com.tdp.ms.autogestion.model.LogData;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.model.TicketStatus;
import com.tdp.ms.autogestion.repository.TicketRepository;
import com.tdp.ms.autogestion.util.Constants;
import com.tdp.ms.autogestion.util.FunctionsUtil;

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

	private static final Log log = LogFactory.getLog(RetrieveTicketStatusUseCaseImpl.class);
	
	@Autowired
	TicketRepository ticketRepository;

	@Autowired
	FunctionsUtil functionsUtil;

	@Override
	public ResponseEntity<TicketStatusResponse> retrieveTicketStatus(int idTicket) throws GenericDomainException {
		try {
			List<Ticket> tickets = validateRefresh(idTicket);
			Ticket ticket = tickets.get(tickets.size() == 1 ? 0 : 1);

			ResponseEntity<TicketStatusResponse> ticketStatusResponse = new ResponseEntity<>(
					TicketStatusResponse.from(ticket, ticketRepository.getAdditionalData(ticket, 0)), HttpStatus.OK);

			functionsUtil.saveLogData(new LogData(idTicket, ticket.getCustomer().getNationalId(),
					ticket.getCustomer().getNationalType(), "Retrieve Ticket Status", "retrieveTicketStatus",
					String.valueOf(idTicket), ticketStatusResponse.toString(), "Retrieve Ticket Status"));

			return ticketStatusResponse;
		} catch (DomainException e) {
			log.error(this.getClass().getName() + " - Exception: " + e.getLocalizedMessage());
			
			throw e;
		} catch (Exception e) {
			log.error(this.getClass().getName() + " - Exception: " + e.getLocalizedMessage());
			
			throw new GenericDomainException(ErrorCategory.UNEXPECTED, e.getLocalizedMessage());
		}
	}

	private List<Ticket> validateRefresh(int idTicket) throws ParseException {
		List<Ticket> tickets = ticketRepository.getTicketStatus(idTicket);
		if (tickets.size() == 1) {
			// Si ticket es cableTv en estado refresh
			if (tickets.get(0).getInvolvement().equals(Constants.CABLE)
					&& tickets.get(0).getTicketStatus().equals(TicketStatus.REFRESH.name())) {
				int minutes = functionsUtil.getMinutesRefresh(tickets.get(0));
				if (minutes >= Constants.INT_MINUTES) {
					tickets.add(0, ticketRepository.updateTicketStatus(tickets.get(0).getIdTriage(),
							TicketStatus.REFRESH_SOLVED.name()));
					minutes = 0;
				}
			}
		} else {
			// Si ticket es cableTv en estado refresh
			if (tickets.get(1).getInvolvement().equals(Constants.CABLE)
					&& tickets.get(1).getTicketStatus().equals(TicketStatus.REFRESH.name())) {
				int minutes = functionsUtil.getMinutesRefresh(tickets.get(1));
				if (minutes >= Constants.INT_MINUTES) {
					tickets.add(1, ticketRepository.updateTicketStatus(tickets.get(1).getIdTriage(),
							TicketStatus.REFRESH_SOLVED.name()));
					minutes = 0;
				}
			}
		}
		return tickets;
	}
}