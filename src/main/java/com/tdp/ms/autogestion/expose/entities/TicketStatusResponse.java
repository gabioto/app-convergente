package com.tdp.ms.autogestion.expose.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.tdp.ms.autogestion.model.Ticket;

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
public class TicketStatusResponse {

	private Integer ticketId;

	private String description;

	private LocalDateTime creationDate;

	private String type;

	private LocalDateTime statusChangeDate;

	private String ticketStatus;

	private LocalDateTime modifiedDateTicket;

	private List<AdditionalData> additionalData;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class AdditionalData {
		private String key;

		private String value;
	}

	public static TicketStatusResponse from(Ticket ticket, int idTicket) {
		TicketStatusResponse response = new TicketStatusResponse();
		response.setDescription(ticket.getDescription());
		response.setTicketId(idTicket);
		response.setTicketStatus(ticket.getTicketStatus());
		response.setCreationDate(ticket.getCreationDate());
		response.setType(ticket.getType());
		response.setModifiedDateTicket(ticket.getModifiedDateTicket());
		return response;
	}
}