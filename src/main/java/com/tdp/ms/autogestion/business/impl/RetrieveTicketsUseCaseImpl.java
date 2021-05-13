package com.tdp.ms.autogestion.business.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.business.RetrieveTicketsUseCase;
import com.tdp.ms.autogestion.exception.DomainException;
import com.tdp.ms.autogestion.exception.ErrorCategory;
import com.tdp.ms.autogestion.exception.ForbiddenException;
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
public class RetrieveTicketsUseCaseImpl implements RetrieveTicketsUseCase {

	private static final Log log = LogFactory.getLog(RetrieveTicketsUseCaseImpl.class);

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	FunctionsUtil functionsUtil;

	@Override
	public ResponseEntity<TicketStatusResponse> pendingTicket(String type, String involvement, String reference,
			String nationalIdType, String nationalId) throws GenericDomainException {

		LocalDate today = LocalDate.now(ZoneOffset.of(Constants.ZONE_OFFSET));

		try {
			// Validar si tiene tickets en el dia
			List<Ticket> tickets = ticketRepository.findByCustomerAndUseCase(nationalIdType, nationalId, reference,
					involvement, today.atStartOfDay(), today.atStartOfDay().plusDays(1));

			List<Integer> listIds = getListIds(tickets);

			if (!listIds.isEmpty()) {
				return evaluateTicketStatus(listIds);
			}

			// Validar si tiene tickets en dias anteriores
			tickets = ticketRepository.findByCustomerAndUseCasePast(nationalIdType, nationalId, reference, involvement);

			listIds = getListIds(tickets);

			if (!listIds.isEmpty()) {
				return evaluatePastTicketStatus(listIds);
			}
			return new ResponseEntity<>(new TicketStatusResponse(), HttpStatus.OK);
		} catch (DomainException e) {
			log.error(this.getClass().getName() + " - Exception: " + e.getLocalizedMessage());
			
			throw e;
		} catch (Exception e) {
			log.error(this.getClass().getName() + " - Exception: " + e.getLocalizedMessage());
			
			throw new GenericDomainException(ErrorCategory.UNEXPECTED, e.getLocalizedMessage());
		}
	}

	private List<Integer> getListIds(List<Ticket> tickets) {
		List<Integer> listIds = new ArrayList<>();
		
		if (!tickets.isEmpty()) {
			String idTicketTriage = "";
			int count = 0;
			for (Ticket ticket : tickets) {
				if (idTicketTriage.isEmpty()) {
					idTicketTriage = ticket.getIdTriage().toString();
					listIds.add(0, ticket.getId());					
				} else if (idTicketTriage.equals(ticket.getIdTriage().toString()) && count == 0) {						
					idTicketTriage = ticket.getIdTriage().toString();
					listIds.remove(0);
					listIds.add(0, ticket.getId());
					count++;
				} else if (!idTicketTriage.equals(ticket.getIdTriage().toString())) {
					listIds.add(1, ticket.getId());
					break;
				}
				if (ticket.getTicketStatus().equals(TicketStatus.WA_DEFAULT.name())
						|| ticket.getTicketStatus().equals(TicketStatus.WA_DEFAULT_SOLVED.name())) {
					if (count > 0) {
						listIds.remove(0);
						listIds.add(0, ticket.getId());						
					} else {
						count++;
					}
				}
			}
		}
		return listIds;
	}

	private ResponseEntity<TicketStatusResponse> evaluateTicketStatus(List<Integer> listIds) throws ParseException {
		Ticket ticket = ticketRepository.getTicket(listIds.get(0));
		
		int minutesTimeout = getHoursTimeout(ticket);
		if (minutesTimeout >= Constants.INT_HOURS_TIMEOUT && validateStatus(ticket)) {
			ticket = updateTicketStatus(ticket);
		}
		if (validateStatus(ticket)) {
			int minutes = 0;
			if (ticket.getInvolvement().equals(Constants.CABLE) && ticket.getTicketStatus().equals(TicketStatus.REFRESH.name())) {
				minutes = getMinutesRefresh(ticket);
				if (minutes >= Constants.INT_MINUTES) {
					ticket = ticketRepository.updateTicketStatus(ticket.getIdTriage(), TicketStatus.REFRESH_SOLVED.name());
					minutes = 0;
				}
			}			
			ResponseEntity<TicketStatusResponse> ticketStatusResponse = new ResponseEntity<>(
					TicketStatusResponse.from(ticket, ticketRepository.getAdditionalData(ticket, minutes)), HttpStatus.OK);

			saveLog(ticket, ticketStatusResponse.toString());

			return ticketStatusResponse;
		} else {
			// Si tiene un ticket puede crear otro, en caso contrario no
			if (listIds.size() == 1) {
				ResponseEntity<TicketStatusResponse> ticketStatusResponse = new ResponseEntity<>(
						new TicketStatusResponse(), HttpStatus.OK);

				saveLog(ticket, ticketStatusResponse.toString());

				return ticketStatusResponse;
			} else {
				saveLog(ticket, "User can´t create more tickets");

				throw new ForbiddenException("User can´t create more tickets");
			}
		}
	}

