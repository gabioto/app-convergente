package com.tdp.ms.autogestion.repository.datasource.api.entities;

import java.util.ArrayList;
import java.util.List;

import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.model.TicketAdditionalData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TicketApiRequest {

	private String description;

	private String severity;

	private String type;

	private Integer priority;

	private Channel channel;

	private List<TicketAdditionalData> additionalData = new ArrayList<>();

	public void generateRequest(Ticket pTicket) {
		description = pTicket.getDescription();
		severity = pTicket.getSeverity();
		type = pTicket.getType();
		priority = pTicket.getPriority();
		channel = new Channel(pTicket.getChannelId() != null ? pTicket.getChannelId() : "3");
		additionalData.add(new TicketAdditionalData("use-case-id", pTicket.getUseCaseId()));
		additionalData.add(new TicketAdditionalData("service-code", pTicket.getCustomer().getServiceCode()));
		additionalData.add(new TicketAdditionalData("sub-operation-code", pTicket.getSubOperationCode()));
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	private static class Channel {
		private String id;
	}

}