package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.tdp.ms.autogestion.model.Customer;


/**
 * The persistent class for the tbl_customer database table.
 * 
 */
@Entity
@Table(name="tbl_customer")
@NamedQuery(name="TblCustomer.findAll", query="SELECT t FROM TblCustomer t")
public class TblCustomer implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TblCustomerPK id;

	//bi-directional many-to-one association to TblTicket
	@OneToMany(mappedBy="tblCustomer")
	private List<TblTicket> tblTickets;

	public TblCustomer() {
	}

	public TblCustomerPK getId() {
		return this.id;
	}

	public void setId(TblCustomerPK id) {
		this.id = id;
	}

	public List<TblTicket> getTblTickets() {
		return this.tblTickets;
	}

	public void setTblTickets(List<TblTicket> tblTickets) {
		this.tblTickets = tblTickets;
	}

	public TblTicket addTblTicket(TblTicket tblTicket) {
		getTblTickets().add(tblTicket);
		tblTicket.setTblCustomer(this);

		return tblTicket;
	}

	public TblTicket removeTblTicket(TblTicket tblTicket) {
		getTblTickets().remove(tblTicket);
		tblTicket.setTblCustomer(null);

		return tblTicket;
	}

	public Customer fromThis() {
		return id.fromThis();
	}
}