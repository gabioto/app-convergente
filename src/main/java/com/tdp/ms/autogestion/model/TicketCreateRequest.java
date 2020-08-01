package com.tdp.ms.autogestion.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.tdp.ms.autogestion.util.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TicketCreateRequest {

	private String description = "averia";

	private String severity = "minor";

	private String type = "TroubleTicket";

	private int priority = 1;

	private Channel channel;

	@NotNull(message = Constants.MSG_NOT_EMPTY)
	private RelatedObject relatedObject;

	@NotNull(message = Constants.MSG_NOT_EMPTY)
	private List<AdditionalData> additionalData;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class AdditionalData {
		@NotEmpty(message = Constants.MSG_NOT_EMPTY)
		private String key;

		@NotEmpty(message = Constants.MSG_NOT_EMPTY)
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class RelatedObject {
		@NotEmpty(message = Constants.MSG_NOT_EMPTY)
		private String involvement;

		@NotEmpty(message = Constants.MSG_NOT_EMPTY)
		private String reference;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class Channel {
		@NotEmpty(message = Constants.MSG_NOT_EMPTY)
		private String name;

		@NotEmpty(message = Constants.MSG_NOT_EMPTY)
		private String id;
	}

	public Ticket fromThis() {
		Ticket ticket = new Ticket();
		ticket.setDescription(description);
		ticket.setSeverity(severity);
		ticket.setType(type);
		ticket.setPriority(priority);
		ticket.setInvolvement(relatedObject.getInvolvement());
		ticket.setChannelId(channel != null ? channel.getId() : null);
		return ticket;
	}
}
