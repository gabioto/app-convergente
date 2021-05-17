package com.tdp.ms.autogestion.business.impl;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.business.UpdateTicketStatusUseCase;
import com.tdp.ms.autogestion.exception.DomainException;
import com.tdp.ms.autogestion.exception.ErrorCategory;
import com.tdp.ms.autogestion.exception.GenericDomainException;
import com.tdp.ms.autogestion.exception.ResourceNotFoundException;
import com.tdp.ms.autogestion.exception.ValidRequestException;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse;
import com.tdp.ms.autogestion.model.LogData;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.model.TicketStatus;
import com.tdp.ms.autogestion.repository.TicketRepository;
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
public class UpdateTicketStatusUseCaseImpl implements UpdateTicketStatusUseCase {

	private static final Log log = LogFactory.getLog(UpdateTicketStatusUseCaseImpl.class);
	
	@Autowired
	TicketRepository ticketRepository;

	@Autowired
	FunctionsUtil functionsUtil;
	
	@Override
	public ResponseEntity<TicketStatusResponse> updateTicketStatus(int idTicket, String status)
			throws GenericDomainException {

		try {
			if (idTicket != 0) {
				for (TicketStatus elemento : TicketStatus.values()) {
					if (elemento.name().equals(status)) {
						Ticket ticket = ticketRepository.updateTicketStatus(idTicket, status);

						ResponseEntity<TicketStatusResponse> ticketStatusResponse = new ResponseEntity<>(
								TicketStatusResponse.from(ticket, new ArrayList<>()), HttpStatus.OK);

						functionsUtil.saveLogData(new LogData(idTicket, ticket.getCustomer().getNationalType(),
								ticket.getCustomer().getNationalId(), "Update Ticket", "updateTicketStatus",
								String.valueOf(idTicket).concat("|").concat(status), ticketStatusResponse.toString(),
								"Update Ticket"));

						return ticketStatusResponse;
					}
				}
				throw new ValidRequestException(ErrorCategory.MISSING_MANDATORY, "invalid status param");
			} else {
				throw new ValidRequestException(ErrorCategory.MISSING_MANDATORY, "idTicket is empty or null");
			}
		} catch (ResourceNotFoundException e) {
			log.error(this.getClass().getName() + " - Exception: " + e.getLocalizedMessage());
			
			throw e;
		} catch (DomainException e) {
			log.error(this.getClass().getName() + " - Exception: " + e.getLocalizedMessage());
			
			throw e;
		} catch (Exception e) {
			log.error(this.getClass().getName() + " - Exception: " + e.getLocalizedMessage());

			throw new GenericDomainException(ErrorCategory.UNEXPECTED, e.getLocalizedMessage());
		}
	}
}