	private ResponseEntity<TicketStatusResponse> evaluatePastTicketStatus(List<Integer> listIds) throws ParseException {
		Ticket ticket = null;

		int minutes = 0;
		if (!listIds.isEmpty()) {
			ticket = ticketRepository.getTicket(listIds.get(0));
			
			int minutesTimeout = getHoursTimeout(ticket);
			if (minutesTimeout >= Constants.INT_HOURS_TIMEOUT && validateStatus(ticket)) {
				ticket = updateTicketStatus(ticket);
			}			
			if (ticket.getInvolvement().equals(Constants.CABLE) && ticket.getTicketStatus().equals(TicketStatus.REFRESH.name())) {
				minutes = getMinutesRefresh(ticket);
				if (minutes >= Constants.INT_MINUTES) {
					ticket = ticketRepository.updateTicketStatus(ticket.getId(), TicketStatus.REFRESH_SOLVED.name());
					minutes = 0;
				}
			}
		}

		// Cuando solo tiene un ticket
		if (ticket != null && validateStatus(ticket)) {
			return new ResponseEntity<>(TicketStatusResponse.from(ticket, ticketRepository.getAdditionalData(ticket, minutes)),
					HttpStatus.OK);
		} else {
			// Puede crear Ticket
			return new ResponseEntity<>(new TicketStatusResponse(), HttpStatus.OK);
		}
	}

	private boolean validateStatus(Ticket ticket) {
		return !ticket.getTicketStatus().equalsIgnoreCase(TicketStatus.SOLVED.name())
				&& !ticket.getTicketStatus().equalsIgnoreCase(TicketStatus.WA_DEFAULT_SOLVED.name())
				&& !ticket.getTicketStatus().equalsIgnoreCase(TicketStatus.WA_SOLVED.name())
				&& !ticket.getTicketStatus().equalsIgnoreCase(TicketStatus.FAULT_SOLVED.name())
				&& !ticket.getTicketStatus().equalsIgnoreCase(TicketStatus.GENERIC_SOLVED.name());
	}

	private Ticket updateTicketStatus(Ticket ticket) {
		if (ticket.getTicketStatus().equals(TicketStatus.WHATSAPP.name())) {
			ticket = ticketRepository.updateTicketStatusTimeout(ticket.getId(), TicketStatus.WA_SOLVED.name());					
		} else if (ticket.getTicketStatus().equals(TicketStatus.GENERIC.name())) {
			ticket = ticketRepository.updateTicketStatusTimeout(ticket.getId(), TicketStatus.GENERIC_SOLVED.name());					
		} else if (ticket.getTicketStatus().equals(TicketStatus.FAULT.name())) {
			ticket = ticketRepository.updateTicketStatusTimeout(ticket.getId(), TicketStatus.FAULT_SOLVED.name());					
		} else if (ticket.getTicketStatus().equals(TicketStatus.RESET_SOLVED.name())) {
			ticket = ticketRepository.updateTicketStatusTimeout(ticket.getId(), TicketStatus.SOLVED.name());					
		} else if (ticket.getTicketStatus().equals(TicketStatus.WA_DEFAULT.name())) {
			ticket = ticketRepository.updateTicketStatusTimeout(ticket.getId(), TicketStatus.WA_DEFAULT_SOLVED.name());					
		} else if (ticket.getTicketStatus().equals(TicketStatus.REFRESH.name())) {
			ticket = ticketRepository.updateTicketStatusTimeout(ticket.getId(), TicketStatus.REFRESH_SOLVED.name());					
		}
		return ticket;
	}
	
	private void saveLog(Ticket ticket, String message) {
		functionsUtil.saveLogData(new LogData(ticket.getIdTriage(), ticket.getCustomer().getNationalId(),
				ticket.getCustomer().getNationalType(), "Retrieve Ticket", "retrieveTicket",
				ticket.getIdTriage().toString(), message, "Retrieve Ticket"));
	}
	
	private int getMinutesRefresh(Ticket ticket) throws ParseException {
		int minutes = 0;
		String dateReset = ticket.getModifiedDateTicket().toString().replace("T", " ");
		String dateActual = LocalDateTime.now(ZoneOffset.of(Constants.ZONE_OFFSET)).toString().replace("T", " " );
			
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
		Date d1 = format.parse(dateReset);
		Date d2 = format.parse(dateActual);
		long diff = d2.getTime() - d1.getTime();
		minutes = (int) TimeUnit.MILLISECONDS.toSeconds(diff);		
		return minutes; 
	}

	private int getHoursTimeout(Ticket ticket) throws ParseException {
		int hours = 0;
		String dateCreation = ticket.getCreationDate().toString().replace("T", " ");
		String dateActual = LocalDateTime.now(ZoneOffset.of(Constants.ZONE_OFFSET)).toString().replace("T", " " );
			
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
		Date d1 = format.parse(dateCreation);
		Date d2 = format.parse(dateActual);
		long diff = d2.getTime() - d1.getTime();
		hours = (int) TimeUnit.MILLISECONDS.toHours(diff);		
		return hours; 
	}
}