package com.tdp.ms.autogestion.repository.datasource.api.entities;

import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.util.DateUtil;

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
public class TicketApiResponse {

	private String id;

	private String href;

	private String description;

	private String creationDate;

	private String severity;

	private String type;

	private String status;

	private String statusChangeDate;

	private String statusChangeReason;

	private Integer priority;

	public Ticket fromThis(Ticket pTicket) {
		pTicket.setId(Integer.valueOf(id));
		pTicket.setHref(href);
		pTicket.setDescription(description);
		pTicket.setCreationDate(DateUtil.formatStringToLocalDateTime(creationDate));
		pTicket.setSeverity(severity);
		pTicket.setType(type);
		pTicket.setStatus(status);
		pTicket.setStatusChangeDate(DateUtil.formatStringToLocalDateTime(statusChangeDate));
		pTicket.setStatusChangeReason(statusChangeReason);
		return pTicket;
	}
}
