package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the tbl_status_history database table.
 * 
 */
@Entity
@Table(name = "tbl_status_history")
@NamedQuery(name = "TblStatusHistory.findAll", query = "SELECT t FROM TblStatusHistory t")
public class TblStatusHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id_status_history")
	private Integer idStatusHistory;

	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "status_change_date")
	private LocalDateTime statusChangeDate;

	@Column(name = "status_change_reason")
	private String statusChangeReason;

	@Column(name = "ticket_status")
	private String ticketStatus;

	// bi-directional many-to-one association to TblTicket
	@ManyToOne
	@JoinColumn(name = "id_ticket")
	private TblTicket tblTicket;

	public TblStatusHistory() {
		// Constructor without fields
	}

	public Integer getIdStatusHistory() {
		return this.idStatusHistory;
	}

	public void setIdStatusHistory(Integer idStatusHistory) {
		this.idStatusHistory = idStatusHistory;
	}

	public LocalDateTime getStatusChangeDate() {
		return statusChangeDate;
	}


	public void setStatusChangeDate(LocalDateTime statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
	}

	public String getStatusChangeReason() {
		return this.statusChangeReason;
	}

	public void setStatusChangeReason(String statusChangeReason) {
		this.statusChangeReason = statusChangeReason;
	}

	public String getTicketStatus() {
		return this.ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public TblTicket getTblTicket() {
		return this.tblTicket;
	}

	public void setTblTicket(TblTicket tblTicket) {
		this.tblTicket = tblTicket;
	}

}