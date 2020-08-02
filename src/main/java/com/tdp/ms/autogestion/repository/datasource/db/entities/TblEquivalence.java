package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tbl_equivalence database table.
 * 
 */
@Entity
@Table(name="tbl_equivalence")
@NamedQuery(name="TblEquivalence.findAll", query="SELECT t FROM TblEquivalence t")
public class TblEquivalence implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="id_equivalence")
	private Integer idEquivalence;

	@Column(name="attachment_name")
	private String attachmentName;

	@Column(name="name_equivalence")
	private String nameEquivalence;

	public TblEquivalence() {
	}

	public Integer getIdEquivalence() {
		return this.idEquivalence;
	}

	public void setIdEquivalence(Integer idEquivalence) {
		this.idEquivalence = idEquivalence;
	}

	public String getAttachmentName() {
		return this.attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getNameEquivalence() {
		return this.nameEquivalence;
	}

	public void setNameEquivalence(String nameEquivalence) {
		this.nameEquivalence = nameEquivalence;
	}

}