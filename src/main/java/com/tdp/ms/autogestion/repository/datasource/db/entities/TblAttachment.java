package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * The persistent class for the tbl_attachment database table.
 * 
 */
@Entity
@Table(name = "tbl_attachment", schema="public")
@NamedQuery(name = "TblAttachment.findAll", query = "SELECT t FROM TblAttachment t")
public class TblAttachment implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id_attachment")
	private Integer idAttachment;
	
	@Column(name = "id_attachment_kafka")
	private Integer idAttachmentKafka;

	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "creation_date")
	private LocalDateTime creationDate;

	@Column(name = "name_attachment")
	private String nameAttachment;

	// bi-directional many-to-one association to TblTicket
	@ManyToOne
	@JoinColumn(name = "id_ticket")
	private TblTicket tblTicket;

	// bi-directional many-to-one association to TblAttachmentAdditionalData
	@OneToMany(mappedBy = "tblAttachment",targetEntity=TblAttachmentAdditionalData.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<TblAttachmentAdditionalData> tblAttachmentAdditionalData;

	public TblAttachment() {
	}

	public Integer getIdAttachmentKafka() {
		return idAttachmentKafka;
	}

	public void setIdAttachmentKafka(Integer idAttachmentKafka) {
		this.idAttachmentKafka = idAttachmentKafka;
	}

	public Integer getIdAttachment() {
		return this.idAttachment;
	}

	public void setIdAttachment(Integer idAttachment) {
		this.idAttachment = idAttachment;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;

	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getNameAttachment() {
		return this.nameAttachment;
	}

	public void setNameAttachment(String nameAttachment) {
		this.nameAttachment = nameAttachment;
	}

	public TblTicket getTblTicket() {
		return this.tblTicket;
	}

	public void setTblTicket(TblTicket tblTicket) {
		this.tblTicket = tblTicket;
	}

	public List<TblAttachmentAdditionalData> getTblAttachmentAdditionalData() {
		return this.tblAttachmentAdditionalData;
	}

	public void setTblAttachmentAdditionalData(List<TblAttachmentAdditionalData> tblAttachmentAdditionalData) {
		this.tblAttachmentAdditionalData = tblAttachmentAdditionalData;
	}

	public TblAttachmentAdditionalData addTblAttachmentAdditionalData(
			TblAttachmentAdditionalData tblAttachmentAdditionalData) {
		getTblAttachmentAdditionalData().add(tblAttachmentAdditionalData);
		tblAttachmentAdditionalData.setTblAttachment(this);

		return tblAttachmentAdditionalData;
	}

	public TblAttachmentAdditionalData removeTblAttachmentAdditionalData(
			TblAttachmentAdditionalData tblAttachmentAdditionalData) {
		getTblAttachmentAdditionalData().remove(tblAttachmentAdditionalData);
		tblAttachmentAdditionalData.setTblAttachment(null);

		return tblAttachmentAdditionalData;
	}

}