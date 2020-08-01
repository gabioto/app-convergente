package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.Ticket;

/**
 * The primary key class for the tbl_customer database table.
 * 
 */
@Embeddable
public class TblCustomerPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "document_type")
	private String documentType;

	@Column(name = "document_number")
	private String documentNumber;

	@Column(name = "service_code")
	private String serviceCode;

	public TblCustomerPK() {
	}

	public String getDocumentType() {
		return this.documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return this.documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getServiceCode() {
		return this.serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public TblCustomerPK(String documentType, String documentNumber, String serviceCode) {
		super();
		this.documentType = documentType;
		this.documentNumber = documentNumber;
		this.serviceCode = serviceCode;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TblCustomerPK)) {
			return false;
		}
		TblCustomerPK castOther = (TblCustomerPK) other;
		return this.documentType.equals(castOther.documentType) && this.documentNumber.equals(castOther.documentNumber)
				&& this.serviceCode.equals(castOther.serviceCode);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.documentType.hashCode();
		hash = hash * prime + this.documentNumber.hashCode();
		hash = hash * prime + this.serviceCode.hashCode();

		return hash;
	}

	public static TblCustomerPK from(Ticket ticket) {
		return new TblCustomerPK(ticket.getCustomer().getNationalType(), ticket.getCustomer().getNationalId(),
				ticket.getCustomer().getServiceCode());
	}

	public Customer fromThis() {
		return new Customer(documentNumber, documentType, serviceCode);
	}
}