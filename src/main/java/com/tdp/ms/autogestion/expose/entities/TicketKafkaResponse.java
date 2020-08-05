package com.tdp.ms.autogestion.expose.entities;

import java.util.ArrayList;
import java.util.List;

import com.tdp.ms.autogestion.model.TicketAdditionalData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TicketKafkaResponse {

	private String eventId;
	private String eventTime;
	private String eventType;
	private Event event;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class Event {

		private TroubleTicket troubleTicket;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		@EqualsAndHashCode(callSuper = false)
		public static class TroubleTicket {

			private String id;
			private String href;
			private String correlationId;
			private String subject;
			private String description;
			private String creationDate;
			private String severity;

			private int priority;
			private String type;
			private String status;
			private String statusChangeDate;
			private String statusChangeReason;
			private Channel channel;
			private List<StatusHistory> statusHistory = new ArrayList<>();
			private List<Attachment> attachment = new ArrayList<>();
			private List<TicketAdditionalData> additionalData = new ArrayList<>();

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			@EqualsAndHashCode(callSuper = false)
			public static class Channel {
				private String id;
				private String href;
				private String name;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			@EqualsAndHashCode(callSuper = false)
			public static class StatusHistory {
				private String statusChangeDate;
				private String statusChangeReason;
				private String ticketStatus;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			@EqualsAndHashCode(callSuper = false)
			public static class Attachment {
				private String attachmentId;
				private String creationDate;
				private String name;
				private List<AdditionalData> additionalData = new ArrayList<>();

				@Data
				@NoArgsConstructor
				@AllArgsConstructor
				@EqualsAndHashCode(callSuper = false)
				public static class AdditionalData {
					private String key;
					private String value;
				}
			}
		}

	}

}
