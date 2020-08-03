package com.tdp.ms.autogestion.business.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.business.UpdateTicketStatusUseCase;
import com.tdp.ms.autogestion.model.TicketStatus;
import com.tdp.ms.autogestion.model.TicketStatusResponse;
import com.tdp.ms.autogestion.repository.datasource.api.TicketApi;
import com.tdp.ms.autogestion.repository.datasource.db.JpaAdditionalDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaCustomerRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceNotificationRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaEquivalenceRepository;
import com.tdp.ms.autogestion.repository.datasource.db.JpaTicketRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;
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
public class UpdateTicketStatusUseCaseImpl implements UpdateTicketStatusUseCase {

	private static final Log log = LogFactory.getLog(UpdateTicketStatusUseCaseImpl.class);
	private static final String TAG = UpdateTicketStatusUseCaseImpl.class.getCanonicalName();

	@Autowired
	TicketApi ticketApi;

	@Autowired
	JpaTicketRepository jpaTicketRepository;

	@Autowired
	JpaAdditionalDataRepository additionalDataRepository;

	@Autowired
	JpaEquivalenceRepository equivalenceRepository;

	@Autowired
	JpaEquivalenceNotificationRepository equivalenceNotificationRepository;

	@Autowired
	JpaCustomerRepository customerRepository;

	@Autowired
	FunctionsUtil functionsUtil;

	@Override
	public ResponseEntity<TicketStatusResponse> updateTicketStatus(int idTicket, String status) {
		TicketStatusResponse ticketStatusResponse = new TicketStatusResponse();

		if (idTicket != 0) {
			for (TicketStatus elemento : TicketStatus.values()) {
				if (elemento.name().equals(status)) {
					LocalDateTime sysDate = LocalDateTime.now(ZoneOffset.of(Constants.ZONE_OFFSET));
					Optional<List<TblTicket>> list = jpaTicketRepository.getTicketStatus(idTicket);

					if (list.isPresent()) {
						TblTicket ticket = list.get().get(0);
						ticket.setStatusTicket(status);
						ticket.setModifiedDateTicket(sysDate);
						jpaTicketRepository.save(ticket);
					}
					ticketStatusResponse.setTicketStatus(elemento.name());
					ticketStatusResponse.setStatusChangeDate(sysDate);
					ticketStatusResponse.setTicketId(idTicket);

					functionsUtil.saveLogData(idTicket, "", "", "", "", idTicket + status,
							String.valueOf(ticketStatusResponse), "updateTicketStatus");

					return new ResponseEntity<>(ticketStatusResponse, HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(ticketStatusResponse, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(ticketStatusResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}