package com.tdp.ms.autogestion.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Class: TrazabilidadpruebaResponse. <br/>
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
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TicketCreateResponse {

	private String id;

	private String href;

	private String description;

	private String type;

	private String creationDate;

	private String ticketStatus;

	private List<TicketAdditionalData> additionalData = new ArrayList<>();

	public static TicketCreateResponse from(Ticket ticket) {
		TicketCreateResponse response = new TicketCreateResponse();
		response.setDescription(ticket.getDescription());
		response.setHref(ticket.getHref());
		response.setId(ticket.getId());
		response.setTicketStatus(TicketStatus.CREATED.name());
		response.setCreationDate(ticket.getCreationDate().toString());
		response.setType(ticket.getType());
		response.getAdditionalData().add(new TicketAdditionalData("status", ticket.getStatus()));
		return response;
	}
}
