package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tbl_additional_data database table.
 * 
 */
@Entity
@Table(name="tbl_additional_data")
@NamedQuery(name="TblAdditionalData.findAll", query="SELECT t FROM TblAdditionalData t")
public class TblAdditionalData implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="id_additional_data")
	private String idAdditionalData;
	
	@Column(name="key_additional")
	private String keyAdditional;

	@Column(name="value_additional")
	private String valueAdditional;

	//bi-directional many-to-one association to TblTicket
	@ManyToOne
	@JoinColumn(name="id_ticket")
	private TblTicket tblTicket;

	public TblAdditionalData() {
	}

	public String getKeyAdditional() {
		return this.keyAdditional;
	}

	public void setKeyAdditional(String keyAdditional) {
		this.keyAdditional = keyAdditional;
	}

	public String getValueAdditional() {
		return this.valueAdditional;
	}

	public void setValueAdditional(String valueAdditional) {
		this.valueAdditional = valueAdditional;
	}

	public TblTicket getTblTicket() {
		return this.tblTicket;
	}

	public void setTblTicket(TblTicket tblTicket) {
		this.tblTicket = tblTicket;
	}

	public String getIdAdditionalData() {
		return idAdditionalData;
	}

	public void setIdAdditionalData(String idAdditionalData) {
		this.idAdditionalData = idAdditionalData;
	}
	
}