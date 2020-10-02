package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="tbl_log_data")
@NamedQuery(name="TblLogData.findAll", query="SELECT t FROM TblLogData t")
public class TblLogData implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id_log_data")
	private Integer idLogData;

	@Column(name = "id_ticket_triaje")
	private Integer idTicketTriaje;

	@Column(name = "document_number")
	private String documentNumber;

	@Column(name = "document_type")
	private String documentType;

	private String channel;

	@Column(name = "type_log")
	private String typeLog;

	private String request;

	private String response;

	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "date_created")
	private LocalDateTime dateCreated;

	@Column(name = "action_log")
	private String actionLog;

	public TblLogData() {
		// Constructor without fields
	}

	public Integer getIdLogData() {
		return idLogData;
	}

	public void setIdLogData(Integer idLogData) {
		this.idLogData = idLogData;
	}

	public Integer getIdTicketTriaje() {
		return idTicketTriaje;
	}

	public void setIdTicketTriaje(Integer idTicketTriaje) {
		this.idTicketTriaje = idTicketTriaje;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTypeLog() {
		return typeLog;
	}

	public void setTypeLog(String typeLog) {
		this.typeLog = typeLog;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getActionLog() {
		return actionLog;
	}

	public void setActionLog(String actionLog) {
		this.actionLog = actionLog;
	}

}
