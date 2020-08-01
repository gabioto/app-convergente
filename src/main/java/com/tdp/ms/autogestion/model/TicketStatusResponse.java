package com.tdp.ms.autogestion.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.tdp.ms.autogestion.util.Constants;

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

	private LocalDateTime statusChangeDate;
	
	private String ticketId;
	
	private String ticketStatus;
	
	private List<ClienteData> clienteData;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class ClienteData {
		@NotEmpty(message = Constants.MSG_NOT_EMPTY)
		private String key;

		@NotEmpty(message = Constants.MSG_NOT_EMPTY)
		private String value;
	}
}