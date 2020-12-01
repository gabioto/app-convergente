package com.tdp.ms.autogestion.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

	private Integer id;

	private Integer idTriage;
	
	private String href;

	private String description;

	private LocalDateTime creationDate;

	private String severity;

	private String type;

	private String status;

	private LocalDateTime statusChangeDate;

	private String statusChangeReason;

	private Integer priority;

	private String technology;

	private String useCaseId;

	private String subOperationCode;
	
	private String productIdentifier;

	private String involvement;

	private String channelId;

	private String channelName;

	private Customer customer;
	
	private String ticketStatus;
	
	private LocalDateTime modifiedDateTicket;
	
	private LocalDateTime eventTimeKafka;
	
	private List<AdditionalData> additionalData = new ArrayList<>();

	private List<Attachment> attachments = new ArrayList<>();

}