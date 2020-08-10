package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.util.Constants;

/**
 * The persistent class for the tbl_ticket database table. , schema =
 * "esqfcrautogestion"
 */
@Entity
@Table(name = "tbl_ticket")
@NamedQuery(name = "TblTicket.findAll", query = "SELECT t FROM TblTicket t")
public class TblTicket implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id_ticket")
	private Integer idTicket;

	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "creation_date")
	private LocalDateTime creationDate;

	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "creation_date_ticket")
	private LocalDateTime creationDateTicket;

	private String description;

	@Column(name = "id_ticket_triage")
	private Integer idTicketTriage;

	@Column(name = "id_use_case")
	private String idUseCase;

	private Integer priority;

	private String severity;

	private String status;

	private String involvement;

	private String technology;

	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "status_change_date")
	private LocalDateTime statusChangeDate;

	@Column(name = "status_change_reason")
	private String statusChangeReason;

	@Column(name = "status_ticket")
	private String statusTicket;

	@Column(name = "ticket_type")
	private String ticketType;

	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "modified_date_ticket")
	private LocalDateTime modifiedDateTicket;

	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "event_time_kafka")
	private LocalDateTime eventTimeKafka;

	// bi-directional many-to-one association to TblAdditionalData
	@OneToMany(mappedBy = "tblTicket", targetEntity = TblAdditionalData.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<TblAdditionalData> tblAdditionalData = new ArrayList<>();

	// bi-directional many-to-one association to TblAttachment
	@OneToMany(mappedBy = "tblTicket", targetEntity = TblAttachment.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<TblAttachment> tblAttachments = new ArrayList<>();

	// bi-directional many-to-one association to TblStatusHistory
	@OneToMany(mappedBy = "tblTicket")
	private List<TblStatusHistory> tblStatusHistories;

	// bi-directional many-to-one association to TblCustomer
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "client_document_number", referencedColumnName = "document_number"),
			@JoinColumn(name = "client_document_type", referencedColumnName = "document_type"),
			@JoinColumn(name = "client_service_code", referencedColumnName = "service_code") })
	private TblCustomer tblCustomer;

	public TblTicket() {
	}

	public LocalDateTime getEventTimeKafka() {
		return eventTimeKafka;
	}

	public void setEventTimeKafka(LocalDateTime eventTimeKafka) {
		this.eventTimeKafka = eventTimeKafka;
	}

	public LocalDateTime getModifiedDateTicket() {
		return modifiedDateTicket;
	}

	public void setModifiedDateTicket(LocalDateTime modifiedDateTicket) {
		this.modifiedDateTicket = modifiedDateTicket;
	}

	public String getInvolvement() {
		return involvement;
	}

	public void setInvolvement(String involvement) {
		this.involvement = involvement;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public Integer getIdTicket() {
		return this.idTicket;
	}

	public void setIdTicket(Integer idTicket) {
		this.idTicket = idTicket;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIdTicketTriage() {
		return this.idTicketTriage;
	}

	public void setIdTicketTriage(Integer idTicketTriage) {
		this.idTicketTriage = idTicketTriage;
	}

	public String getIdUseCase() {
		return this.idUseCase;
	}

	public void setIdUseCase(String idUseCase) {
		this.idUseCase = idUseCase;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getSeverity() {
		return this.severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusChangeReason() {
		return this.statusChangeReason;
	}

	public void setStatusChangeReason(String statusChangeReason) {
		this.statusChangeReason = statusChangeReason;
	}

	public String getStatusTicket() {
		return this.statusTicket;
	}

	public void setStatusTicket(String statusTicket) {
		this.statusTicket = statusTicket;
	}

	public String getTicketType() {
		return this.ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public List<TblAdditionalData> getTblAdditionalData() {
		return this.tblAdditionalData;
	}

	public void setTblAdditionalData(List<TblAdditionalData> tblAdditionalData) {
		this.tblAdditionalData = tblAdditionalData;
	}

	public TblAdditionalData addTblAdditionalData(TblAdditionalData tblAdditionalData) {
		getTblAdditionalData().add(tblAdditionalData);
		tblAdditionalData.setTblTicket(this);

		return tblAdditionalData;
	}

	public TblAdditionalData removeTblAdditionalData(TblAdditionalData tblAdditionalData) {
		getTblAdditionalData().remove(tblAdditionalData);
		tblAdditionalData.setTblTicket(null);

		return tblAdditionalData;
	}

	public List<TblAttachment> getTblAttachments() {
		return this.tblAttachments;
	}

	public void setTblAttachments(List<TblAttachment> tblAttachments) {
		this.tblAttachments = tblAttachments;
	}

	public TblAttachment addTblAttachment(TblAttachment tblAttachment) {
		getTblAttachments().add(tblAttachment);
		tblAttachment.setTblTicket(this);

		return tblAttachment;
	}

	public TblAttachment removeTblAttachment(TblAttachment tblAttachment) {
		getTblAttachments().remove(tblAttachment);
		tblAttachment.setTblTicket(null);

		return tblAttachment;
	}

	public List<TblStatusHistory> getTblStatusHistories() {
		return this.tblStatusHistories;
	}

	public void setTblStatusHistories(List<TblStatusHistory> tblStatusHistories) {
		this.tblStatusHistories = tblStatusHistories;
	}

	public TblStatusHistory addTblStatusHistory(TblStatusHistory tblStatusHistory) {
		getTblStatusHistories().add(tblStatusHistory);
		tblStatusHistory.setTblTicket(this);

		return tblStatusHistory;
	}

	public TblStatusHistory removeTblStatusHistory(TblStatusHistory tblStatusHistory) {
		getTblStatusHistories().remove(tblStatusHistory);
		tblStatusHistory.setTblTicket(null);

		return tblStatusHistory;
	}

	public TblCustomer getTblCustomer() {
		return this.tblCustomer;
	}

	public void setTblCustomer(TblCustomer tblCustomer) {
		this.tblCustomer = tblCustomer;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getCreationDateTicket() {
		return creationDateTicket;
	}

	public void setCreationDateTicket(LocalDateTime creationDateTicket) {
		this.creationDateTicket = creationDateTicket;
	}

	public LocalDateTime getStatusChangeDate() {
		return statusChangeDate;
	}

	public void setStatusChangeDate(LocalDateTime statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
	}

	public static TblTicket from(Ticket ticket, TblCustomer tableCustomer, String status) {
		TblTicket tableTicket = new TblTicket();
		tableTicket.setIdTicketTriage(Integer.valueOf(ticket.getId()));
		tableTicket.setDescription(ticket.getDescription());
		tableTicket.setCreationDate(ticket.getCreationDate());
		tableTicket.setCreationDateTicket(LocalDateTime.now(ZoneOffset.of(Constants.ZONE_OFFSET)));
		tableTicket.setSeverity(ticket.getSeverity());
		tableTicket.setTicketType(ticket.getType());
		tableTicket.setStatus(ticket.getStatus());
		tableTicket.setStatusChangeDate(ticket.getStatusChangeDate());
		tableTicket.setStatusChangeReason(ticket.getStatusChangeReason());
		tableTicket.setPriority(ticket.getPriority());
		tableTicket.setInvolvement(ticket.getInvolvement());
		tableTicket.setStatusTicket(status);
		tableTicket.setIdUseCase(ticket.getUseCaseId());
		tableTicket.setTechnology(ticket.getTechnology());
		tableTicket.setTblCustomer(tableCustomer);
		return tableTicket;
	}

	public Ticket fromThis() {
		return new Ticket(idTicket, idTicketTriage, "", description, creationDate, severity, ticketType, status,
				statusChangeDate, statusChangeReason, priority, technology, idUseCase, "", involvement, "", "",
				tblCustomer.fromThis(), statusTicket, modifiedDateTicket,
				TblAdditionalData.listFromThis(tblAdditionalData), TblAttachment.listFromThis(tblAttachments));
	}

	public static List<Ticket> listFromThis(List<TblTicket> tblTickets) {
		List<Ticket> tickets = new ArrayList<>();

		if (tblTickets != null) {
			for (TblTicket tblTicket : tblTickets) {
				tickets.add(tblTicket.fromThis());
			}
		}

		return tickets;
	}

}