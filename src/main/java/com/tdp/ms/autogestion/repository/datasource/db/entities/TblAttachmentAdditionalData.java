package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tbl_attachment_additional_data database table.
 * 
 */
@Entity
@Table(name="tbl_attach_additional_data", schema="public")
@NamedQuery(name="TblAttachmentAdditionalData.findAll", query="SELECT t FROM TblAttachmentAdditionalData t")
public class TblAttachmentAdditionalData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="id_attach_additional_data")
	private Integer idAttachAdditionalData;

	@Column(name="key_attachment_additional")
	private String keyAttachmentAdditional;

	@Column(name="value_attachment_additional")
	private String valueAttachmentAdditional;

	//bi-directional many-to-one association to TblAttachment
	@ManyToOne
	@JoinColumn(name="id_attachment")
	private TblAttachment tblAttachment;

	public TblAttachmentAdditionalData() {
	}

	public Integer getIdAttachAdditionalData() {
		return idAttachAdditionalData;
	}

	public void setIdAttachAdditionalData(Integer idAttachAdditionalData) {
		this.idAttachAdditionalData = idAttachAdditionalData;
	}

	public String getKeyAttachmentAdditional() {
		return this.keyAttachmentAdditional;
	}

	public void setKeyAttachmentAdditional(String keyAttachmentAdditional) {
		this.keyAttachmentAdditional = keyAttachmentAdditional;
	}

	public String getValueAttachmentAdditional() {
		return this.valueAttachmentAdditional;
	}

	public void setValueAttachmentAdditional(String valueAttachmentAdditional) {
		this.valueAttachmentAdditional = valueAttachmentAdditional;
	}

	public TblAttachment getTblAttachment() {
		return this.tblAttachment;
	}

	public void setTblAttachment(TblAttachment tblAttachment) {
		this.tblAttachment = tblAttachment;
	}

}