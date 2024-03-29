package com.tdp.ms.autogestion.expose.entities;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.util.AdditionalValid;
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

	@NotEmpty(message = Constants.MSG_NOT_EMPTY)
	private String description;

	@NotEmpty(message = Constants.MSG_NOT_EMPTY)
	private String severity;

	@NotEmpty(message = Constants.MSG_NOT_EMPTY)
	private String type;

	@Min(value = 1, message = Constants.MSG_NOT_EMPTY)
	private int priority;

	@NotNull(message = Constants.MSG_NOT_EMPTY)
	private Channel channel;

	@NotNull(message = Constants.MSG_NOT_EMPTY)
	private RelatedObject relatedObject;

	@NotNull
	@AdditionalValid
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
