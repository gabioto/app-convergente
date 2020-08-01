package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tbl_equivalence_notification database table.
 * 
 */
@Entity
@Table(name="tbl_equivalence_notification", schema="public")
@NamedQuery(name="TblEquivalenceNotification.findAll", query="SELECT t FROM TblEquivalenceNotification t")
public class TblEquivalenceNotification implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="id_equivalence_notification")
	private Integer idEquivalenceNotification;

	private String code;

	private String description;
	
	private String action;
	
	private String title;
	
	private String descriptiontitle;
	
	private String body;
	
	private String footer;
	
	private String icon;

	public TblEquivalenceNotification() {
	}

	public Integer getIdEquivalenceNotification() {
		return this.idEquivalenceNotification;
	}

	public void setIdEquivalenceNotification(Integer idEquivalenceNotification) {
		this.idEquivalenceNotification = idEquivalenceNotification;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public String getDescriptiontitle() {
		return descriptiontitle;
	}

	public String getBody() {
		return body;
	}

	public String getFooter() {
		return footer;
	}

	public String getIcon() {
		return icon;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescriptiontitle(String descriptiontitle) {
		this.descriptiontitle = descriptiontitle;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}