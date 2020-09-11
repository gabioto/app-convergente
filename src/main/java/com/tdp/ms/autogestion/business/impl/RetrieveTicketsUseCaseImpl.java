package com.tdp.ms.autogestion.business.impl;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.business.RetrieveTicketsUseCase;
import com.tdp.ms.autogestion.exception.DomainException;
import com.tdp.ms.autogestion.exception.ForbiddenException;
import com.tdp.ms.autogestion.exception.ResourceNotFoundException;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.model.TicketStatus;
import com.tdp.ms.autogestion.repository.TicketRepository;
import com.tdp.ms.autogestion.util.Constants;

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

	@Override
	public ResponseEntity<TicketStatusResponse> pendingTicket(String type, String involvement, String reference,
			String nationalIdType, String nationalId) {

		LocalDate today = LocalDate.now(ZoneOffset.of(Constants.ZONE_OFFSET));

		try {
			// Validar si tiene tickets en el dia
			List<Ticket> tickets = ticketRepository.findByCustomerAndUseCase(nationalIdType, nationalId, reference,
					involvement, today.atStartOfDay(), today.atStartOfDay().plusDays(1));

			List<Integer> listIds = getListIds(tickets, true);

			if (listIds.size() > 0) {
				return evaluateTicketStatus(listIds);
			}

			// Validar si tiene tickets en dias anteriores
			tickets = ticketRepository.findByCustomerAndUseCasePast(nationalIdType, nationalId, reference, involvement);

			listIds = getListIds(tickets, false);

			if (listIds.size() > 0) {
				return evaluatePastTicketStatus(listIds);
			}

			return new ResponseEntity<>(new TicketStatusResponse(), HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (ForbiddenException e) {
			throw e;
		} catch (DomainException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	private List<Integer> getListIds(List<Ticket> tickets, boolean isToday) {
		List<Integer> listIds = new ArrayList<Integer>();

		if (tickets != null && tickets.size() > 0) {
			String idTicketTriage = "";
			int count = 0;
			for (Ticket ticket : tickets) {
				if (idTicketTriage.isEmpty()) {
					idTicketTriage = ticket.getIdTriage().toString();
					listIds.add(0, ticket.getId());

					log.info("1 - Id Ticket Triaje (Dia Actual): " + idTicketTriage);
				} else {
					if (!idTicketTriage.equals(ticket.getIdTriage().toString())) {
						idTicketTriage = ticket.getIdTriage().toString();
						listIds.add(1, ticket.getId());
						count = 2;

						log.info("2 - Id Ticket Triaje (Dia Actual): " + idTicketTriage);
					} else {
						if (count == 0) {
							idTicketTriage = ticket.getIdTriage().toString();
							listIds.remove(0);
							listIds.add(0, ticket.getId());
							count++;

							log.info("1 - Id Ticket Triaje Actualizado (Dia Actual): " + idTicketTriage);
						}
						if (isToday && count == 2) {
							idTicketTriage = ticket.getIdTriage().toString();
							listIds.remove(1);
							listIds.add(1, ticket.getId());
							count++;

							log.info("2 - Id Ticket Triaje Actualizado (Dia Actual): " + idTicketTriage);
						}
					}
				}
			}
		}

		return listIds;
	}

	private ResponseEntity<TicketStatusResponse> evaluateTicketStatus(List<Integer> listIds) {
		Ticket ticket = ticketRepository.getTicket(listIds.get(listIds.size() == 1 ? 0 : 1));

		if (validateStatus(ticket)) {
			return new ResponseEntity<>(TicketStatusResponse.from(ticket, ticketRepository.getAdditionalData(ticket)),
					HttpStatus.OK);
		} else {
			// Si tiene un ticket puede crear otro, en caso contrario no
			// TODO: Validar si devuelve una excepción
			if (listIds.size() == 1) {
				return new ResponseEntity<>(new TicketStatusResponse(), HttpStatus.OK);
			} else {
				throw new ForbiddenException("User can´t create more tickets");
			}
		}
	}

	private ResponseEntity<TicketStatusResponse> evaluatePastTicketStatus(List<Integer> listIds) {
		Ticket ticket = null;

		if (listIds.size() == 1) {
			ticket = ticketRepository.getTicket(listIds.get(0));
		}

		// Cuando solo tiene un ticket
		if (ticket != null && validateStatus(ticket)) {
			return new ResponseEntity<>(TicketStatusResponse.from(ticket, ticketRepository.getAdditionalData(ticket)),
					HttpStatus.OK);
		} else {
			// Puede crear Ticket
			return new ResponseEntity<>(new TicketStatusResponse(), HttpStatus.OK);
		}

	}

	private boolean validateStatus(Ticket ticket) {
		return !ticket.getTicketStatus().equalsIgnoreCase(TicketStatus.SOLVED.name())
				&& !ticket.getTicketStatus().equalsIgnoreCase(TicketStatus.WA_SOLVED.name())
				&& !ticket.getTicketStatus().equalsIgnoreCase(TicketStatus.FAULT_SOLVED.name())
				&& !ticket.getTicketStatus().equalsIgnoreCase(TicketStatus.GENERIC_SOLVED.name());
	}
}