package com.tdp.ms.autogestion.business;

import org.springframework.http.ResponseEntity;

import com.tdp.ms.autogestion.model.TicketRetrieveRequest;
import com.tdp.ms.autogestion.model.TicketStatusResponse;

/**
 * Class: TrazabilidadpruebaService. <br/>
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
public interface RetrieveTicketsUseCase {

	ResponseEntity<TicketStatusResponse> pendingTicket(TicketRetrieveRequest request);

}
