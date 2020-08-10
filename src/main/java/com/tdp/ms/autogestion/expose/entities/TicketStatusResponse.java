package com.tdp.ms.autogestion.expose.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tdp.ms.autogestion.model.AdditionalData;
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
@JsonInclude(Include.NON_NULL)
public class TicketStatusResponse {

	private Integer ticketId;

	private String description;

	private LocalDateTime creationDate;

	private String type;

	private LocalDateTime statusChangeDate;

	private String ticketStatus;

	private LocalDateTime modifiedDateTicket;

	private List<ResponseAdditionalData> additionalData;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class ResponseAdditionalData {
		private String key;

		private String value;
	}

	public static TicketStatusResponse from(Ticket ticket, List<AdditionalData> addDataList) {
		TicketStatusResponse response = new TicketStatusResponse();
		response.setTicketId(ticket.getIdTriage());
		response.setDescription(ticket.getDescription());
		response.setCreationDate(ticket.getCreationDate());
		response.setType(ticket.getType());
		response.setStatusChangeDate(ticket.getStatusChangeDate());
		response.setTicketStatus(ticket.getTicketStatus());
		response.setModifiedDateTicket(ticket.getModifiedDateTicket());
		response.setAdditionalData(from(addDataList));
		return response;
	}

	private static List<ResponseAdditionalData> from(List<AdditionalData> additionalDataList) {
		List<ResponseAdditionalData> respAdditionalData = new ArrayList<>();
		for (AdditionalData additionalData : additionalDataList) {
			respAdditionalData.add(from(additionalData));
		}

		return respAdditionalData;
	}

	private static ResponseAdditionalData from(AdditionalData additionalData) {
		return new ResponseAdditionalData(additionalData.getKey(), additionalData.getValue());
	}
}