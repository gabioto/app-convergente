package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.tdp.ms.autogestion.model.AdditionalData;


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
	
	public AdditionalData fromThis() {
		return new AdditionalData(keyAdditional, valueAdditional);
	}
	
	public static List<AdditionalData> listFromThis(List<TblAdditionalData> tblAddDataList){
		List<AdditionalData> addDataList = new ArrayList<>();
		for (TblAdditionalData tblAdditionalData : tblAddDataList) {
			addDataList.add(tblAdditionalData.fromThis());
		}
		
		return addDataList;
	}
}